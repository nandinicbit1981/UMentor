package parimi.com.umentor.helper;

/**
 * Created by nandpa on 9/27/17.
 */

public enum NotificationType {
    REQUEST("REQUEST"),
    ACCEPT("ACCEPT"),
    MESSAGE("MESSAGE");

    private  String notificationType;

    NotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationType(){
        return this.notificationType;
    }

    public static NotificationType getByType(String type) {
        for (NotificationType mType : values()) {
            if (mType.getNotificationType().equals(type)) {
                return mType;
            }
        }
        return null;
    }
}
