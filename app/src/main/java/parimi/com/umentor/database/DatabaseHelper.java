package parimi.com.umentor.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import parimi.com.umentor.models.Requests;
import parimi.com.umentor.models.User;

/**
 * Created by nandpa on 8/27/17.
 */

public class DatabaseHelper {

    private DatabaseReference mDatabase;

    @Inject
    public DatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance().getReference("umentor-d21ff");
    }

    public void saveUser(User user) {
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
        mDatabase.child("requests").setValue(requests);
    }
}
