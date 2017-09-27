package parimi.com.umentor.models;

import parimi.com.umentor.helper.NotificationType;

/**
 * Created by nandpa on 9/27/17.
 */

public class Notification {

    private String id;
    private String sender;
    private String receiver;
    private String title;
    private NotificationType notificationType;
    private String message;

    public Notification(String id,
                        String sender,
                        String receiver,
                        NotificationType notificationType,
                        String message,
                        String title) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.message = message;
        this.title = title;
    }

    public Notification( String sender,
                         String receiver,
                         NotificationType notificationType,
                         String message,
                         String title) {

        this.sender = sender;
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.message = message;
        this.title = title;
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
}
