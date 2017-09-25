package parimi.com.umentor.models;

import parimi.com.umentor.helper.MentorStatus;

/**
 * Created by nandpa on 9/24/17.
 */

public class Requests {
    private String sender;
    private String receiver;
    private MentorStatus status;

    public Requests(String sender, String receiver, MentorStatus mentorStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = mentorStatus;
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
}
