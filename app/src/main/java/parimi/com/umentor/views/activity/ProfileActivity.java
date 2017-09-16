package parimi.com.umentor.views.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import parimi.com.umentor.R;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.models.User;


public class ProfileActivity extends AppCompatActivity {

    @Inject
    DatabaseHelper databaseHelper;

    @BindView(R.id.name)
    TextView nameTxt;

    @BindView(R.id.email)
    TextView emailTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        User user = (User) getIntent().getExtras().getParcelable("user");

        nameTxt.setText(user.getName());
        emailTxt.setText(user.getEmail());

        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }
        databaseHelper.saveUser(user);
    }

}
