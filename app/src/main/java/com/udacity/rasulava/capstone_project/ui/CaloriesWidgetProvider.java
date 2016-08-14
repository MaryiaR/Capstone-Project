package com.udacity.rasulava.capstone_project.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.model.HistoryItem;

/**
 * Created by Maryia on 14.08.2016.
 */
public class CaloriesWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }

    public static void update(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, CaloriesWidgetProvider.class));
        if (ids != null)
            for (int i = 0; i < ids.length; i++) {
                CaloriesWidgetProvider.updateWidget(context, appWidgetManager, ids[i]);
            }
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {
        HistoryItem today = Utils.getTodayData(context);
        int kcalDaily = Utils.getDailyKcal(context);
        int max = kcalDaily;
        if (today.getKcal() > kcalDaily) {
            max = today.getKcal();
        }
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
        widgetView.setProgressBar(R.id.progressBar, max, today.getKcal(), false);
        widgetView.setTextViewText(R.id.tv_kcal, context.getString(R.string.widget_kcal, today.getKcal(), kcalDaily));
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }

}