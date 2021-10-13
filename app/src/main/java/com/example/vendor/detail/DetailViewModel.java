package com.example.vendor.detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vendor.MyType2;
import com.example.vendor.MyType3;
import com.example.vendor.base.BaseViewModel;
import com.example.vendor.database.DbHelper;
import com.example.vendor.database.FeedEntry;
import com.example.vendor.maps.MapsActivity;
import com.example.vendor.products.ProductsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@SuppressLint("StaticFieldLeak")

public class DetailViewModel extends BaseViewModel {
    int REQUEST_PHONE_CALL=1;
    private Context context;

    public void setContext(Context context){
        this.context=context;
    }

    public void viewLocation(List<MyType2> list){
        Intent i=new Intent(context, MapsActivity.class);
        i.putExtra("lat",list.get(0).latitude);
        i.putExtra("longi",list.get(0).longitude);
        i.putExtra("providerName",list.get(0).name);
        context.startActivity(i);
    }
    public void sendMail(String addresses, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",addresses, null));
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
    public void call(Activity activity,String contactNo){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        else
        {
            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "+91"+contactNo));
                context.startActivity(callIntent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void floatActionButton(String id) {
        Intent i=new Intent(context, ProductsActivity.class);
        i.putExtra("id",id);
        context.startActivity(i);
        ((Activity)context).finish();
    }
    public List<MyType2> readData2(String ide) {
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT * FROM " + FeedEntry.Entry.TABLE_PROVIDERS
                +" WHERE "+FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+" = "+ide,null);

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
            MyType2 type=new MyType2();
            type.id=id;
            type.email=email;
            type.location=location;
            type.phone=phone;
            type.name=names;
            type.latitude=lati;
            type.longitude=longi;
            itemIds.add(type);
        }

        cursor1.close();
        return itemIds;
    }
    public List<MyType3> readData(String id) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor1 = db.rawQuery("SELECT *"
                        //+ FeedEntry.Entry.TABLE_PRODUCTS+" . "+FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME+","
                        //+ FeedEntry.Entry.TABLE_PRODUCTS+"."+FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION+","
                        //+ FeedEntry.Entry.TABLE_PRODUCTS+"."+FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE
                        + " FROM " + FeedEntry.Entry.TABLE_PROVIDERS + " pv INNER JOIN "
                        + FeedEntry.Entry.TABLE_PRODUCTS + " pd ON pv." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID
                        + " = pd." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID + " WHERE pd."
                        + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID + " = " + id
                , null);

        cursor1.moveToFirst();
        int count = cursor1.getCount();
        List<MyType3> itemIds = new ArrayList<>();
        if (count > 0) {
            String productNames = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME));
            String productDes = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION));
            String productPrice = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE));
            String providerId = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            String providerName = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME));
            String providerPhone = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER));
            String providerEmail = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS));
            String productId = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_ID));
            String lati= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT));
            String longi= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG));
            String location=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION));

            MyType3 type = new MyType3();
            type.providerName = providerName;
            type.providerEmail = providerEmail;
            type.providerId = providerId;
            type.providerPhone = providerPhone;
            type.productName = productNames;
            type.productDescription = productDes;
            type.productPrice = productPrice;
            type.productId=productId;
            type.latitude=lati;
            type.longitude=longi;
            type.providerLocation=location;
            itemIds.add(type);

        }
        while (cursor1.moveToNext()) {
            String productNames = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME));
            String productDes = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION));
            String productPrice = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE));
            String providerId = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            String providerName = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME));
            String providerPhone = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER));
            String providerEmail = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS));
            String productId = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_ID));
            String lati= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT));
            String longi= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG));
            String location=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION));
            /*
            MyType2 type2=new MyType2();
            type2.name=providerName;
            type2.phone=providerPhone;
            type2.email=providerEmail;
            list.add(type2);*/

            MyType3 type = new MyType3();
            type.providerName = providerName;
            type.providerEmail = providerEmail;
            type.providerId = providerId;
            type.providerPhone = providerPhone;
            type.productName = productNames;
            type.productDescription = productDes;
            type.productPrice = productPrice;
            type.productId=productId;
            type.latitude=lati;
            type.longitude=longi;
            type.providerLocation=location;
            itemIds.add(type);

        }
        cursor1.close();
        return itemIds;
    }

}
