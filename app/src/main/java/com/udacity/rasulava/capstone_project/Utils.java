package com.udacity.rasulava.capstone_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.udacity.rasulava.capstone_project.db.DBHelper;
import com.udacity.rasulava.capstone_project.model.HistoryItem;
import com.udacity.rasulava.capstone_project.model.IntakeItem;
import com.udacity.rasulava.capstone_project.model.UserData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Maryia on 12.08.2016.
 */
public class Utils {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private static final Calendar calendar = Calendar.getInstance();
    private static final String PREFS_NAME = "user_prefs";

    private static final String PREFS_KCAL_KEY = "prefs_kcal";
    private static final String PREFS_GENDER_KEY = "prefs_gender";
    private static final String PREFS_WEIGHT_KEY = "prefs_weight";
    private static final String PREFS_HEIGHT_KEY = "prefs_height";
    private static final String PREFS_EXERCISE_KEY = "prefs_exercise";
    private static final String PREFS_GOAL_KEY = "prefs_goal";
    private static final String PREFS_AGE_KEY = "prefs_age";

    /**
     * Uses Mifflin-St Jeor formula
     *
     * @param userData
     * @return
     */
    public static int getCaloriesNeed(UserData userData) {
        double kcal = 10 * userData.getWeight() + 6.25 * userData.getHeight() - 5 * userData.getAge();
        switch (userData.getGender()) {
            case FEMALE:
                kcal = (kcal - 161) * userData.getExercise().getFactor() * userData.getGoal().getFactor();
                break;
            case MALE:
                kcal = (kcal + 5) * userData.getExercise().getFactor() * userData.getGoal().getFactor();
                break;
        }
        return (int) kcal;
    }

    public static boolean haveInternetConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public static Date getDateFromString(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    private static HistoryItem createHistoryItem(Context context, String date) {
        List<IntakeItem> dbIntakeList = DBHelper.getInstance(context).getHistoryForDate(date);
        HistoryItem item = intakesToHistoryItem(dbIntakeList);
        item.setDate(date);
        return item;
    }

    public static List<HistoryItem> getHistoryForAWeek(Context context) {
        List<HistoryItem> list = new ArrayList<>();
        calendar.setTime(new Date());
        for (int i = 0; i < 6; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String date = Utils.dateToString(calendar.getTime());
            HistoryItem item = createHistoryItem(context, date);
            list.add(item);
        }

        return list;
    }

    public static HistoryItem getTodayData(Context context) {
        return createHistoryItem(context, Utils.dateToString(new Date()));
    }

    public static HistoryItem intakesToHistoryItem(List<IntakeItem> intakeList) {

        HistoryItem item = new HistoryItem();
        if (intakeList == null || intakeList.isEmpty()) {
            return item;
        }

        int fatSum = 0;
        int carbsSum = 0;
        int protSum = 0;
        int kcalSum = 0;
        for (IntakeItem intake : intakeList) {
            fatSum += intake.getFat();
            carbsSum += intake.getCarbs();
            protSum += intake.getProtein();
            kcalSum += intake.getKcal();
        }
        item.setCarbs(carbsSum);
        item.setFat(fatSum);
        item.setProtein(protSum);
        item.setKcal(kcalSum);

        return item;
    }

    public static void updateUserData(Context context, UserData data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(PREFS_AGE_KEY, data.getAge());
        ed.putString(PREFS_GENDER_KEY, data.getGender().name());
        ed.putInt(PREFS_WEIGHT_KEY, data.getWeight());
        ed.putInt(PREFS_HEIGHT_KEY, data.getHeight());
        ed.putString(PREFS_EXERCISE_KEY, data.getExercise().name());
        ed.putString(PREFS_GOAL_KEY, data.getGoal().name());
        ed.putInt(PREFS_KCAL_KEY, data.getKcal());
        ed.commit();
    }

    public static UserData getUserData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        UserData userData = new UserData();
        userData.setAge(sharedPreferences.getInt(PREFS_AGE_KEY, 0));
        userData.setGender(UserData.GENDER.valueOf(sharedPreferences.getString(PREFS_GENDER_KEY, UserData.GENDER.FEMALE.name())));
        userData.setWeight(sharedPreferences.getInt(PREFS_WEIGHT_KEY, 0));
        userData.setHeight(sharedPreferences.getInt(PREFS_HEIGHT_KEY, 0));
        userData.setExercise(UserData.EXERCISE.valueOf(sharedPreferences.getString(PREFS_EXERCISE_KEY, UserData.EXERCISE.LITTLE.name())));
        userData.setGoal(UserData.GOAL.valueOf(sharedPreferences.getString(PREFS_GOAL_KEY, UserData.GOAL.LOSS.name())));
        userData.setKcal(sharedPreferences.getInt(PREFS_KCAL_KEY, 0));
        return userData;
    }

    public static int getDailyKcal(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PREFS_KCAL_KEY, 0);
    }

}
