package parimi.com.umentor.rest;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import parimi.com.umentor.database.DatabaseHelper;
import parimi.com.umentor.helper.Constants;

import static parimi.com.umentor.helper.Constants.fcmURL;

/**
 * Created by nandpa on 9/30/17.
 */

public class RestInterface {

    @Inject
    DatabaseHelper databaseHelper;

    public static void sendNotification(Context context, String registrationID, String subject, String message) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "key="+ Constants.API_KEY);
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
                System.out.println("started");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("failed");
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
