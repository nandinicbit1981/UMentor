package parimi.com.umentor.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import parimi.com.umentor.models.User;

import static parimi.com.umentor.helper.Constants.UMENTOR;

/**
 * Created by nandpa on 9/24/17.
 */

public class SharedPreferenceHelper {

    public static void saveString(Context context, String itemName, String itemValue) {
        SharedPreferences preferences = context.getSharedPreferences(UMENTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(itemName, new Gson().toJson(itemValue));
        editor.commit();
    }

    public static String getString(Context context, String itemName) {
        SharedPreferences preferences = context.getSharedPreferences(UMENTOR, Context.MODE_PRIVATE);
        return preferences.getString(itemName, null);
    }

    public static void saveUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(UMENTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER, new Gson().toJson(user));
        editor.commit();
    }
    public static User getCurrentUser(Context context) {
        return new Gson().fromJson(getString(context, Constants.USER), User.class);
    }
}
