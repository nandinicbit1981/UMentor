package parimi.com.umentor.views.fragment;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parimi.com.umentor.R;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.MentorStatus;
import parimi.com.umentor.helper.RoundedImageView;
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.models.NetworkUser;
import parimi.com.umentor.models.Requests;
import parimi.com.umentor.models.User;
import parimi.com.umentor.rest.RestInterface;
import parimi.com.umentor.views.activity.MainActivity;

import static parimi.com.umentor.helper.CommonHelper.decodeFromFirebaseBase64;
import static parimi.com.umentor.helper.Constants.USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    @Inject
    DatabaseHelper databaseHelper;

    User user;

    @BindView(R.id.name)
    TextView nameTxt;

    @BindView(R.id.age)
    TextView ageTxt;

    @BindView(R.id.summary)
    TextView summaryTxt;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.job)
    TextView jobTxt;

    @BindView(R.id.editButton)
    Button editButton;

    @BindView(R.id.imageView)
    RoundedImageView imageView;

    MainActivity mainActivity;

    User currentUser;
    List<NetworkUser> networkUserList = new ArrayList<>();
    List<String> networkUserIdList = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }

    public void setActivity(Activity activity) {
        mainActivity = (MainActivity)activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UMentorDaggerInjector.get().inject(this);
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        final Bundle bundle = getArguments();
        currentUser = SharedPreferenceHelper.getCurrentUser(getContext());
        if (bundle != null) {
            user = (User) bundle.get(USER);
            if(!currentUser.getId().equals(user.getId())) {
                editButton.setText(getString(R.string.request_mentor));
            }
           // ratingBar.setEnabled(false);
            ratingBar.setClickable(false);
            ratingBar.setIsIndicator(true);

            try {

                Bitmap bitmap = decodeFromFirebaseBase64(user.getProfilePic());
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ratingBar.setRating(user.getRating());
            nameTxt.setText(user.getName());
            ageTxt.setText(String.valueOf(user.getAge()));
            summaryTxt.setText(user.getSummary());
            jobTxt.setText(user.getJob());

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    float existingRating = 0;
                    int count = 0;

                    for(NetworkUser networkUser : networkUserList) {
                        if(networkUser.isRatingGiven()) {
                            if(!networkUser.getUserId().equals(currentUser.getId())) {
                                existingRating += networkUser.getRating();
                                count++;
                            }

                        }
                    }

                    existingRating += v;
                    user.setRating(existingRating/(count + 1));
                    databaseHelper.saveUser(user);
                    databaseHelper.addMenteeToMentor(currentUser.getId(), user.getId(), v, true);
                }
            });
            //experienceTxt.setText(String.valueOf(user.getExperience()));

            String categoryString = "";
//            if(user.getCategory() != null && user.getCategory() != null) {
//                for (String category : user.getCategory().keySet()) {
//                    if (categoryString != "") {
//                        categoryString += ", ";
//                    }
//                    categoryString += category;
//                }
//            }
//            summaryTxt.setText(categoryString);
        }

        databaseHelper.getNetwork().child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    NetworkUser networkUser = new NetworkUser();
                    networkUser.setUserId(ds.getKey());
                    networkUser.setRatingGiven((Boolean) ds.child("setRatingGiven").getValue());
                    networkUser.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));

                    if(!networkUserList.contains(networkUser)) {
                        networkUserList.add(networkUser);
                    }
                    if(!networkUserIdList.contains(networkUser.getUserId())) {
                        if(networkUser.getUserId().equals(currentUser.getId()) && !networkUserIdList.contains(dataSnapshot.getKey())){
                            networkUserIdList.add(dataSnapshot.getKey());
                        } else {
                            networkUserIdList.add(networkUser.getUserId());
                        }
                    }

                    if(networkUser.getUserId().equals(currentUser.getId())) {
                        editButton.setText(getString(R.string.send_message_to_mentor));
                        ratingBar.setEnabled(true);
                        ratingBar.setIsIndicator(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @OnClick(R.id.editButton)
    public void onEditButtonClick() {
        Fragment fragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        if(currentUser == null) {
            currentUser = SharedPreferenceHelper.getCurrentUser(getContext());
        }
        if(currentUser.getId().equals(user.getId())) {
            bundle.putSerializable(USER, user);
            fragment.setArguments(bundle);
            ((MainActivity) getActivity()).insertFragment(fragment);
        }

        else if(networkUserIdList.contains(user.getId())) {
            Bundle messageBundle =  new Bundle();
            messageBundle.putSerializable(USER, user);
            SendMessageFragment sendMessageFragment = new SendMessageFragment();
            sendMessageFragment.setArguments(messageBundle);

            ((MainActivity)getActivity()).insertFragment(sendMessageFragment);
        } else {
            Requests requests = new Requests(null,
                    currentUser.getId(),
                    user.getId(),
                    MentorStatus.REQUEST_MENTOR,
                    currentUser.getName(),
                    currentUser.getFcmToken()
                    );
            databaseHelper.saveRequest(requests);
            RestInterface.sendNotification(getContext(), user.getFcmToken(), "Mentor Request", currentUser.getName() + " wants to add you as a mentor");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(networkUserIdList.contains(user.getId())) {
            editButton.setText(getString(R.string.message));
            ratingBar.invalidate();
            ratingBar.setIsIndicator(false);
        }
    }


}
