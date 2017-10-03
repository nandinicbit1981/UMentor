package parimi.com.umentor.rest;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by nandpa on 9/30/17.
 */

public class RestInterface {

    private static final String fcmURL = "https://fcm.googleapis.com/fcm/send";
    public static void sendNotification(Context context, String registrationID, String subject, String message) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "key=AIzaSyD-EQLKRvqLmnRaMFfBtT0RIa6mYZg5xV4");
        client.addHeader("Content-type","application/json");
        String notificationMessage = prepareNotificationPayloadForAndroid(registrationID, subject, message);
        StringEntity entity = null;
        try {
            entity = new StringEntity(notificationMessage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        client.post(context, fcmURL, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });
    }

    private static String prepareNotificationPayloadForAndroid(String registrationID, String subject, String message) {

        String payload = "{\"to\":\""
                + registrationID
                + "\",\"notification\":{\"title\":\""
                + subject
                + "\",\"body\":\""
                + message
                + "\"}}";
        return payload;
    }
}
