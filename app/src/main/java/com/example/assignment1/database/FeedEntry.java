package com.example.assignment1.database;

import android.provider.BaseColumns;

public class FeedEntry {
    private FeedEntry() {

    }

    public static class Entry implements BaseColumns {
       public static final String TABLE_PRODUCTS = "products";
        public static final String TABLE_PROVIDERS = "providers";

         public static final String COLUMN_NAME_PRODUCT_ID = "product_id";
        public static final String COLUMN_NAME_PRODUCT_NAME = "product_name";
        public static final String COLUMN_NAME_PRODUCT_DESCRIPTION = "product_description";
        public static final String COLUMN_NAME_PRODUCT_PRICE = "product_price";

        public static final String COLUMN_NAME_PROVIDER_ID = "provider_id";

        public static final String COLUMN_NAME_PROVIDER_NAME = "provider_name";
        public static final String COLUMN_NAME_PROVIDER_EMAIL_ADDRESS = "provider_email_address";
        public static final String COLUMN_NAME_PROVIDER_PHONE_NUMBER = "provider_phone_no";
        public static final String COLUMN_NAME_PROVIDER_LOCATION = "provider_location";

        public static final String COLUMN_NAME_PROVIDER_LAT= "latitude";
        public static final String COLUMN_NAME_PROVIDER_LONG= "longitude";
    }

    public static final String SQL_CREATE_TABLE_PROVIDERS = "CREATE TABLE " + Entry.TABLE_PROVIDERS +" ("
            + Entry.COLUMN_NAME_PROVIDER_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Entry.COLUMN_NAME_PROVIDER_NAME+ " TEXT,"
            + Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS+ " TEXT,"
            + Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER+ " TEXT,"
            + Entry.COLUMN_NAME_PROVIDER_LAT+ " TEXT,"
            + Entry.COLUMN_NAME_PROVIDER_LONG+ " TEXT,"
            + Entry.COLUMN_NAME_PROVIDER_LOCATION+ " TEXT);";

    public static final String SQL_DELETE_PROVIDERS = "DROP TABLE IF EXISTS "+ Entry.TABLE_PROVIDERS;

    public static final String SQL_CREATE_TABLE_PRODUCTS = "CREATE TABLE " + Entry.TABLE_PRODUCTS +" ("
            + Entry.COLUMN_NAME_PRODUCT_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Entry.COLUMN_NAME_PROVIDER_ID+ " INTEGER,"
            + Entry.COLUMN_NAME_PRODUCT_NAME+ " TEXT,"
            + Entry.COLUMN_NAME_PRODUCT_DESCRIPTION+ " TEXT,"
            + Entry.COLUMN_NAME_PRODUCT_PRICE+ " TEXT,"
            + "FOREIGN KEY("+ Entry.COLUMN_NAME_PROVIDER_ID+ ") REFERENCES "+ Entry.TABLE_PROVIDERS +"(" + Entry.COLUMN_NAME_PROVIDER_ID + ")ON DELETE CASCADE);";

    //NUMERIC
    public static final String SQL_DELETE_PRODUCTS = "DROP TABLE IF EXISTS "+ Entry.TABLE_PRODUCTS;
}