package com.thelkl321.ilejeszcze;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class AppWidget extends AppWidgetProvider {

    private static final String LOG_TAG = "TimerWidget";
    static Calendar c = Calendar.getInstance();


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        // Get current hour in millis + minutes in millis
        c.setTimeInMillis(System.currentTimeMillis());
        long currentHourInMillis = TimeUnit.HOURS.toMillis(c.get(Calendar.HOUR_OF_DAY)) + TimeUnit.MINUTES.toMillis(c.get(Calendar.MINUTE));
        long nextHourInMillis = 0;
        long displayedHourInMillis = 0;

        for(int i=1; i<=16; i++){
            if(MainActivity.hoursInMillis.get(i) > currentHourInMillis){
                nextHourInMillis = MainActivity.hoursInMillis.get(i);
                break;
            }
        }
        displayedHourInMillis = nextHourInMillis-currentHourInMillis;

        c.setTimeInMillis(displayedHourInMillis);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minutes = String.valueOf(c.get(Calendar.MINUTE));

        if(Integer.parseInt(minutes)<10) minutes = "0" + minutes;
        views.setTextViewText(R.id.widgetText, hour + ":" + minutes);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private PendingIntent createTimerTickIntent(Context context) {
        Intent intent = new Intent(TIMER_WIDGET_UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "Widget Provider enabled.  Starting timer to update widget every 60 seconds");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);



        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, createTimerTickIntent(context));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "Widget Provider disabled. Turning off timer");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createTimerTickIntent(context));
    }

    /**
     * Custom Intent name that is used by the 'AlarmManager' to tell us to update the
     clock once per second.
     */
    public static String TIMER_WIDGET_UPDATE = "com.thelkl321.ilejeszcze.widget.TIMER_WIDGET_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "Received intent " + intent);
        if (TIMER_WIDGET_UPDATE.equals(intent.getAction())) {
            Log.d(LOG_TAG, "Timer update");
            // Get the widget manager and ids for this widget provider, then call the shared timer update method.
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);

            for (int appWidgetID: ids) {
                updateAppWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }
}

