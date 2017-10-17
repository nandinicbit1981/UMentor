package parimi.com.umentor.views.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.SearchedMentorListAdapter;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.models.User;

import static parimi.com.umentor.helper.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilteredMentorListFragment extends Fragment {

    List<String> filteredMentorUid = new ArrayList<>();
    List<User> filteredMentors = new ArrayList<>();

    @Inject
    DatabaseHelper databaseHelper;

    @BindView(R.id.filtered_mentor_list)
    ListView listView;
    SearchedMentorListAdapter searchedMentorListAdapter;

    public FilteredMentorListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UMentorDaggerInjector.get().inject(this);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filtered_mentor_list, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            filteredMentorUid = (List<String>) bundle.get(FILTEREDMENTORS);
        }
        searchedMentorListAdapter = new SearchedMentorListAdapter(this.getActivity());
        listView.setAdapter(searchedMentorListAdapter);
        getFilteredMentors();
        return view;
    }

    private void getFilteredMentors() {
        for(int i=0;i<filteredMentorUid.size() ; i++) {

            databaseHelper.getUsers().child(filteredMentorUid.get(i)).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue() != null) {
                        User user = new User(
                                dataSnapshot.child(NAME).getValue().toString(),
                                dataSnapshot.child(ID).getValue().toString(),
                                dataSnapshot.child(EMAIL).getValue().toString(),
                                dataSnapshot.child(GENDER).getValue().toString(),
                                Integer.parseInt(dataSnapshot.child(AGE).getValue().toString()),
                                dataSnapshot.child(SUMMARY).getValue().toString(),
                                Integer.parseInt(dataSnapshot.child(EXPERIENCE).getValue().toString()),
                                dataSnapshot.child(FCMTOKEN).getValue() != null ? dataSnapshot.child(FCMTOKEN).getValue().toString() : "",
                                Float.parseFloat(dataSnapshot.child(RATING).getValue().toString()),
                                (List<String>) dataSnapshot.child(CATEGORIES).getValue(),
                                dataSnapshot.child(JOB).getValue().toString(),
                                dataSnapshot.child(PROFILEPIC).getValue().toString()
                        );
                        if (!filteredMentors.contains(user)) {
                            filteredMentors.add(user);
                            if (filteredMentors.size() == filteredMentorUid.size()) {
                                searchedMentorListAdapter.setUsersLlist(filteredMentors);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
