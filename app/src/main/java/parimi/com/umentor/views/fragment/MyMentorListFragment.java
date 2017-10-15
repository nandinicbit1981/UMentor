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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMentorListFragment extends Fragment {


    @BindView(R.id.mentor_list)
    ListView listView;

    @Inject
    DatabaseHelper databaseHelper;

    List<String> mentorIds = new ArrayList<>();
    List<User> mentors = new ArrayList<>();
    MentorListAdapter mentorListAdapter;

    User currentUser;


    public MyMentorListFragment() {
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
                    if(dataSnapshot1.child("mentor").getValue().equals(true)) {
                        mentorIds.add(dataSnapshot1.getKey().toString());
                    }
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
                            dataSnapshot.child("name").getValue().toString(),
                            dataSnapshot.child("id").getValue().toString(),
                            dataSnapshot.child("email").getValue().toString(),
                            dataSnapshot.child("gender").getValue().toString(),
                            Integer.parseInt(dataSnapshot.child("age").getValue().toString()),
                            dataSnapshot.child("summary").getValue().toString(),
                            Integer.parseInt(dataSnapshot.child("experience").getValue().toString()),
                            dataSnapshot.child("fcmToken").getValue().toString(),
                            Float.parseFloat(dataSnapshot.child("rating").getValue().toString()),
                            (List<String>)dataSnapshot.child("categories").getValue(),
                            dataSnapshot.child("job").getValue().toString(),
                            dataSnapshot.child("profilePic").getValue().toString()
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
