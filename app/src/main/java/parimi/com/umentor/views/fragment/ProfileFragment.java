package parimi.com.umentor.views.fragment;


import android.app.Activity;
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
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.models.NetworkUser;
import parimi.com.umentor.models.Requests;
import parimi.com.umentor.models.User;
import parimi.com.umentor.rest.RestInterface;
import parimi.com.umentor.views.activity.MainActivity;

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
//
//    @BindView(R.id.experience)
//    TextView experienceTxt;

      @BindView(R.id.editButton)
      Button editButton;

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
            user = (User) bundle.get("user");
            if(!currentUser.getId().equals(user.getId())) {
                editButton.setText(getString(R.string.request_mentor));
            }
            ratingBar.setEnabled(false);
            ratingBar.setRating(user.getRating());
            nameTxt.setText(user.getName());
            ageTxt.setText(String.valueOf(user.getAge()));
            summaryTxt.setText(user.getSummary());
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
                        networkUserIdList.add(networkUser.getUserId());
                    }
                    if(networkUser.getUserId().equals(currentUser.getId())) {
                        editButton.setText(getString(R.string.message));
                        ratingBar.setEnabled(true);
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
            bundle.putSerializable("user", user);
            fragment.setArguments(bundle);
            ((MainActivity) getActivity()).insertFragment(fragment);
        }

        else if(networkUserIdList.contains(user.getId())) {
            Bundle messageBundle =  new Bundle();
            messageBundle.putSerializable("user", user);
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
        }
    }


}
