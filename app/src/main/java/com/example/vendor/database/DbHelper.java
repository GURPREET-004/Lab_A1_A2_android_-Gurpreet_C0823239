package com.example.vendor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FeedEntry.SQL_CREATE_TABLE_PROVIDERS);
        sqLiteDatabase.execSQL(FeedEntry.SQL_CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(FeedEntry.SQL_DELETE_PROVIDERS);
        sqLiteDatabase.execSQL(FeedEntry.SQL_DELETE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }
}
