package parimi.com.umentor.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import parimi.com.umentor.R;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.SharedPreferenceHelper;
import parimi.com.umentor.models.User;
import parimi.com.umentor.widget.HomeWidgetProvider;
import parimi.com.umentor.widget.UpdateWidgetService;

import static parimi.com.umentor.helper.Constants.MENTORREQUESTACCEPTED;
import static parimi.com.umentor.helper.Constants.RATINGGIVEN;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";

    @Inject
    UpdateWidgetService service;

    @Inject
    DatabaseHelper databaseHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Notification Message Body: ");
        Log.d(TAG, "title "+ remoteMessage.getData());
        UMentorDaggerInjector.get().inject(this);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setColor(this.getResources().getColor(android.R.color.transparent))
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        int code = 1000;
        notificationManager.notify(code, notificationBuilder.build());

        if (remoteMessage.getNotification().getTitle().toString().equals(MENTORREQUESTACCEPTED)) {
            service.updateWidget(this.getBaseContext(), MENTORREQUESTACCEPTED);
        } else {
            service.updateWidget(this.getBaseContext(), RATINGGIVEN);
        }
    }
}