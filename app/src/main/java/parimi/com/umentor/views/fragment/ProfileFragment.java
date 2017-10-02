package parimi.com.umentor.views.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parimi.com.umentor.R;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.MentorStatus;
import parimi.com.umentor.helper.SharedPreferenceHelper;
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

    @BindView(R.id.expertise)
    TextView expertiseTxt;

    @BindView(R.id.experience)
    TextView experienceTxt;

    @BindView(R.id.email)
    TextView emailTxt;

    @BindView(R.id.editButton)
    Button editButton;

    MainActivity mainActivity;

    User currentUser;

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
        Bundle bundle = getArguments();
        User currentUser = SharedPreferenceHelper.getCurrentUser(getContext());
        if (bundle != null) {
            user = (User) bundle.get("user");
            if(!currentUser.getId().equals(user.getId())) {
                editButton.setText(getString(R.string.request_mentor));
            }
            nameTxt.setText(user.getName());
            emailTxt.setText(user.getEmail());
            ageTxt.setText(String.valueOf(user.getAge()));
            experienceTxt.setText(String.valueOf(user.getExperience()));

            String categoryString = "";
//            if(user.getCategory() != null && user.getCategory() != null) {
//                for (String category : user.getCategory().keySet()) {
//                    if (categoryString != "") {
//                        categoryString += ", ";
//                    }
//                    categoryString += category;
//                }
//            }
//            expertiseTxt.setText(categoryString);
        }
        if(user != null && user.getId() != null) {
            databaseHelper.saveUser(user);
        }
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
        } else {
            Requests requests = new Requests(null,
                    currentUser.getId(),
                    user.getId(),
                    MentorStatus.REQUEST_MENTOR,
                    currentUser.getName(),
                    currentUser.getFcmToken()
                    );
            databaseHelper.saveRequest(requests);
            RestInterface.sendNotification(getContext(), user.getFcmToken(), "Friend Request", currentUser.getName() + " wants to add you as a friend");
        }

    }

}
