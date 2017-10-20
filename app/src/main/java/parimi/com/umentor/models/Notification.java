package parimi.com.umentor.models;

import android.support.annotation.NonNull;

import parimi.com.umentor.helper.NotificationType;

/**
 * Created by nandpa on 9/27/17.
 */

public class Notification implements Comparable<Notification>{

    private String id;
    private String sender;
    private String receiver;
    private String title;
    private String senderFcmToken;
    private NotificationType notificationType;
    private String message;
    private long timeStamp;

    public Notification(String id,
                        String sender,
                        String receiver,
                        NotificationType notificationType,
                        String message,
                        String title,
                        String senderFcmToken,
                        long timeStamp
                        ) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.message = message;
        this.title = title;
        this.senderFcmToken = senderFcmToken;
        this.timeStamp = timeStamp;
    }

    public Notification( String sender,
                         String receiver,
                         NotificationType notificationType,
                         String message,
                         String title,
                         String fcmToken,
                         long timeStamp
                        ) {

        this.sender = sender;
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.message = message;
        this.title = title;
        this.senderFcmToken = fcmToken;
        this.timeStamp = timeStamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSenderFcmToken() {
        return senderFcmToken;
    }

    public void setSenderFcmToken(String senderFcmToken) {
        this.senderFcmToken = senderFcmToken;
    }

    @Override
    public boolean equals(Object obj) {
        return this.timeStamp == ((Notification) obj).getTimeStamp();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Override
    public int compareTo(@NonNull Notification notification) {
        if(this.timeStamp > notification.timeStamp) return 1;
        if(this.timeStamp < notification.timeStamp) return -1;
        return 0;
    }
}
