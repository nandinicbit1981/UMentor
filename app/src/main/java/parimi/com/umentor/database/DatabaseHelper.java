package parimi.com.umentor.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import parimi.com.umentor.helper.NotificationType;
import parimi.com.umentor.models.Message;
import parimi.com.umentor.models.Notification;
import parimi.com.umentor.models.Requests;
import parimi.com.umentor.models.User;

/**
 * Created by nandpa on 8/27/17.
 */

public class DatabaseHelper {

    private DatabaseReference mDatabase;

    @Inject
    public DatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance().getReference("umentor-d21ff");
    }

    public void saveUser(User user) {
        mDatabase.child("users").child(user.getId()).setValue(user);
    }

    public void saveUserToCategories(String category,String user) {
        mDatabase.child("selectedCategories").child(category).child(user).setValue(true);
    }

    public DatabaseReference getCategories() {
       return mDatabase.child("categories");
    }

    public DatabaseReference getUsers() {
        return mDatabase.child("users");
    }

    public DatabaseReference getSelectedCategories() {
        return mDatabase.child("selectedCategories");
    }

    public DatabaseReference getRequests() {
        return mDatabase.child("requests");
    }

    public void saveRequest(Requests requests) {

        // save the request to the requests
        DatabaseReference requests1 = mDatabase.child("requests").push();
        requests1.setValue(requests);

        // create an entry in the notifications
        DatabaseReference notificationsRequest = mDatabase.child("notifications").push();
        Notification notification = new Notification(
                notificationsRequest.getKey(),
                requests.getSender(),
                requests.getReceiver(),
                NotificationType.REQUEST,
                "",
                requests.getSenderName() + " would like to add you as a mentor");
        notificationsRequest.setValue(notification);

    }

    public void updateRequest(Requests requests) {
        mDatabase.child("requests").child(requests.getId()).setValue(requests);
    }

    public DatabaseReference getNotifications() {
        return mDatabase.child("notifications");
    }

    public void addMentorToUser(String userId,String mentorId) {
        mDatabase.child("mentors").child(userId).child(mentorId).setValue(true);
    }

    public DatabaseReference getMentors() {
        return mDatabase.child("mentors");
    }

    public void saveUserChatChannels(Message message, String channelId) {
        DatabaseReference reference= mDatabase.child("user-chat-channels").child(message.getSenderId()).child(message.getReceiverId()).push();
        reference.child("channel").setValue(channelId);

        reference= mDatabase.child("user-chat-channels").child(message.getReceiverId()).child(message.getSenderId()).push();
        reference.child("channel").setValue(channelId);
    }

    public DatabaseReference getUserChatChannels() {
        return mDatabase.child("user-chat-channels");
    }

    public DatabaseReference getChannels() {
        return mDatabase.child("channels");
    }

    public void saveChatToChannel(String channelId, Message message){
        mDatabase.child("channels").child(channelId).push().setValue(message);
    }
}
