package parimi.com.umentor.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nandpa on 8/26/17.
 */

public class User implements Parcelable, Serializable {
    private String name;
    private String id;
    private String email;
    private String gender;
    private int age;
    private String summary;
    private int experience;
    private String fcmToken;
    private float rating;
    private String job;
    private List<String> categories;

    public User(Parcel in) {
        name = in.readString();
        id = in.readString();
        email = in.readString();
        gender = in.readString();
        age = in.readInt();
        summary = in.readString();
        experience = in.readInt();
        fcmToken = in.readString();
        rating = in.readFloat();
        job = in.readString();
        categories = in.readArrayList(User.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {

    }

    public User(String displayName,
                String uid, String email,
                String gender, int age, String summary,
                int experience, String fcmToken,
                float rating,
                List<String> categories,
                String job
    ) {
        this.name = displayName;
        this.id = uid;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.summary = summary;
        this.experience = experience;
        this.fcmToken = fcmToken;
        this.rating = rating;
        this.job = job;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("email", email);
        result.put("gender", gender);
        result.put("summary", summary);
        result.put("experience", experience);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(email);
        parcel.writeString(gender);
        parcel.writeInt(age);
        parcel.writeString(summary);
        parcel.writeInt(experience);
        parcel.writeString(fcmToken);
        parcel.writeFloat(rating);
        parcel.writeString(job);
        parcel.writeList(categories);

    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;

        return user.getId().equals(name);
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
