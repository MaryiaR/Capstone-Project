package com.udacity.rasulava.capstone_project;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Maryia on 14.08.2016.
 */
public class CaloriesApplication extends Application {
    private Tracker mTracker;
    private boolean googleServicesAvailable = true;

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker("YOUR ID");
        }
        return mTracker;
    }

    public boolean isGoogleServicesAvailable() {
        return googleServicesAvailable;
    }

    public void setGoogleServicesAvailable(boolean googleServicesAvailable) {
        this.googleServicesAvailable = googleServicesAvailable;
    }
}
