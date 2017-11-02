package parimi.com.umentor.views.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parimi.com.umentor.ButtonClickInterface;
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.CategoryAdapter;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.Constants;
import parimi.com.umentor.models.Category;
import parimi.com.umentor.models.Notification;
import parimi.com.umentor.models.User;
import parimi.com.umentor.views.activity.MainActivity;

import static parimi.com.umentor.helper.Constants.FILTEREDMENTORS;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class MentorSearchFragment extends android.support.v4.app.Fragment implements ButtonClickInterface {

    @Inject
    DatabaseHelper databaseHelper;

    @BindView(R.id.gridview)
    GridView gridView;

    @BindView(R.id.search)
    Button searchBtn;
    List<Category> selectedCategories = new ArrayList<>();
    List<User> filteredMentors = new ArrayList<>();
    List<String> filteredMentorUid = new ArrayList<>();
    CategoryAdapter categoryAdapter;

    public MentorSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UMentorDaggerInjector.get().inject(this);
        View view = inflater.inflate(R.layout.fragment_mentor_search, container, false);
        ButterKnife.bind(this, view);

        final List<Category> categories = new ArrayList<>();

        databaseHelper.getCategories().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categoriesSnapshot: dataSnapshot.getChildren()) {
                    categories.add(new Category(categoriesSnapshot.getValue().toString()));
                }

                categoryAdapter = new CategoryAdapter(getActivity(), categories, Constants.MENTORSEARCHFRAGMENT);
                categoryAdapter.setOnCategorySelected(MentorSearchFragment.this);
                gridView.setAdapter(categoryAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onItemSelected(String name) {
        Category category = new Category(name);
        List<String> categoryStringList = new ArrayList<>();
        if(!selectedCategories.contains(category)) {
            selectedCategories.add(category);
        } else {
            selectedCategories.remove(category);
        }

        for(Category category1 : selectedCategories) {
            if(!categoryStringList.contains(category1.getCategory())) {
                categoryStringList.add(category1.getCategory());
            } else {
                categoryStringList.remove(category1.getCategory());
            }

        }
        if(selectedCategories.size() > 0) {
            searchBtn.setEnabled(true);
            searchBtn.setBackground(getContext().getDrawable(R.drawable.round_blue_button));
        } else {
            searchBtn.setEnabled(false);
            searchBtn.setBackground(getContext().getDrawable(R.drawable.round_disabled_button));
        }
        categoryAdapter.setCategoriesSelected(categoryStringList);
    }

    @Override
    public void onRequestAccepted(Notification notification, String acceptOrReject) {

    }

    @OnClick(R.id.search)
    public void onSearchClicked() {
        for (Category category : selectedCategories) {

            databaseHelper.getSelectedCategories().child(category.getCategory()).orderByValue().equalTo(true).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapShot: dataSnapshot.getChildren()) {
                        if(!filteredMentorUid.contains(userSnapShot.getKey().toString())) {
                            filteredMentorUid.add(userSnapShot.getKey().toString());
                        }
                    }
                    android.support.v4.app.Fragment fragment = new FilteredMentorListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(FILTEREDMENTORS, (Serializable) filteredMentorUid);
                    fragment.setArguments(bundle);
                    try {
                        ((MainActivity) getActivity()).insertFragment(fragment);
                    } catch(Exception e) {
                        System.out.println(e.getLocalizedMessage());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
