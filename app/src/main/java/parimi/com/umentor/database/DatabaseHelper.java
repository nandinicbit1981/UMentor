package parimi.com.umentor.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

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
    private FirebaseStorage mstorage;

    @Inject
    public DatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance().getReference("umentor-d21ff");
        mstorage = FirebaseStorage.getInstance("gs://umentor-d21ff.appspot.com");
    }

    public void saveUser(User user) {
        mDatabase.child("users").child(user.getId()).setValue(user);
    }

    public void updateUser(User user) {
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
                requests.getSenderName() + " would like to add you as a mentor",
                requests.getSenderFcmToken(),
                new Date().getTime()
                );
        notificationsRequest.setValue(notification);

    }

    public void updateRequest(Requests requests) {
        mDatabase.child("requests").child(requests.getId()).setValue(requests);
    }

    public DatabaseReference getNotifications() {
        return mDatabase.child("notifications");
    }

    public void addUserToNetwork(String userId, String mentorId) {
        addMentorToUser(userId, mentorId, 0, false);
        addMenteeToMentor(userId, mentorId, 0, false);
    }

    public void addMentorToUser(String userId,String mentorId, float rating, boolean setRatingGiven) {
        getNetwork().child(userId).child(mentorId).child("mentor").setValue(true);
        getNetwork().child(userId).child(mentorId).child("mentee").setValue(false);
        getNetwork().child(userId).child(mentorId).child("rating").setValue(rating);
        getNetwork().child(userId).child(mentorId).child("setRatingGiven").setValue(setRatingGiven);
    }

    public void addMenteeToMentor(String userId, String mentorId, float rating, boolean setRatingGiven) {
        getNetwork().child(mentorId).child(userId).child("mentor").setValue(false);
        getNetwork().child(mentorId).child(userId).child("mentee").setValue(true);
        getNetwork().child(mentorId).child(userId).child("rating").setValue(rating);
        getNetwork().child(mentorId).child(userId).child("setRatingGiven").setValue(setRatingGiven);
    }

    public DatabaseReference getNetwork() {
        mDatabase.child("network").keepSynced(true);
        return mDatabase.child("network");
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

    public void saveNotification(Notification notification) {
        DatabaseReference notificationsRequest = mDatabase.child("notifications").push();
        notification.setId(notificationsRequest.getKey());
        notificationsRequest.setValue(notification);
    }

    public StorageReference getStorageRef() {
        return mstorage.getReference();
    }

    public StorageReference getImagesRef() {
        return getStorageRef().child("images");
    }

    public DatabaseReference getImageDatabaseRef() {
         return   mDatabase.child("images");
    }
}
