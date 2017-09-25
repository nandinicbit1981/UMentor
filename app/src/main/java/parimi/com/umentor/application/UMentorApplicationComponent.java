package parimi.com.umentor.application;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Created by nandpa on 9/25/17.
 */

@Component(modules={AndroidInjectionModule.class, UMentorApplicationModule.class})
public interface UMentorApplicationComponent extends AndroidInjector<UMentorApplication>{
}
