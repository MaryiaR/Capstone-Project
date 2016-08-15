package com.udacity.rasulava.capstone_project.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.udacity.rasulava.capstone_project.DataBackupAgent;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.model.IntakeItem;
import com.udacity.rasulava.capstone_project.ui.CaloriesWidgetProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maryia on 12.08.2016.
 */
public class DBHelper {
    private Context context;

    private ContentResolver contentResolver;

    public DBHelper(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    public List<Product> getProductsByName(String name) {
        Uri uri = FoodContract.ProductEntry.buildProductWithNameUri(name);
//        String selection = FoodContract.ProductEntry.COLUMN_PRODUCT_NAME + " LIKE ?";
//        String[] selectionArgs = new String[]{name};
        List<Product> productList = new ArrayList<>();
        try {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setId((long) cursor.getInt(cursor.getColumnIndex(FoodContract.ProductEntry._ID)));
                    product.setName(cursor.getString(cursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_NAME)));
                    product.setFat(cursor.getInt(cursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_FAT)));
                    product.setCarbohydrate(cursor.getInt(cursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_CARB)));
                    product.setProtein(cursor.getInt(cursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_PROTEIN)));
                    product.setCalories(cursor.getInt(cursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_CALORIES)));
                    productList.add(product);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return productList;
    }

    public List<IntakeItem> getHistoryForDate(Date date) {
        String selection = DataProvider.historyByDateSelection;
        String[] selectionArgs = new String[]{String.valueOf(Utils.roundDateToDay(date.getTime()))};
        List<Intake> resultList = new ArrayList<>();
        try {
            Cursor cursor = contentResolver.query(FoodContract.IntakeEntry.CONTENT_URI, null, selection, selectionArgs, null);
            if (cursor.moveToFirst()) {
                do {
                    Intake intake = new Intake();
                    intake.setId((long) cursor.getInt(cursor.getColumnIndex(FoodContract.IntakeEntry._ID)));
                    intake.setDate(new Date(cursor.getLong(cursor.getColumnIndex(FoodContract.IntakeEntry.COLUMN_DATE))));
                    intake.setWeight(cursor.getInt(cursor.getColumnIndex(FoodContract.IntakeEntry.COLUMN_WEIGHT)));
                    int productId = cursor.getInt(cursor.getColumnIndex(FoodContract.IntakeEntry.COLUMN_PRODUCT_ID));

                    Cursor productCursor = contentResolver.query(FoodContract.ProductEntry.buildProductUri(productId), null, null, null, null);
                    if (productCursor.moveToFirst()) {
                        Product product = new Product();
                        product.setId((long) productId);
                        product.setName(productCursor.getString(productCursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_NAME)));
                        product.setFat(productCursor.getInt(productCursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_FAT)));
                        product.setCarbohydrate(productCursor.getInt(productCursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_CARB)));
                        product.setProtein(productCursor.getInt(productCursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_PROTEIN)));
                        product.setCalories(productCursor.getInt(productCursor.getColumnIndex(FoodContract.ProductEntry.COLUMN_PRODUCT_CALORIES)));
                        intake.setProduct(product);
                    }
                    productCursor.close();
                    resultList.add(intake);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<IntakeItem> intakeList = new ArrayList<>();
        for (Intake dbIntake : resultList) {
            intakeList.add(new IntakeItem(dbIntake));
        }
        return intakeList;
    }


    public long save(Product product) {
        ContentValues cv = new ContentValues();
        cv.put(FoodContract.ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
        cv.put(FoodContract.ProductEntry.COLUMN_PRODUCT_FAT, product.getFat());
        cv.put(FoodContract.ProductEntry.COLUMN_PRODUCT_CARB, product.getCarbohydrate());
        cv.put(FoodContract.ProductEntry.COLUMN_PRODUCT_PROTEIN, product.getProtein());
        cv.put(FoodContract.ProductEntry.COLUMN_PRODUCT_CALORIES, product.getCalories());
        Uri newUri = contentResolver.insert(FoodContract.ProductEntry.CONTENT_URI, cv);

        DataBackupAgent.requestBackup(context);
        return FoodContract.ProductEntry.getIdFromUri(newUri);
    }

    public long save(Intake intake) {
        long date = Utils.roundDateToDay(intake.getDate().getTime());

        ContentValues cv = new ContentValues();
        cv.put(FoodContract.IntakeEntry.COLUMN_DATE, date);
        cv.put(FoodContract.IntakeEntry.COLUMN_WEIGHT, intake.getWeight());
        cv.put(FoodContract.IntakeEntry.COLUMN_PRODUCT_ID, intake.getProduct().getId());
        Uri newUri = contentResolver.insert(FoodContract.IntakeEntry.CONTENT_URI, cv);

        DataBackupAgent.requestBackup(context);
        CaloriesWidgetProvider.update(context);
        return FoodContract.IntakeEntry.getIdFromUri(newUri);
    }

    public boolean deleteOldData() {
        Date weekAgoDate = Utils.getDateWeekAgo();
        String selection = DataProvider.oldHistorySelection;
        String[] selectionArgs = new String[]{String.valueOf(weekAgoDate.getTime())};
        int rows = contentResolver.delete(FoodContract.IntakeEntry.CONTENT_URI, selection, selectionArgs);
        return rows > 0;
    }


    public void delete(Long intakeId) {
        Uri uri = FoodContract.IntakeEntry.buildIntakeUri(intakeId);
        contentResolver.delete(uri, null, null);
        DataBackupAgent.requestBackup(context);
        CaloriesWidgetProvider.update(context);
    }
}
