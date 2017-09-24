package parimi.com.umentor.views.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.ButterKnife;
import parimi.com.umentor.R;
import parimi.com.umentor.helper.BottomNavigationViewHelper;
import parimi.com.umentor.models.User;
import parimi.com.umentor.views.fragment.MentorSearchFragment;
import parimi.com.umentor.views.fragment.MessagesFragment;
import parimi.com.umentor.views.fragment.NotificationsFragment;
import parimi.com.umentor.views.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    User user;
    FirebaseUser firebaseUser;
    private static final int RC_SIGN_IN = 999 ;
    private static final int RC_PHOTO_PICKER = 2;
    private static final String USER = "user";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.profile:
                                selectedFragment = new ProfileFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(USER, user);
                                selectedFragment.setArguments(bundle);
                                ((ProfileFragment)selectedFragment).setActivity(MainActivity.this);
                                break;
                            case R.id.search:
                                selectedFragment = new MentorSearchFragment();
                                break;
                            case R.id.messages:
                                selectedFragment = new MessagesFragment();
                                break;
                            case R.id.notifications:
                                selectedFragment = new NotificationsFragment();
                                break;

                        }
                       insertFragment(selectedFragment);
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        ProfileFragment fragment = new ProfileFragment();
        fragment.setActivity(MainActivity.this);
        insertFragment(fragment);
    }

    private void updateUsername(final FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        FirebaseDatabase.getInstance().getReference("umentor-d21ff").child("users").child(firebaseUser.getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                     HashMap<String, String> categories = new HashMap<>();
                    if (dataSnapshot.child("category").getValue() != null) {
                        categories= (HashMap<String, String>) dataSnapshot.child("category").getValue();
                    }
                    user = new User(
                            dataSnapshot.child("name").getValue().toString(),
                            dataSnapshot.child("id").getValue().toString(),
                            dataSnapshot.child("email").getValue().toString(),
                            dataSnapshot.child("gender").getValue().toString(),
                            Integer.parseInt(dataSnapshot.child("age").getValue().toString()),
                            dataSnapshot.child("expertise").getValue().toString(),
                            Integer.parseInt(dataSnapshot.child("experience").getValue().toString())
                    );

                } else{
                    user = new User(
                            firebaseUser.getDisplayName(),
                            firebaseUser.getUid(),
                            firebaseUser.getEmail(),
                            "",
                            0,
                            "",
                            0);
                }


                Fragment fragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(USER, user);
                fragment.setArguments(bundle);
                insertFragment(fragment);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onResume() {
        Fragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        fragment.setArguments(bundle);
        super.onResume();
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

    public void insertFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment).commit();
    }


}
