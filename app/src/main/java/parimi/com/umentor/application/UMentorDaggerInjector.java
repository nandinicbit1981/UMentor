package parimi.com.umentor.application;

/**
 * Created by nandpa on 9/26/17.
 */

public class UMentorDaggerInjector {
    private static UMentorApplicationComponent appComponent = DaggerUMentorApplicationComponent.builder().build();
    public static UMentorApplicationComponent get() {
        return appComponent;
    }
}
