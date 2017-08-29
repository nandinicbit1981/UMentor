package parimi.com.umentor.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import parimi.com.umentor.models.User;

/**
 * Created by nandpa on 8/27/17.
 */

public class DatabaseHelper {

    private DatabaseReference mDatabase;


    public DatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance().getReference("umentor-d21ff");
    }

    public void saveUser(User user) {
        mDatabase.child("users").child(user.getId()).setValue(user);
    }
}
