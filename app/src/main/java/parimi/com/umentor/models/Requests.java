package parimi.com.umentor.models;

import parimi.com.umentor.helper.MentorStatus;

/**
 * Created by nandpa on 9/24/17.
 */

public class Requests {
    private String id;
    private String sender;
    private String receiver;
    private MentorStatus status;
    private String senderFcmToken;
    private String senderName;

    public Requests(String id,
                    String sender,
                    String receiver,
                    MentorStatus mentorStatus,
                    String senderName,
                    String fcmToken
                    ) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.status = mentorStatus;
        this.senderName = senderName;
        this.senderFcmToken = fcmToken;
    }


    public MentorStatus getStatus() {
        return status;
    }

    public void setStatus(MentorStatus status) {
        this.status = status;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderFcmToken() {
        return senderFcmToken;
    }

    public void setSenderFcmToken(String senderFcmToken) {
        this.senderFcmToken = senderFcmToken;
    }
}
