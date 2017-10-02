package parimi.com.umentor.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import parimi.com.umentor.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Notification Message Body: ");
        Log.d(TAG, "title "+ remoteMessage.getData());


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


    }
}