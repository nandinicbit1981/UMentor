package parimi.com.umentor.views.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import parimi.com.umentor.R;
import parimi.com.umentor.models.User;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.username)
    TextView mUsernameTxt;

    private String mUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    private static final int RC_SIGN_IN = 999 ;
    private static final int RC_PHOTO_PICKER = 2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//
//
//        mCallbackManager = CallbackManager.Factory.create();
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions("email", "public_profile");
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//
//                        try {
//                            String name = object.getString("name");
//                            String id = object.getString("id");
//                            String email = object.getString("email");
//
//                            Log.d("Name + Email + ID", name + " " + email + " " + id);
//                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                            intent.putExtra("name", name);
//                            intent.putExtra("id", id);
//                            intent.putExtra("email", email);
//                            startActivity(intent);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email");
//                graphRequest.setParameters(parameters);
//                graphRequest.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null) {
                    //signed in
                    updateUsername(currentUser);
                    attachChildListener();
                } else {
                    // not signed in

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    private void updateUsername(FirebaseUser firebaseUser) {
        User user = new User(
                firebaseUser.getDisplayName(),
                firebaseUser.getUid(),
                firebaseUser.getEmail(),
                "",
                "",
                1);

        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        mUsernameTxt.setText(mUsername);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null ) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachChildListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN :
                if(resultCode == RESULT_OK)
                    System.out.println(data);
                    Toast.makeText(getBaseContext(), "Signed in", Toast.LENGTH_LONG);
                if(resultCode == RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), " Not signed in", Toast.LENGTH_SHORT );
                }
                break;
            case RC_PHOTO_PICKER :
                Uri selectedImage = data.getData();
                StorageReference photoRef = mStorageReference.child(selectedImage.getLastPathSegment());
                photoRef.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            }
                        }
                );
                break;
            default:

        }

    }

    private void detachChildListener() {

        if( mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }

    private void attachChildListener() {
        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }


}
