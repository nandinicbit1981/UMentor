package parimi.com.umentor.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import parimi.com.umentor.R;

import static parimi.com.umentor.helper.Constants.MENTEE;
import static parimi.com.umentor.helper.Constants.MENTOR;
import static parimi.com.umentor.helper.Constants.RATING;

/**
 * Created by nandpa on 10/15/17.
 */

public class HomeWidgetProvider extends AppWidgetProvider {

    public static String UPDATE_ACTION = "ActionUpdateHomeWidget";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.home_widget);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        String mentee = "";
        String mentor = "";
        String rating = "";
        if(extras != null && extras.size() > 0) {
            mentee = String.valueOf(extras.getSerializable(MENTEE)!= null ? extras.getSerializable(MENTEE) : "");
            mentor = String.valueOf(extras.getSerializable(MENTOR)!= null ? extras.getSerializable(MENTOR) : "");
            rating = String.valueOf(extras.getSerializable(RATING)!= null ? extras.getSerializable(RATING) : "");
        } else {
            mentee = "0";
            mentor = "0";
            rating = "0";
        }


        if (action != null && action.equals(UPDATE_ACTION)) {
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName name = new ComponentName(context, HomeWidgetProvider.class);
            int[] appWidgetId = AppWidgetManager.getInstance(context).getAppWidgetIds(name);
            final int N = appWidgetId.length;
            if (N < 1)
            {
                return ;
            }
            else {
                int id = appWidgetId[N-1];
                updateWidget(context, appWidgetManager, id ,mentee, mentor, rating);
            }
        }

        else {
            super.onReceive(context, intent);
        }
    }


    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String mentee, String mentor, String rating){

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_widget);
        if(!mentee.equals("")) {
            views.setTextViewText(R.id.mentee_count, mentee);
        }
        if(!mentor.equals("")) {
            views.setTextViewText(R.id.mentor_count, mentor);
        }
        if(!rating.equals("")) {
            views.setTextViewText(R.id.rating, rating);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }


}
