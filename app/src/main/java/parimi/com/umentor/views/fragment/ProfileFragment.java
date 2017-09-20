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
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.models.User;
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

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.get("user");
            nameTxt.setText(user.getName());
            emailTxt.setText(user.getEmail());
            ageTxt.setText(String.valueOf(user.getAge()));
            experienceTxt.setText(String.valueOf(user.getExperience()));
            expertiseTxt.setText(String.valueOf(user.getExpertise()));
        }
        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
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
        bundle.putSerializable("user", user);
        fragment.setArguments(bundle);
        ((MainActivity)getActivity()).insertFragment(fragment);

    }

}
