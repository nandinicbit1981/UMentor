package parimi.com.umentor.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import javax.inject.Inject;

import parimi.com.umentor.helper.NotificationType;
import parimi.com.umentor.models.Message;
import parimi.com.umentor.models.Notification;
import parimi.com.umentor.models.Requests;
import parimi.com.umentor.models.User;

import static parimi.com.umentor.helper.Constants.ADDASMENTOR;
import static parimi.com.umentor.helper.Constants.CATEGORIES;
import static parimi.com.umentor.helper.Constants.CHANNEL;
import static parimi.com.umentor.helper.Constants.CHANNELS;
import static parimi.com.umentor.helper.Constants.FIREBASEDATABASE;
import static parimi.com.umentor.helper.Constants.MENTEE;
import static parimi.com.umentor.helper.Constants.MENTOR;
import static parimi.com.umentor.helper.Constants.NETWORK;
import static parimi.com.umentor.helper.Constants.NOTIFICATIONS;
import static parimi.com.umentor.helper.Constants.RATING;
import static parimi.com.umentor.helper.Constants.REQUESTS;
import static parimi.com.umentor.helper.Constants.SELECTEDCATEGORIES;
import static parimi.com.umentor.helper.Constants.SETRATINGGIVEN;
import static parimi.com.umentor.helper.Constants.USERCHATCHANNELS;
import static parimi.com.umentor.helper.Constants.USERS;

/**
 * Created by nandpa on 8/27/17.
 */

public class DatabaseHelper {

    private DatabaseReference mDatabase;

    @Inject
    public DatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance().getReference(FIREBASEDATABASE);
    }

    public void saveUser(User user) {
        mDatabase.child(USERS).child(user.getId()).setValue(user);
    }

    public void updateUser(User user) {
        mDatabase.child(USERS).child(user.getId()).setValue(user);
    }

    public void saveUserToCategories(String category,String user) {
        mDatabase.child(SELECTEDCATEGORIES).child(category).child(user).setValue(true);
    }

    public DatabaseReference getCategories() {
       return mDatabase.child(CATEGORIES);
    }

    public DatabaseReference getUsers() {
        return mDatabase.child(USERS);
    }

    public DatabaseReference getSelectedCategories() {
        return mDatabase.child(SELECTEDCATEGORIES);
    }

    public DatabaseReference getRequests() {
        return mDatabase.child(REQUESTS);
    }

    public void saveRequest(Requests requests) {

        // save the request to the requests
        DatabaseReference requests1 = mDatabase.child(REQUESTS).push();
        requests1.setValue(requests);

        // create an entry in the notifications
        DatabaseReference notificationsRequest = mDatabase.child(NOTIFICATIONS).push();
        Notification notification = new Notification(
                notificationsRequest.getKey(),
                requests.getSender(),
                requests.getReceiver(),
                NotificationType.REQUEST,
                "",
                requests.getSenderName() + ADDASMENTOR,
                requests.getSenderFcmToken(),
                new Date().getTime()
                );
        notificationsRequest.setValue(notification);

    }

    public void updateRequest(Requests requests) {
        mDatabase.child(REQUESTS).child(requests.getId()).setValue(requests);
    }

    public DatabaseReference getNotifications() {
        return mDatabase.child(NOTIFICATIONS);
    }

    public void addUserToNetwork(String userId, String mentorId) {
        addMentorToUser(userId, mentorId, 0, false);
        addMenteeToMentor(userId, mentorId, 0, false);
    }

    public void addMentorToUser(String userId,String mentorId, float rating, boolean setRatingGiven) {
        getNetwork().child(userId).child(mentorId).child(MENTOR).setValue(true);
        getNetwork().child(userId).child(mentorId).child(MENTEE).setValue(false);
        getNetwork().child(userId).child(mentorId).child(RATING).setValue(rating);
        getNetwork().child(userId).child(mentorId).child(SETRATINGGIVEN).setValue(setRatingGiven);
    }

    public void addMenteeToMentor(String userId, String mentorId, float rating, boolean setRatingGiven) {
        getNetwork().child(mentorId).child(userId).child(MENTOR).setValue(false);
        getNetwork().child(mentorId).child(userId).child(MENTEE).setValue(true);
        getNetwork().child(mentorId).child(userId).child(RATING).setValue(rating);
        getNetwork().child(mentorId).child(userId).child(SETRATINGGIVEN).setValue(setRatingGiven);
    }

    public DatabaseReference getNetwork() {
        mDatabase.child(NETWORK).keepSynced(true);
        return mDatabase.child(NETWORK);
    }

    public void saveUserChatChannels(Message message, String channelId) {
        DatabaseReference reference= mDatabase.child(USERCHATCHANNELS).child(message.getSenderId()).child(message.getReceiverId()).push();
        reference.child(CHANNEL).setValue(channelId);

        reference= mDatabase.child(USERCHATCHANNELS).child(message.getReceiverId()).child(message.getSenderId()).push();
        reference.child(CHANNEL).setValue(channelId);
    }

    public DatabaseReference getUserChatChannels() {
        return mDatabase.child(USERCHATCHANNELS);
    }

    public DatabaseReference getChannels() {
        return mDatabase.child(CHANNELS);
    }

    public void saveChatToChannel(String channelId, Message message){
        mDatabase.child(CHANNELS).child(channelId).push().setValue(message);
    }

    public void saveNotification(Notification notification) {
        DatabaseReference notificationsRequest = mDatabase.child(NOTIFICATIONS).push();
        notification.setId(notificationsRequest.getKey());
        notificationsRequest.setValue(notification);
    }

}
