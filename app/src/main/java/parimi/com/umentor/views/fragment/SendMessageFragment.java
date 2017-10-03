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
import parimi.com.umentor.helper.UMentorHelper;
import parimi.com.umentor.models.Message;
import parimi.com.umentor.models.Notification;
import parimi.com.umentor.models.User;
import parimi.com.umentor.rest.RestInterface;

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
            mentor = (User) bundle.get("user");
        }
        MessageAdapter messageAdapter = new MessageAdapter(getActivity());
        messagesListView.setAdapter(messageAdapter);
        getChannel();

        return view;
    }

    private void getChannel() {
        databaseHelper.getUserChatChannels().child(currentUser.getId()).child(mentor.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    getMessages(ds.child("channel").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMessages(String channel) {
        databaseHelper.getChannels().child(channel).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message message = new Message();
                    message.setReceiverId(ds.child("receiverId").getValue().toString());
                    message.setSenderId(ds.child("senderId").getValue().toString());
                    message.setSenderName(ds.child("senderName").getValue().toString());
                    message.setReceiverName(ds.child("receiverName").getValue().toString());
                    message.setMessage(ds.child("message").getValue().toString());
                    message.setTimeStamp(Long.valueOf(ds.child("timeStamp").getValue().toString()));
                    if(!messageList.contains(message)) {
                        messageList.add(message);
                    }
                    if(messageAdapter == null) {
                        messageAdapter = new MessageAdapter(getActivity());
                    }
                    messageAdapter.setMessageLlist(messageList);
                    messagesListView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.message_send_button)
    public void sendMessage() {
        UMentorHelper.hideKeyboard(this.getContext(), getView());

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
                        databaseHelper.saveChatToChannel(ds.child("channel").getValue().toString(), message);
                    }
                }
                Notification notification = new Notification(currentUser.getId(), mentor.getId(), NotificationType.MESSAGE, currentUser.getName() + " sent you a new message", "New Message", mentor.getFcmToken(), new Date().getTime());
                databaseHelper.saveNotification(notification);
                RestInterface.sendNotification(getContext(), mentor.getFcmToken(), "New Message", currentUser.getName() + " sent you a new message");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
