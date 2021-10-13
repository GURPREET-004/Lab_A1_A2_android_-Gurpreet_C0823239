package com.example.assignment1.main;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.assignment1.R;
import com.example.assignment1.adapter.FragmentAdapter;
import com.example.assignment1.base.BaseViewModel;
import com.example.assignment1.database.DbHelper;
import com.example.assignment1.database.FeedEntry;
import com.example.assignment1.type.MyType2;
import com.example.assignment1.type.MyType3;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends BaseViewModel {
    @SuppressLint("StaticFieldLeak")
    Context context;

    public void setContext(Context context){
        this.context=context;
    }

    public  boolean doesDatabaseExists(){
        File dbFile = context.getDatabasePath(DbHelper.DATABASE_NAME);
        return dbFile.exists();
    }
    public void callFirstTime(){
        if (!doesDatabaseExists()){
            writeFirstTimeProviderData("Rahua","wholesale@rahua.pro","917-591-6142","40.745731","-73.978336"," New York, New York, USA 10016.");
            String ide=readIdFirstTime("Rahua");
            Log.e("test","not exists id= "+ide);

            Log.e("test","length of array"+context.getResources().getStringArray(R.array.productName).length);
            for (int i=0;i<context.getResources().getStringArray(R.array.productName).length;i++){
                Log.e("test","add product"+context.getResources().getStringArray(R.array.productName)[i]);
                writeFirstTimeTenProducts(context.getResources().getStringArray(R.array.productName)[i],context.getResources().getStringArray(R.array.productDescription)[i],context.getResources().getStringArray(R.array.productPrice)[i],ide);
            }
        }
    }
    public String readIdFirstTime(String proName){
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT *"
                        +" FROM " + FeedEntry.Entry.TABLE_PROVIDERS +
                        " WHERE "+FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME+" = '"+proName+"'"
                ,null);

        cursor1.moveToFirst();
        String providerId="";
        int count = cursor1.getCount();
        if (count > 0) {
            providerId=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
        }
        cursor1.close();
        return providerId;
    }
    public void writeFirstTimeProviderData(String name,String email,String phoneNo,String latitude_l,String longitude_l,String providerAddress){
        Log.e("test","write");
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //insert values into database table
        ContentValues values=new ContentValues();
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME,name);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS,email);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER,phoneNo);

        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT,latitude_l);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG,longitude_l);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION,providerAddress);

        db.insert(FeedEntry.Entry.TABLE_PROVIDERS,null,values);

    }
    public void writeFirstTimeTenProducts(String productName,String description,String price,String id){
        Log.e("test","ten pro "+productName+" "+price +" " +id);
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME,productName);
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION,description);
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE,price);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID,id);
        db.insert(FeedEntry.Entry.TABLE_PRODUCTS,null,values);
    }


    public void setViewPager(ViewPager viewPager, FragmentManager fragmentManager, TabLayout tabLayout){
        FragmentAdapter fragmentAdapter=new FragmentAdapter(fragmentManager);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public List<MyType3> readProductsData() {

        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT *"

                +" FROM " + FeedEntry.Entry.TABLE_PROVIDERS + " pv INNER JOIN "
                + FeedEntry.Entry.TABLE_PRODUCTS + " pd ON pv." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID
                + " = pd." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID,null);

        cursor1.moveToFirst();
        List<MyType3> itemIds = new ArrayList<>();
        int count = cursor1.getCount();
        if (count > 0) {
            String productNames= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME));
            String productDes = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION));
            String productPrice=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE));
            String providerId=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            String providerName=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME));
            String providerPhone=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER));
            String providerEmail=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS));
            String productId= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_ID));

            MyType3 type=new MyType3();
            type.providerName=providerName;
            type.providerEmail=providerEmail;
            type.providerId=providerId;
            type.providerPhone=providerPhone;
            type.productName=productNames;
            type.productDescription=productDes;
            type.productPrice=productPrice;
            type.productId=productId;
            itemIds.add(type);
        }


        while (cursor1.moveToNext()) {
            String productNames= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME));
            String productDes = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION));
            String productPrice=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE));
            String providerId=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            String providerName=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME));
            String providerPhone=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER));
            String providerEmail=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS));

            MyType3 type=new MyType3();
            type.providerName=providerName;
            type.providerEmail=providerEmail;
            type.providerId=providerId;
            type.providerPhone=providerPhone;
            type.productName=productNames;
            type.productDescription=productDes;
            type.productPrice=productPrice;

            itemIds.add(type);

        }
        cursor1.close();
        return itemIds;
    }


    public List<MyType2> readProvidersData() {
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT * FROM " + FeedEntry.Entry.TABLE_PROVIDERS,null);
        cursor1.moveToFirst();
        List<MyType2> itemIds = new ArrayList<>();
        int count = cursor1.getCount();
        if (count > 0) {
            String names= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME));
            String id = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            String email=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS));
            String location=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION));
            String phone=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER));
            String lati= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT));
            String longi= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG));
            String locati= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION));
            MyType2 type=new MyType2();
            type.id=id;
            type.email=email;
            type.location=location;
            type.phone=phone;
            type.name=names;
            type.latitude=lati;
            type.longitude=longi;
            type.location=locati;
            itemIds.add(type);
        }
        while (cursor1.moveToNext()) {
            String names= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME));
            String id = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            String email=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS));
            String location=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION));
            String phone=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER));
            String lati= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT));
            String longi= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG));
            String locati= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION));
            MyType2 type=new MyType2();
            type.id=id;
            type.email=email;
            type.location=location;
            type.phone=phone;
            type.name=names;
            type.latitude=lati;
            type.longitude=longi;
            type.location=locati;
            itemIds.add(type);

        }
        cursor1.close();

        return itemIds;
    }


}
