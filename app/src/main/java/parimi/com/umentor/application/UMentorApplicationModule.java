package parimi.com.umentor.application;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import parimi.com.umentor.views.activity.MainActivity;

/**
 * Created by nandpa on 9/25/17.
 */

@Module
public abstract class UMentorApplicationModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
}
