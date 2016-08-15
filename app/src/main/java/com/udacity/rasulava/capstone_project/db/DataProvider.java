package com.udacity.rasulava.capstone_project.db;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Maryia on 15.08.2016.
 */
public class DataProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DataDbHelper mOpenHelper;

    static final int INTAKE = 100;
    static final int INTAKE_WITH_ID = 101;

    static final int PRODUCT = 200;
    static final int PRODUCT_WITH_ID = 201;
    static final int PRODUCT_WITH_NAME = 202;

    private static final String productByIdSelection = FoodContract.ProductEntry._ID + " = ? ";

    private static final String intakeByIdSelection = FoodContract.IntakeEntry._ID + " = ? ";

    public static final String oldHistorySelection = FoodContract.IntakeEntry.COLUMN_DATE + " <= ? ";

    public static final String historyByDateSelection = FoodContract.IntakeEntry.COLUMN_DATE + " = ? ";


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FoodContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FoodContract.PATH_INTAKE, INTAKE);
        matcher.addURI(authority, FoodContract.PATH_INTAKE + "/#", INTAKE_WITH_ID);

        matcher.addURI(authority, FoodContract.PATH_PRODUCT, PRODUCT);
        matcher.addURI(authority, FoodContract.PATH_PRODUCT + "/#", PRODUCT_WITH_ID);
        matcher.addURI(authority, FoodContract.PATH_PRODUCT + "/*", PRODUCT_WITH_NAME);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DataDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case INTAKE:
            case INTAKE_WITH_ID:
                return FoodContract.IntakeEntry.CONTENT_TYPE;
            case PRODUCT_WITH_NAME:
            case PRODUCT_WITH_ID:
                return FoodContract.ProductEntry.CONTENT_TYPE;
            case PRODUCT:
                return FoodContract.ProductEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case INTAKE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.IntakeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case INTAKE_WITH_ID: {
                String id = uri.getLastPathSegment();
                retCursor = mOpenHelper.getWritableDatabase().query(
                        FoodContract.IntakeEntry.TABLE_NAME,
                        projection,
                        intakeByIdSelection,
                        new String[]{id},
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case PRODUCT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case PRODUCT_WITH_ID: {
                String id = uri.getLastPathSegment();
                retCursor = mOpenHelper.getWritableDatabase().query(FoodContract.ProductEntry.TABLE_NAME,
                        projection,
                        productByIdSelection,
                        new String[]{id},
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case PRODUCT_WITH_NAME: {
                String name = uri.getLastPathSegment();
                selection = FoodContract.ProductEntry.COLUMN_PRODUCT_NAME + " LIKE ?";
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        new String[]{"%" + name + "%"},
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case INTAKE: {
                long _id = db.insert(FoodContract.IntakeEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FoodContract.IntakeEntry.buildIntakeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PRODUCT: {
                long _id = db.insert(FoodContract.ProductEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FoodContract.ProductEntry.buildProductUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case INTAKE:
                rowsDeleted = db.delete(
                        FoodContract.IntakeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INTAKE_WITH_ID:
                String id = uri.getLastPathSegment();
                rowsDeleted = db.delete(
                        FoodContract.IntakeEntry.TABLE_NAME, intakeByIdSelection, new String[]{id});
                break;

            case PRODUCT:
                rowsDeleted = db.delete(
                        FoodContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case INTAKE:
                rowsUpdated = db.update(FoodContract.IntakeEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PRODUCT:
                rowsUpdated = db.update(FoodContract.ProductEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
