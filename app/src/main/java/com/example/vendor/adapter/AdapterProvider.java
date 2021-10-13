package com.example.vendor.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.detail.DetailActivity;
import com.example.vendor.MyType;
import com.example.vendor.MyType2;
import com.example.vendor.MyType3;
import com.example.vendor.R;
import com.example.vendor.database.DbHelper;
import com.example.vendor.database.FeedEntry;
import com.example.vendor.maps.MapsActivity;

import java.util.ArrayList;
import java.util.List;

public class AdapterProvider extends RecyclerView.Adapter<AdapterProvider.ViewHolder> {
    List<MyType2> list;
    Context context;
    int REQUEST_PHONE_CALL=1;
    public AdapterProvider(List<MyType2> list){
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(list.get(position).name);

        holder.setData(list.get(position).name,list.get(position).email,list.get(position).phone,list.get(position).location);


        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(position);
            }
        });
        holder.sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail(position);
            }
        });
        holder.viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLocation(position);
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context.getApplicationContext(), DetailActivity.class);
                i.putExtra("id",list.get(position).id);
                context.startActivity(i);
                /*
                View pop=LayoutInflater.from(context).inflate(R.layout.popup_view,null);
                holder.popupWindow=new PopupWindow(pop, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

                TextView name=pop.findViewById(R.id.popupName);
                TextView email=pop.findViewById(R.id.popupEmail);
                TextView contact=pop.findViewById(R.id.popupContact);
                TextView id=pop.findViewById(R.id.popupID);
                RecyclerView recyclerView=pop.findViewById(R.id.popupRecyclerview);

                //holder.popupWindow.setBackgroundDrawable(new ColorDrawable(context.getColor(R.color.black)));
                holder.popupWindow.setOutsideTouchable(true);
                holder.popupWindow.setFocusable(true);

                name.setText(list.get(position).name);
                email.setText(list.get(position).email);
                contact.setText(list.get(position).phone);
                id.setText(list.get(position).id);
                LinearLayoutManager layoutManager=new LinearLayoutManager(context.getApplicationContext());
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                AdapterProducts adapterProducts=new AdapterProducts(readData(list.get(position).id));
                recyclerView.setAdapter(adapterProducts);
                if (!holder.popupWindow.isShowing()){
                    holder.popupWindow.showAtLocation(view, Gravity.CENTER,10,10);
                    //dimbackground(holder.popupWindow);
                }*/
            }
        });
    }
    public List<MyType> readData(String id) {

        DbHelper dbHelper=new DbHelper(context.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
/*
        //projection define which column you want to use after query executes
        String[] projection = {
                FeedEntry.Entry.COLUMN_NAME_ID,
                FeedEntry.Entry.COLUMN_NAME_NAME
        };
        // where clause of query
        String selection = FeedEntry.Entry.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArg = {"a"
        };
        String sortOrder = FeedEntry.Entry.COLUMN_NAME_EMAIL + " ASC";

        Cursor cursor = db.query(
                FeedEntry.Entry.TABLE_NAME,
                projection,
                selection,
                selectionArg,
                null,
                null,
                null
        );*/

        Cursor cursor1 = db.rawQuery("SELECT *"
                //+ FeedEntry.Entry.TABLE_PRODUCTS+" . "+FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME+","
                //+ FeedEntry.Entry.TABLE_PRODUCTS+"."+FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION+","
                //+ FeedEntry.Entry.TABLE_PRODUCTS+"."+FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE
                +" FROM " + FeedEntry.Entry.TABLE_PROVIDERS + " pv INNER JOIN "
                + FeedEntry.Entry.TABLE_PRODUCTS + " pd ON pv." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID
                + " = pd." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID +" WHERE pd."
                        +FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+" = "+id
                ,null);

        cursor1.moveToFirst();
        int count = cursor1.getCount();
        if (count > 0) {
            String name1 = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME));

        }

        List<MyType> itemIds = new ArrayList<>();
        while (cursor1.moveToNext()) {
            String names= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME));
            String des = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION));
            String price=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE));
            //long itemid = cursor1.getLong(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));

            MyType type=new MyType();
            type.name=names;
            type.price=price;
            type.description=des;
            itemIds.add(type);

        }
        cursor1.close();
        Toast.makeText(context.getApplicationContext(), "sixe"+itemIds.size(), Toast.LENGTH_SHORT).show();
        return itemIds;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView cardView;
        TextView email,phone,address;

        TextView call,viewMap,sendEmail;
        PopupWindow popupWindow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.customCard);
            name=itemView.findViewById(R.id.customTextView);
            email=itemView.findViewById(R.id.customPopupEmail);
            phone=itemView.findViewById(R.id.customPopupContact);
            address=itemView.findViewById(R.id.customPopupAddress);
            call=itemView.findViewById(R.id.callTextView);
            viewMap=itemView.findViewById(R.id.viewMap);
            sendEmail=itemView.findViewById(R.id.sendEmailTextView);
        }
        public void setData(String dataName,String dataEmail,String dataPhone,String dataAddress){
            name.setText(dataName);

            email.setText(dataEmail);
            phone.setText(dataPhone);
            address.setText(dataAddress);
        }
    }

    public List<MyType3> removeItem(String productId, int position) {

        List<MyType3> type3=readDeletedData(productId);
        list.remove(position);
        DbHelper dbHelper=new DbHelper(context.getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        long newRowId=db.delete(FeedEntry.Entry.TABLE_PROVIDERS,FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+"="+productId,null);
        notifyItemRemoved(position);
        return type3;
    }

    public List<MyType2> readDeletetedDataWhenNoProduct(String id){
        DbHelper dbHelper = new DbHelper(context.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<MyType2> liste=new ArrayList<>();
        Cursor cursor1= db.rawQuery("SELECT *"
                + " FROM " + FeedEntry.Entry.TABLE_PROVIDERS +" WHERE "
                + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID + " = " + id,null);
        cursor1.moveToFirst();
        int count1= cursor1.getCount();
        if (count1 > 0) {
            String providerId = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            String providerName = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME));
            String providerPhone = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER));
            String providerEmail = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS));
            String lati= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT));
            String longi= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG));
            String location=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION));

            MyType2 type2=new MyType2();
            type2.name=providerName;
            type2.phone=providerPhone;
            type2.email=providerEmail;
            type2.id=providerId;
            type2.location=location;
            type2.latitude=lati;
            type2.longitude=longi;
            liste.add(type2);
        }
        while (cursor1.moveToNext()) {
            String providerId = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            String providerName = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME));
            String providerPhone = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER));
            String providerEmail = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS));
            String lati= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT));
            String longi= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG));
            String location=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION));

            MyType2 type2=new MyType2();
            type2.name=providerName;
            type2.phone=providerPhone;
            type2.email=providerEmail;
            type2.id=providerId;
            type2.location=location;
            type2.latitude=lati;
            type2.longitude=longi;
            liste.add(type2);
        }
        return liste;
    }

    public List<MyType3> readDeletedData(String id) {


        DbHelper dbHelper = new DbHelper(context.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT *"
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
        //list=new ArrayList<>();
        while (cursor1.moveToNext()) {
            //long itemid = cursor1.getLong(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));

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
    public List<MyType2> getData(){
        return list;
    }
    public void restoreItem(List<MyType3> type2, int position) {
        MyType2 myType2=new MyType2();
        myType2.email=type2.get(0).providerEmail;
        myType2.name=type2.get(0).providerName;
        myType2.id=type2.get(0).providerId;
        myType2.phone=type2.get(0).providerPhone;
        myType2.latitude=type2.get(0).latitude;
        myType2.longitude=type2.get(0).longitude;
        myType2.location=type2.get(0).providerLocation;
        list.add(position,myType2);
        writeData(myType2,type2);
        //data.add(position, item);
        notifyItemInserted(position);
    }
    public void restoreItemWhenNoProduct(List<MyType2> type2, int position) {
        MyType2 myType2=new MyType2();
        myType2.email=type2.get(0).email;
        myType2.name=type2.get(0).name;
        myType2.id=type2.get(0).id;
        myType2.phone=type2.get(0).phone;
        myType2.latitude=type2.get(0).latitude;
        myType2.longitude=type2.get(0).longitude;
        myType2.location=type2.get(0).location;
        list.add(position,myType2);
        //writeData(myType2,type2);
        writeDataWhenNoProduct(myType2);
        //data.add(position, item);
        notifyItemInserted(position);
    }

    public void writeData(MyType2 myType2,List<MyType3> myType3List){
        DbHelper dbHelper=new DbHelper(context.getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //insert values into database table
        ContentValues values=new ContentValues();
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME,myType2.name);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS,myType2.email);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER,myType2.phone);

        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT,myType2.latitude);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG,myType2.longitude);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION,myType2.location);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID,myType2.id);
        long newRowId=db.insert(FeedEntry.Entry.TABLE_PROVIDERS,null,values);
        //writeDataProducts(myType3List);
        /*Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
        finish();*/
        //Toast.makeText(getApplicationContext(), "->"+newRowId, Toast.LENGTH_SHORT).show();

    }
    public void writeDataWhenNoProduct(MyType2 myType2){
        DbHelper dbHelper=new DbHelper(context.getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //insert values into database table
        ContentValues values=new ContentValues();
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME,myType2.name);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS,myType2.email);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER,myType2.phone);

        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT,myType2.latitude);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG,myType2.longitude);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION,myType2.location);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID,myType2.id);
        long newRowId=db.insert(FeedEntry.Entry.TABLE_PROVIDERS,null,values);
        //writeDataProducts(myType3List);
        /*Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
        finish();*/
        //Toast.makeText(getApplicationContext(), "->"+newRowId, Toast.LENGTH_SHORT).show();

    }
    public void writeDataProducts(List<MyType3> myType3List){
        DbHelper dbHelper=new DbHelper(context.getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        for (int i=0;i<myType3List.size();i++){
            ContentValues values=new ContentValues();
            values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME,myType3List.get(i).productName);
            values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION,myType3List.get(i).productDescription);
            values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE,myType3List.get(i).productPrice);
            values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID,myType3List.get(i).providerId);
            long newRowId=db.insert(FeedEntry.Entry.TABLE_PRODUCTS,null,values);

        }
    }
    public static void dimbackground(PopupWindow popupWindow){
        View container=popupWindow.getContentView().getRootView();
        Context c=popupWindow.getContentView().getContext();
        WindowManager windowManager=(WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params=(WindowManager.LayoutParams) container.getLayoutParams();
        params.flags |=WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount =0.9f;
        windowManager.updateViewLayout(container,params);
    }
    public void call(int position){
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        else
        {
            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" +list.get(position).phone));
                context.getApplicationContext().startActivity(callIntent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    public void viewLocation(int position){
        Intent i=new Intent(context.getApplicationContext(), MapsActivity.class);
        i.putExtra("lat",list.get(position).latitude);
        i.putExtra("longi",list.get(position).longitude);
        i.putExtra("providerName",list.get(position).name);
        context.startActivity(i);
    }
    public void sendMail(int position) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",list.get(position).email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
