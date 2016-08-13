package com.udacity.rasulava.capstone_project;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupManager;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;

import com.udacity.rasulava.capstone_project.db.DBHelper;

/**
 * Created by Maryia on 13.08.2016.
 */
public class DataBackupAgent extends BackupAgentHelper {

    @Override
    public void onCreate() {
        FileBackupHelper fileBackupHelper = new FileBackupHelper(this, "../databases/" + DBHelper.DB_NAME);
        addHelper(DBHelper.DB_NAME, fileBackupHelper);

        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, Utils.PREFS_NAME);
        addHelper("prefs", helper);
    }

    public static void requestBackup(Context context) {
        BackupManager bm = new BackupManager(context);
        bm.dataChanged();
    }
}