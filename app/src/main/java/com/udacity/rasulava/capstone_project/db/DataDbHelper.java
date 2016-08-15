package com.udacity.rasulava.capstone_project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maryia on 15.08.2016.
 */
public class DataDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "calories.db";

    public DataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + FoodContract.ProductEntry.TABLE_NAME + " (" +
                FoodContract.ProductEntry._ID + " INTEGER PRIMARY KEY," +
                FoodContract.ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                FoodContract.ProductEntry.COLUMN_PRODUCT_FAT + " INTEGER NOT NULL, " +
                FoodContract.ProductEntry.COLUMN_PRODUCT_CARB + " INTEGER NOT NULL, " +
                FoodContract.ProductEntry.COLUMN_PRODUCT_PROTEIN + " INTEGER NOT NULL, " +
                FoodContract.ProductEntry.COLUMN_PRODUCT_CALORIES + " INTEGER NOT NULL " +
                " );";

        final String SQL_CREATE_INTAKE_TABLE = "CREATE TABLE " + FoodContract.IntakeEntry.TABLE_NAME + " (" +

                FoodContract.IntakeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                FoodContract.IntakeEntry.COLUMN_PRODUCT_ID + " INTEGER NOT NULL, " +
                FoodContract.IntakeEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                FoodContract.IntakeEntry.COLUMN_WEIGHT + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + FoodContract.IntakeEntry.COLUMN_PRODUCT_ID + ") REFERENCES " +
                FoodContract.ProductEntry.TABLE_NAME + " (" + FoodContract.ProductEntry._ID + "), " +

                " UNIQUE (" + FoodContract.IntakeEntry.COLUMN_DATE + ", " +
                FoodContract.IntakeEntry.COLUMN_PRODUCT_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INTAKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FoodContract.ProductEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FoodContract.IntakeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
