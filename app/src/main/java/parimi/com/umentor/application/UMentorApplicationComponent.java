package parimi.com.umentor.application;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import parimi.com.umentor.notifications.MyFirebaseMessagingService;
import parimi.com.umentor.views.activity.MainActivity;
import parimi.com.umentor.views.fragment.EditProfileFragment;
import parimi.com.umentor.views.fragment.FilteredMentorListFragment;
import parimi.com.umentor.views.fragment.MentorSearchFragment;
import parimi.com.umentor.views.fragment.MessageListFragment;
import parimi.com.umentor.views.fragment.MyMentorListFragment;
import parimi.com.umentor.views.fragment.NotificationsFragment;
import parimi.com.umentor.views.fragment.ProfileFragment;
import parimi.com.umentor.views.fragment.SendMessageFragment;
import parimi.com.umentor.widget.UpdateWidgetService;

/**
 * Created by nandpa on 9/25/17.
 */

@Component(modules={AndroidInjectionModule.class, UMentorApplicationModule.class})
@Singleton
public interface UMentorApplicationComponent{
    void inject(MainActivity mainActivity);
    void inject(EditProfileFragment editProfileFragment);
    void inject(FilteredMentorListFragment filteredMentorListFragment);
    void inject(MentorSearchFragment mentorSearchFragment);
    void inject(MessageListFragment messageListFragment);
    void inject(MyMentorListFragment myMentorListFragment);
    void inject(NotificationsFragment notificationsFragment);
    void inject(ProfileFragment profileFragment);
    void inject(SendMessageFragment sendMessageFragment);
    void inject(MyFirebaseMessagingService myFirebaseMessagingService);
    void inject(UpdateWidgetService  updateWidgetService);

}
