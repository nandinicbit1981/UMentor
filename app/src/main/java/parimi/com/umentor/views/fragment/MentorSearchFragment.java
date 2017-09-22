package parimi.com.umentor.views.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parimi.com.umentor.MentorSearchButtonClickInterface;
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.CategoryAdapter;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.Constants;
import parimi.com.umentor.models.Category;
import parimi.com.umentor.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class MentorSearchFragment extends Fragment implements MentorSearchButtonClickInterface{

    @Inject
    DatabaseHelper databaseHelper;

    @BindView(R.id.gridview)
    GridView gridView;



    List<Category> selectedCategories = new ArrayList<>();
    List<User> filteredMentors = new ArrayList<>();


    public MentorSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mentor_search, container, false);
        ButterKnife.bind(this, view);

        final List<Category> categories = new ArrayList<>();
        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }

        databaseHelper.getCategories().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categoriesSnapshot: dataSnapshot.getChildren()) {
                    categories.add(new Category(categoriesSnapshot.getValue().toString()));
                }


                CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories, Constants.MENTORSEARCHFRAGMENT);
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
        if(!selectedCategories.contains(category)) {
            selectedCategories.add(category);
        }
    }

    @OnClick(R.id.search)
    public void onSearchClicked() {
        for (Category category : selectedCategories) {
            databaseHelper.getUsers().child("category").child(category.getCategory()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
