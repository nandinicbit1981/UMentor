package parimi.com.umentor.views.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parimi.com.umentor.R;
import parimi.com.umentor.adapters.MessageAdapter;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.NotificationType;
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.models.Message;
import parimi.com.umentor.models.Notification;
import parimi.com.umentor.models.User;
import parimi.com.umentor.rest.RestInterface;

import static parimi.com.umentor.helper.Constants.CHANNEL;
import static parimi.com.umentor.helper.Constants.MESSAGE;
import static parimi.com.umentor.helper.Constants.NEWMESSAGE;
import static parimi.com.umentor.helper.Constants.RECEIVERID;
import static parimi.com.umentor.helper.Constants.RECEIVERNAME;
import static parimi.com.umentor.helper.Constants.SENDERID;
import static parimi.com.umentor.helper.Constants.SENDERNAME;
import static parimi.com.umentor.helper.Constants.SENTNEWMESSAGE;
import static parimi.com.umentor.helper.Constants.TIMESTAMP;
import static parimi.com.umentor.helper.Constants.USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendMessageFragment extends Fragment {

    @Inject
    DatabaseHelper databaseHelper;

    User currentUser;

    @BindView(R.id.message_send_button)
    Button messageButton;

    @BindView(R.id.message_edit_text)
    EditText messageEditText;

    @BindView(R.id.message_list_view)
    ListView messagesListView;

    List<Message> messageList = new ArrayList<>();
    MessageAdapter messageAdapter;
    boolean initial = false;

    User mentor = null;

    public SendMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        UMentorDaggerInjector.get().inject(this);
        currentUser = SharedPreferenceHelper.getCurrentUser(getActivity());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mentor = (User) bundle.get(USER);
        }
        messageAdapter = new MessageAdapter(getActivity(), messageList);
        messageAdapter.setCurrentUserId(currentUser.getId());
        messagesListView.setAdapter(messageAdapter);
        getChannel();

        return view;
    }

    private void getChannel() {
        databaseHelper.getUserChatChannels().child(currentUser.getId()).child(mentor.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    getMessages(ds.child(CHANNEL).getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMessages(String channel) {
        databaseHelper.getChannels().child(channel).orderByChild(TIMESTAMP).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(messageAdapter == null) {
                    messageAdapter = new MessageAdapter(getActivity(), messageList);
                    messageAdapter.setCurrentUserId(currentUser.getId());
                    messagesListView.setAdapter(messageAdapter);
                }
                if(messageList.size() == 0) {
                    initial = true;
                } else {
                    initial = false;
                }

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message message = new Message();
                    message.setReceiverId(ds.child(RECEIVERID).getValue().toString());
                    message.setSenderId(ds.child(SENDERID).getValue().toString());
                    message.setSenderName(ds.child(SENDERNAME).getValue().toString());
                    message.setReceiverName(ds.child(RECEIVERNAME).getValue().toString());
                    message.setMessage(ds.child(MESSAGE).getValue().toString());
                    message.setTimeStamp(Long.valueOf(ds.child(TIMESTAMP).getValue().toString()));
                    if(!messageList.contains(message)) {
                        messageAdapter.add(message);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getChannel();
    }

    @OnClick(R.id.message_send_button)
    public void sendMessage() {
        final Message message = new Message();
        message.setMessage(messageEditText.getText().toString());
        message.setSenderName(currentUser.getName());
        message.setReceiverName(mentor.getName());
        message.setSenderId(currentUser.getId());
        message.setReceiverId(mentor.getId());
        message.setTimeStamp((new Date()).getTime());
        messageEditText.getText().clear();
        databaseHelper.getUserChatChannels().child(message.getSenderId()).child(message.getReceiverId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    DatabaseReference channelRef = databaseHelper.getChannels().push();
                    channelRef.push().setValue(message);
                    databaseHelper.saveUserChatChannels(message, channelRef.getKey());
                } else {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        databaseHelper.saveChatToChannel(ds.child(CHANNEL).getValue().toString(), message);
                    }
                }
                Notification notification = new Notification(currentUser.getId(), mentor.getId(), NotificationType.MESSAGE, currentUser.getName() + SENTNEWMESSAGE, NEWMESSAGE, mentor.getFcmToken(), new Date().getTime());
                databaseHelper.saveNotification(notification);
                RestInterface.sendNotification(getContext(), mentor.getFcmToken(), NEWMESSAGE, currentUser.getName() + SENTNEWMESSAGE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
