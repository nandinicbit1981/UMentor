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
import parimi.com.umentor.adapters.MentorListAdapter;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.models.User;

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
    MentorListAdapter mentorListAdapter;

    public FilteredMentorListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filtered_mentor_list, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            filteredMentorUid = (List<String>) bundle.get("filteredMentors");
        }
        mentorListAdapter = new MentorListAdapter(this.getActivity());
        listView.setAdapter(mentorListAdapter);
        getFilteredMentors();
        return view;
    }

    private void getFilteredMentors() {
        for(int i=0;i<filteredMentorUid.size() ; i++) {
            if(databaseHelper == null) {
                databaseHelper = new DatabaseHelper();
            }
            databaseHelper.getUsers().child(filteredMentorUid.get(i)).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = new User(
                            dataSnapshot.child("name").getValue().toString(),
                            dataSnapshot.child("id").getValue().toString(),
                            dataSnapshot.child("email").getValue().toString(),
                            dataSnapshot.child("gender").getValue().toString(),
                            Integer.parseInt(dataSnapshot.child("age").getValue().toString()),
                            dataSnapshot.child("expertise").getValue().toString(),
                            Integer.parseInt(dataSnapshot.child("experience").getValue().toString())
                    );
                    if(!filteredMentors.contains(user)) {
                        filteredMentors.add(user);
                        if(filteredMentors.size() == filteredMentorUid.size()) {
                            mentorListAdapter.setUsersLlist(filteredMentors);
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
