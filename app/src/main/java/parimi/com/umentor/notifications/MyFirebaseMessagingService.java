package parimi.com.umentor.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import parimi.com.umentor.R;
import parimi.com.umentor.application.UMentorDaggerInjector;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.widget.UpdateWidgetService;

import static parimi.com.umentor.helper.Constants.MENTORREQUESTACCEPTED;
import static parimi.com.umentor.helper.Constants.RATINGGIVEN;
import static parimi.com.umentor.helper.Constants.UPDATEWIDGETTYPE;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getCanonicalName();

    @Inject
    UpdateWidgetService service;

    @Inject
    DatabaseHelper databaseHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

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
        Intent intent = new Intent(this, UpdateWidgetService.class);

        if (remoteMessage.getNotification().getTitle().toString().equals(MENTORREQUESTACCEPTED)) {
            intent.putExtra(UPDATEWIDGETTYPE, MENTORREQUESTACCEPTED);
        } else {
            intent.putExtra(UPDATEWIDGETTYPE, RATINGGIVEN);
        }

        startService(intent);

    }
}