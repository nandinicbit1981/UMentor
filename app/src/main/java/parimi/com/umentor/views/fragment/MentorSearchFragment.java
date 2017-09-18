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
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.CategoryAdapter;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.models.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class MentorSearchFragment extends Fragment {

    @Inject
    DatabaseHelper databaseHelper;

    @BindView(R.id.gridview)
    GridView gridView;


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

                CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories);
                gridView.setAdapter(categoryAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
