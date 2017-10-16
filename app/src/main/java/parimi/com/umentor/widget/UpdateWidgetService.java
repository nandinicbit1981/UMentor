package parimi.com.umentor.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.models.User;

import static parimi.com.umentor.helper.Constants.MENTORREQUESTACCEPTED;
import static parimi.com.umentor.helper.Constants.RATINGGIVEN;

/**
 * Created by nandpa on 10/15/17.
 */

public class UpdateWidgetService extends Service {

    private static User currentUser;
    private long mentee;
    private long mentor;
    private Context context;
    @Inject
    DatabaseHelper databaseHelper;

    @Inject
    public UpdateWidgetService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void updateWidget(final Context context, String updateWidgetType) {
        UMentorDaggerInjector.get().inject(this);
        currentUser = SharedPreferenceHelper.getCurrentUser(context);
        if(updateWidgetType.equals(MENTORREQUESTACCEPTED)) {
            databaseHelper.getNetwork().child(currentUser.getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mentee = 0;
                    mentor = 0;
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.child("mentee").getValue().equals(true)) {
                            mentee++;
                        }
                        if(ds.child("mentor").getValue().equals(true)) {
                            mentor++;
                        }
                    }

                    //updating widget
                    Intent i = new Intent(context, HomeWidgetProvider.class);
                    i.setAction(HomeWidgetProvider.UPDATE_ACTION);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("mentee", mentee);
                    i.putExtra("mentor", mentor);
                    context.sendBroadcast(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if(updateWidgetType.equals(RATINGGIVEN)){
            databaseHelper.getUsers().child(currentUser.getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Intent i = new Intent(context, HomeWidgetProvider.class);
                    i.setAction(HomeWidgetProvider.UPDATE_ACTION);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("rating", dataSnapshot.child("rating").getValue().toString());
                    context.sendBroadcast(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


}
