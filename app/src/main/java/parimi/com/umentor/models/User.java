package parimi.com.umentor.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
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
    private HashMap<String, String> category;
    private String expertise;
    private int experience;

    public User(Parcel in) {
        name = in.readString();
        id = in.readString();
        email = in.readString();
        gender = in.readString();
        age = in.readInt();
        expertise = in.readString();
        experience = in.readInt();
        in.readMap(this.getCategory(), Category.class.getClassLoader());
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
                String gender, int age, String expertise,
                int experience, HashMap<String, String> category) {
        this.name = displayName;
        this.id = uid;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.expertise = expertise;
        this.experience = experience;
        this.category = category;
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

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
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
        result.put("expertise", expertise);
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
        parcel.writeString(expertise);
        parcel.writeInt(experience);
        parcel.writeMap(category);

    }

    public HashMap<String, String> getCategory() {
        return category;
    }

    public void setCategory(HashMap<String, String> category) {
        this.category = category;
    }
}
