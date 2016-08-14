package com.udacity.rasulava.capstone_project.ui;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.udacity.rasulava.capstone_project.CaloriesApplication;

/**
 * Created by Maryia on 14.08.2016.
 */
public class TrackedActivity extends AppCompatActivity {
    private Tracker mTracker;

    @Override
    protected void onStart() {
        super.onStart();
        CaloriesApplication application = (CaloriesApplication) getApplication();
        if (application.isGoogleServicesAvailable()) {
            mTracker = application.getDefaultTracker();
            mTracker.setScreenName(getClass().getSimpleName());
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }
}
