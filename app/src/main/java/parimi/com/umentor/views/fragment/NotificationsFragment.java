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
import parimi.com.umentor.ButtonClickInterface;
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.NotificationAdapter;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.Constants;
import parimi.com.umentor.helper.NotificationType;
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.models.Notification;
import parimi.com.umentor.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment implements ButtonClickInterface{

    @Inject
    DatabaseHelper databaseHelper;

    @BindView(R.id.notifications_list_view)
    ListView notificationListView;

    User currentUser;
    List<Notification> notifications = new ArrayList<>();
    NotificationAdapter notificationAdapter;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UMentorDaggerInjector.get().inject(this);
        currentUser = SharedPreferenceHelper.getCurrentUser(getActivity());
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, view);
        databaseHelper.getNotifications().orderByChild("receiver").equalTo(currentUser.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                for(DataSnapshot notification: dataSnapshot.getChildren()) {

                    Notification notificationInstance = new Notification(notification.child("id").getValue().toString(),
                            notification.child("sender").getValue().toString(),
                            notification.child("receiver").getValue().toString(),
                            NotificationType.getByType(notification.child("notificationType").getValue().toString()),
                            notification.child("message").getValue().toString(),
                            notification.child("title").getValue().toString()
                    );
                    notifications.add(notificationInstance);
                }
                notificationAdapter = new NotificationAdapter(getActivity(), notifications, NotificationsFragment.this);
                notificationListView.setAdapter(notificationAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onItemSelected(String name) {

    }

    @Override
    public void onRequestAccepted(Notification notification, String acceptOrReject) {
        if(acceptOrReject.equals(Constants.ACCEPT)) {

            //remove the notification as we no longer add it.
            databaseHelper.getNotifications().child(notification.getId()).removeValue();

            //update the adapter
            notifications.remove(notification);
            notificationAdapter.setNotificationsList(notifications);

            //add the user as a mentor
            databaseHelper.addMentorToUser(notification.getSender(), currentUser.getId());
        }


    }
}
