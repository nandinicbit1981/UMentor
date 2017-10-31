package parimi.com.umentor.views.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import parimi.com.umentor.R;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.BottomNavigationViewHelper;
import parimi.com.umentor.helper.Constants;
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.helper.UMentorHelper;
import parimi.com.umentor.models.User;
import parimi.com.umentor.views.fragment.EditProfileFragment;
import parimi.com.umentor.views.fragment.MentorSearchFragment;
import parimi.com.umentor.views.fragment.MessageListFragment;
import parimi.com.umentor.views.fragment.MyMentorListFragment;
import parimi.com.umentor.views.fragment.NotificationsFragment;
import parimi.com.umentor.views.fragment.ProfileFragment;
import parimi.com.umentor.widget.UpdateWidgetService;

import static parimi.com.umentor.helper.Constants.AGE;
import static parimi.com.umentor.helper.Constants.CATEGORIES;
import static parimi.com.umentor.helper.Constants.EMAIL;
import static parimi.com.umentor.helper.Constants.EXPERIENCE;
import static parimi.com.umentor.helper.Constants.GENDER;
import static parimi.com.umentor.helper.Constants.ID;
import static parimi.com.umentor.helper.Constants.JOB;
import static parimi.com.umentor.helper.Constants.NAME;
import static parimi.com.umentor.helper.Constants.PROFILEPIC;
import static parimi.com.umentor.helper.Constants.RATING;
import static parimi.com.umentor.helper.Constants.SUMMARY;
import static parimi.com.umentor.helper.Constants.UPDATEALL;
import static parimi.com.umentor.helper.Constants.UPDATEWIDGETTYPE;
import static parimi.com.umentor.helper.Constants.USER;
import static parimi.com.umentor.helper.Constants.USERS;

public class MainActivity extends AppCompatActivity {

    @Inject
    DatabaseHelper databaseHelper;

    @Inject
    UpdateWidgetService service;


    private String fcmToken;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private BottomNavigationView bottomNavigationView;
    User user;
    FirebaseUser firebaseUser;
    private static final int RC_SIGN_IN = 999 ;
    private static final int RC_PHOTO_PICKER = 2;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        UMentorDaggerInjector.get().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("UMentor");

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setBackgroundDrawable(getDrawable(R.drawable.toolbar_gradient));
        fcmToken = FirebaseInstanceId.getInstance().getToken();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(USERS);

        createAuthStateListener();
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        UMentorHelper.hideKeyboard(getBaseContext(), bottomNavigationView);
                        android.support.v4.app.Fragment selectedFragment = null;
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
                            case R.id.mentors:
                                selectedFragment = new MyMentorListFragment();
                                break;
                            case R.id.messages:
                                selectedFragment = new MessageListFragment();
                                break;
                            case R.id.notifications:
                                selectedFragment = new NotificationsFragment();
                                break;

                        }
                       insertFragment(selectedFragment);
                        return true;
                    }
                });

        super.onCreate(null);
    }

    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void createAuthStateListener() {

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
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setTheme(R.style.LoginTheme)
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void updateUsername(final FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
            if(fcmToken == null) {
                fcmToken = FirebaseInstanceId.getInstance().getToken();
            }
            databaseHelper.getUsers().child(firebaseUser.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        HashMap<String, String> categories = new HashMap<>();
                        if (dataSnapshot.child(Constants.CATEGORY).getValue() != null) {
                            categories = (HashMap<String, String>) dataSnapshot.child(Constants.CATEGORY).getValue();
                        }
                        user = new User(
                                dataSnapshot.child(NAME).getValue().toString(),
                                dataSnapshot.child(ID).getValue().toString(),
                                dataSnapshot.child(EMAIL).getValue().toString(),
                                dataSnapshot.child(GENDER).getValue().toString(),
                                Integer.parseInt(dataSnapshot.child(AGE).getValue().toString()),
                                dataSnapshot.child(SUMMARY).getValue().toString(),
                                Integer.parseInt(dataSnapshot.child(EXPERIENCE).getValue().toString()),
                                fcmToken,
                                Float.parseFloat(dataSnapshot.child(RATING).getValue().toString()),
                                (List<String>)dataSnapshot.child(CATEGORIES).getValue(),
                                dataSnapshot.child(JOB).getValue().toString(),
                                dataSnapshot.child(PROFILEPIC).getValue().toString()

                        );


                    } else {
                        user = new User(
                                firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "",
                                firebaseUser.getUid(),
                                firebaseUser.getEmail(),
                                "",
                                0,
                                "",
                                0,
                                fcmToken,
                                0,
                                new ArrayList<String>(),
                                "",
                                ""
                        );

                    }
                    databaseHelper.saveUser(user);
                    SharedPreferenceHelper.saveUser(MainActivity.this, user);


                    Intent intent = new Intent(MainActivity.this, UpdateWidgetService.class);
                    intent.putExtra(UPDATEWIDGETTYPE, UPDATEALL);
                    startService(intent);

                    Fragment fragment;
                    if(user.getAge() > 0) {
                      fragment = new ProfileFragment();
                    } else {
                      fragment = new EditProfileFragment();
                    }
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
        super.onResume();
        Fragment fragment;
//        if(user != null) {
//            if(user.getAge() > 0) {
//               fragment = new ProfileFragment();
//            } else {
//                fragment = new EditProfileFragment();
//            }
//            fcmToken = FirebaseInstanceId.getInstance().getToken();
//            user.setFcmToken(fcmToken);
//            databaseHelper.saveUser(user);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(USER, user);
//            fragment.setArguments(bundle);
//            insertFragment(fragment);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_SIGN_IN :
                if(resultCode == RESULT_OK)
                    System.out.println(data);

                if(resultCode == RESULT_CANCELED) {

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

    public void insertFragment(final android.support.v4.app.Fragment fragment) {
        if(!isFinishing()) {
            new Handler().post(new Runnable() {
                public void run() {
                    FragmentManager manager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame_layout, fragment).commitAllowingStateLoss();
                }
            });
        }
    }



}
