package parimi.com.umentor;

import parimi.com.umentor.models.Notification;

/**
 * Created by nandpa on 9/21/17.
 */

public interface ButtonClickInterface {
    void onItemSelected(String name);
    void onRequestAccepted(Notification notification, String acceptOrReject);
}
