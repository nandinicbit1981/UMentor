package parimi.com.umentor.views.fragment;


import android.content.Intent;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import parimi.com.umentor.ButtonClickInterface;
import parimi.com.umentor.NotificationsClickInterface;
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.NotificationAdapter;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.Constants;
import parimi.com.umentor.helper.NotificationType;
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.models.Notification;
import parimi.com.umentor.models.User;
import parimi.com.umentor.rest.RestInterface;
import parimi.com.umentor.views.activity.MainActivity;
import parimi.com.umentor.widget.UpdateWidgetService;

import static parimi.com.umentor.helper.Constants.AGE;
import static parimi.com.umentor.helper.Constants.CATEGORIES;
import static parimi.com.umentor.helper.Constants.EMAIL;
import static parimi.com.umentor.helper.Constants.EXPERIENCE;
import static parimi.com.umentor.helper.Constants.FCMTOKEN;
import static parimi.com.umentor.helper.Constants.GENDER;
import static parimi.com.umentor.helper.Constants.ID;
import static parimi.com.umentor.helper.Constants.JOB;
import static parimi.com.umentor.helper.Constants.MENTORREQUESTACCEPTED;
import static parimi.com.umentor.helper.Constants.MESSAGE;
import static parimi.com.umentor.helper.Constants.NAME;
import static parimi.com.umentor.helper.Constants.NOTIFICATIONTYPE;
import static parimi.com.umentor.helper.Constants.PROFILEPIC;
import static parimi.com.umentor.helper.Constants.RATING;
import static parimi.com.umentor.helper.Constants.RECEIVER;
import static parimi.com.umentor.helper.Constants.SENDER;
import static parimi.com.umentor.helper.Constants.SENDERFCMTOKEN;
import static parimi.com.umentor.helper.Constants.SUMMARY;
import static parimi.com.umentor.helper.Constants.TIMESTAMP;
import static parimi.com.umentor.helper.Constants.TITLE;
import static parimi.com.umentor.helper.Constants.UPDATEWIDGETTYPE;
import static parimi.com.umentor.helper.Constants.USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment implements ButtonClickInterface, NotificationsClickInterface {

    @Inject
    DatabaseHelper databaseHelper;

    @BindView(R.id.notifications_list_view)
    ListView notificationListView;

    @Inject
    UpdateWidgetService service;

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
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, view);
        databaseHelper.getNotifications().orderByChild(RECEIVER).equalTo(currentUser.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot notification : dataSnapshot.getChildren()) {

                    Notification notificationInstance = new Notification(notification.child(ID).getValue().toString(),
                            notification.child(SENDER).getValue().toString(),
                            notification.child(RECEIVER).getValue().toString(),
                            NotificationType.getByType(notification.child(NOTIFICATIONTYPE).getValue().toString()),
                            notification.child(MESSAGE).getValue().toString(),
                            notification.child(TITLE).getValue().toString(),
                            notification.child(SENDERFCMTOKEN).getValue().toString(),
                            Long.valueOf(notification.child(TIMESTAMP).getValue().toString())
                    );
                    if (!notifications.contains(notificationInstance)) {
                        notifications.add(notificationInstance);
                    }
                }
                Comparator compare = Collections.reverseOrder();
                Collections.sort(notifications, compare);
                notificationAdapter = new NotificationAdapter(getActivity(), notifications, NotificationsFragment.this, NotificationsFragment.this);
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
        if (acceptOrReject.equals(Constants.ACCEPT)) {

            //remove the notification as we no longer add it.
            databaseHelper.getNotifications().child(notification.getId()).removeValue();

            //update the adapter
            notifications.remove(notification);
            notificationAdapter.setNotificationsList(notifications);

            //add the user as a mentor
            databaseHelper.addUserToNetwork(notification.getSender(), currentUser.getId());

            Notification acceptedNotification = new Notification(notification.getReceiver(),
                    notification.getSender(),
                    NotificationType.ACCEPT,
                    currentUser.getName() + getString(R.string.accept_mentor_request),
                    MENTORREQUESTACCEPTED, notification.getSenderFcmToken(),
                    new Date().getTime());
            RestInterface.sendNotification(getContext(), notification.getSenderFcmToken(), MENTORREQUESTACCEPTED, currentUser.getName() + getString(R.string.accept_mentor_request));
            databaseHelper.saveNotification(acceptedNotification);
            Intent intent = new Intent(getActivity(), UpdateWidgetService.class);
            intent.putExtra(UPDATEWIDGETTYPE, MENTORREQUESTACCEPTED);
            getActivity().startService(intent);
        }
    }


    @Override
    public void onItemClicked(String id, final NotificationType notificationType) {

        databaseHelper.getUsers().child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
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
                    Fragment fragment = null;
                    if(notificationType.equals(NotificationType.REQUEST)) {
                        fragment = new ProfileFragment();
                    } else if(notificationType.equals(NotificationType.MESSAGE)) {
                        fragment = new SendMessageFragment();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(USER, user);
                    fragment.setArguments(bundle);
                    ((MainActivity) getActivity()).insertFragment(fragment);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
