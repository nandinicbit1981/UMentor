package parimi.com.umentor.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import parimi.com.umentor.R;

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
        String mentee = String.valueOf(extras.getSerializable("mentee")!= null ? extras.getSerializable("mentee") : "");
        String mentor = String.valueOf(extras.getSerializable("mentor")!= null ? extras.getSerializable("mentor") : "");
        String rating = String.valueOf(extras.getSerializable("rating")!= null ? extras.getSerializable("rating") : "");

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
