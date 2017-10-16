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
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.models.User;

import static parimi.com.umentor.helper.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageListFragment extends Fragment {

    @BindView(R.id.mentor_list)
    ListView listView;

    @Inject
    DatabaseHelper databaseHelper;

    List<String> mentorIds = new ArrayList<>();
    List<User> mentors = new ArrayList<>();
    List<String> menteeIds = new ArrayList<>();
    MentorListAdapter mentorListAdapter;

    User currentUser;


    public MessageListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UMentorDaggerInjector.get().inject(this);
        View view =  inflater.inflate(R.layout.fragment_my_mentor_list, container, false);
        ButterKnife.bind(this, view);
        currentUser = SharedPreferenceHelper.getCurrentUser(getActivity());
        databaseHelper.getNetwork().child(currentUser.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    mentorIds.add(dataSnapshot1.getKey().toString());
                }
                getMentorProfiles(mentorIds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mentorListAdapter = new MentorListAdapter(mentors, getActivity());
        listView.setAdapter(mentorListAdapter);
        return view;
    }

    private void getMentorProfiles(final List<String> mentorIds) {
        for(String mentorId : mentorIds) {
            databaseHelper.getUsers().child(mentorId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = new User(
                            dataSnapshot.child(NAME).getValue().toString(),
                            dataSnapshot.child(ID).getValue().toString(),
                            dataSnapshot.child(EMAIL).getValue().toString(),
                            dataSnapshot.child(GENDER).getValue().toString(),
                            Integer.parseInt(dataSnapshot.child(AGE).getValue().toString()),
                            dataSnapshot.child(SUMMARY).getValue().toString(),
                            Integer.parseInt(dataSnapshot.child(EXPERIENCE).getValue().toString()),
                            dataSnapshot.child(FCMTOKEN).getValue().toString(),
                            Float.parseFloat(dataSnapshot.child(RATING).getValue().toString()),
                            (List<String>)dataSnapshot.child(CATEGORIES).getValue(),
                            dataSnapshot.child(JOB).getValue().toString(),
                            dataSnapshot.child(PROFILEPIC).getValue().toString()
                    );
                    mentors.add(user);
                    if(mentorIds.size() == mentors.size()) {
                        mentorListAdapter.setMentors(mentors);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


}
