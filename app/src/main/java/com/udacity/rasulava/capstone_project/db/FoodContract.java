package com.udacity.rasulava.capstone_project.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Maryia on 15.08.2016.
 */
public class FoodContract {

    public static final String CONTENT_AUTHORITY = "com.udacity.rasulava.capstone_project";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PRODUCT = "product";
    public static final String PATH_INTAKE = "intake";

    public static final class ProductEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRODUCT).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        public static final String TABLE_NAME = "product";

        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_CALORIES = "calories";
        public static final String COLUMN_PRODUCT_FAT = "fat";
        public static final String COLUMN_PRODUCT_PROTEIN = "protein";
        public static final String COLUMN_PRODUCT_CARB = "carbohydrate";

        public static Uri buildProductUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static Uri buildProductWithNameUri(String name) {
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }
    }

    public static final class IntakeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INTAKE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INTAKE;

        public static final String TABLE_NAME = "intake";

        public static final String COLUMN_WEIGHT = "weight";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_PRODUCT_ID = "product_id";

        public static Uri buildIntakeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

    }
}
