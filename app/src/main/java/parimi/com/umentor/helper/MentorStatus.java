package parimi.com.umentor.helper;

/**
 * Created by nandpa on 9/24/17.
 */

public enum MentorStatus {
    NOT_MENTOR("NOT_MENTOR"),
    REQUEST_MENTOR("REQUEST_MENTOR"),
    MENTOR("MENTOR");

    private  String mentorStatus;

    MentorStatus(String mentor) {
        this.mentorStatus = mentor;
    }

    public String getMentorStatus(){
        return this.mentorStatus;
    }

    public static MentorStatus getByStatus(String status) {
        for (MentorStatus mStatus : values()) {
            if (mStatus.getMentorStatus() == status) {
                return mStatus;
            }
        }
        return null;
    }
}
