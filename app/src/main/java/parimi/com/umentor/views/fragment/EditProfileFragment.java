package parimi.com.umentor.views.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import parimi.com.umentor.CheckBoxClickInterface;
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.CategoryAdapter;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.Constants;
import parimi.com.umentor.models.Category;
import parimi.com.umentor.models.User;
import parimi.com.umentor.views.activity.MainActivity;

import static parimi.com.umentor.helper.Constants.USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements CheckBoxClickInterface {

    User user;

    @Inject
    DatabaseHelper databaseHelper;

    @BindView(R.id.name)
    EditText nameTxt;

    @BindView(R.id.age)
    EditText ageTxt;

    @BindView(R.id.experience)
    EditText experienceTxt;

    @BindView(R.id.summaryEditText)
    EditText expertiseTxt;

    @BindView(R.id.jobTxt)
    EditText jobEditTxt;

    @BindView(R.id.saveButton)
    Button saveButton;


    List<Category> categories = new ArrayList<>();

    List<String> selectedCategories = new ArrayList<>();

    HashMap<String, Boolean> saveSelectedCategories = new HashMap<>();
    CategoryAdapter categoryAdapter;

    public  EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UMentorDaggerInjector.get().inject(this);
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.get(USER);
            nameTxt.setText(user.getName());
            ageTxt.setText(String.valueOf(user.getAge()));
            experienceTxt.setText(String.valueOf(user.getExperience()));
            expertiseTxt.setText(user.getSummary());
            selectedCategories = user.getCategories();
            jobEditTxt.setText(user.getJob());
        }


        databaseHelper.getCategories().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categoriesSnapshot: dataSnapshot.getChildren()) {
                    categories.add(new Category(categoriesSnapshot.getValue().toString()));
                }

                categoryAdapter = new CategoryAdapter(getActivity(), categories, Constants.EDITPROFILEFRAGMENT);
                categoryAdapter.setOnChechboxItemSelected(EditProfileFragment.this);
                categoryAdapter.setCategoriesSelected(selectedCategories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @OnTextChanged({R.id.name, R.id.age, R.id.experience, R.id.summaryEditText})
    public void fieldsChanged() {
        enableDisableSaveButton();
    }


    public void enableDisableSaveButton() {
        int age = ageTxt.getText().toString().equals("") ? 0 : Integer.parseInt(ageTxt.getText().toString());
        int experience =  experienceTxt.getText().toString().equals("") ? 0 : Integer.parseInt(experienceTxt.getText().toString());

        if(nameTxt.getText().length() > 0 &&
                age > 10 &&
                experience > 0 &&
                jobEditTxt.getText().length() > 0 &&
                expertiseTxt.getText().length() > 0 &&
                (selectedCategories != null && selectedCategories.size() > 0)) {
            saveButton.setEnabled(true);
            saveButton.setBackground(getActivity().getDrawable(R.drawable.round_transparent_button));
        } else {
            saveButton.setEnabled(false);
            saveButton.setBackground(getActivity().getDrawable(R.drawable.round_disabled_button));
        }

    }


    @OnClick(R.id.saveButton)
    public void onSaveButtonClicked() {

        user.setName(nameTxt.getText().toString());
        user.setAge(Integer.parseInt(ageTxt.getText().toString()));
        user.setExperience(Integer.parseInt(experienceTxt.getText().toString()));
        user.setSummary(expertiseTxt.getText().toString());
        user.setCategories(selectedCategories);
        user.setJob(jobEditTxt.getText().toString());
        databaseHelper.saveUser(user);

        for(int i=0;i < selectedCategories.size();i++) {
            saveSelectedCategories.put(user.getId(), true);
            databaseHelper.saveUserToCategories(selectedCategories.get(i), user.getId());
        }
        Fragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        fragment.setArguments(bundle);
        ((MainActivity)getActivity()).insertFragment(fragment);
    }

    @OnClick(R.id.select)
    public void onSelectButtonClicked() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.categories_list_view, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle(R.string.choose_a_category);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ListView lv = (ListView) convertView.findViewById(R.id.category_list_view);
        lv.setAdapter(categoryAdapter);
        alertDialog.show();
    }


    @Override
    public void onItemSelected(String name) {
        if(selectedCategories == null) {
            selectedCategories = new ArrayList<>();
        }
        if(selectedCategories.contains(name)) {
            selectedCategories.remove(name);
        } else {
            selectedCategories.add(name);
        }
        enableDisableSaveButton();
    }
}
