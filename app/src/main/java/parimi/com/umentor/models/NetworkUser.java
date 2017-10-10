package parimi.com.umentor.models;

/**
 * Created by nandpa on 10/9/17.
 */

public class NetworkUser {
    private boolean mentor;
    private float rating;
    private String userId;
    private boolean ratingGiven;

    public boolean isMentor() {
        return mentor;
    }

    public void setMentor(boolean mentor) {
        this.mentor = mentor;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRatingGiven() {
        return ratingGiven;
    }

    public void setRatingGiven(boolean ratingGiven) {
        this.ratingGiven = ratingGiven;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof NetworkUser)) {
            return false;
        }

        NetworkUser user = (NetworkUser) obj;

        return user.getUserId().equals(userId);
    }
}
