package parimi.com.umentor.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.models.User;

import static parimi.com.umentor.helper.Constants.MENTEE;
import static parimi.com.umentor.helper.Constants.MENTOR;
import static parimi.com.umentor.helper.Constants.MENTORREQUESTACCEPTED;
import static parimi.com.umentor.helper.Constants.RATING;
import static parimi.com.umentor.helper.Constants.RATINGGIVEN;
import static parimi.com.umentor.helper.Constants.UPDATEWIDGETTYPE;

/**
 * Created by nandpa on 10/15/17.
 */

public class UpdateWidgetService extends IntentService {

    private static User currentUser;
    private long mentee;
    private long mentor;
    private Context context;
    @Inject
    DatabaseHelper databaseHelper;

    @Inject
    public UpdateWidgetService() {
        super(UpdateWidgetService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        UMentorDaggerInjector.get().inject(this);
        context = this;
        String updateWidgetType = intent.getExtras().getSerializable(UPDATEWIDGETTYPE).toString();
        currentUser = SharedPreferenceHelper.getCurrentUser(context);
        if(updateWidgetType.equals(MENTORREQUESTACCEPTED)) {
            databaseHelper.getNetwork().child(currentUser.getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mentee = 0;
                    mentor = 0;
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        if((ds.child(MENTEE).getValue() != null) && (ds.child(MENTEE).getValue().equals(true))) {
                            mentee++;
                        }
                        if((ds.child(MENTOR).getValue() != null) && (ds.child(MENTOR).getValue().equals(true))) {
                            mentor++;
                        }
                    }

                    //updating widget
                    Intent i = new Intent(context, HomeWidgetProvider.class);
                    i.setAction(HomeWidgetProvider.UPDATE_ACTION);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(MENTEE, mentee);
                    i.putExtra(MENTOR, mentor);
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
                    i.putExtra(RATING, dataSnapshot.child(RATING).getValue().toString());
                    context.sendBroadcast(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
