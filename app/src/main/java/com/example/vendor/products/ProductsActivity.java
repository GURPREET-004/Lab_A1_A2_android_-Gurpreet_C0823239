package com.example.vendor.products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vendor.MyType;
import com.example.vendor.MyType3;
import com.example.vendor.R;
import com.example.vendor.base.BaseActivity;
import com.example.vendor.database.DbHelper;
import com.example.vendor.database.FeedEntry;
import com.example.vendor.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class ProductsActivity extends BaseActivity<ProductsViewModel> {
    @BindView(R.id.editTextProductName)
    EditText productName;
    @BindView(R.id.editTextDescription)
    EditText description;
    @BindView(R.id.editTextPrice)
    EditText price;


    String returnedProviderId;
    String id;

    String updateId;

    @Override
    protected ProductsViewModel createViewModel() {
        return new ViewModelProvider(this).get(ProductsViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);

        id=getIntent().getStringExtra("id");
        updateId=getIntent().getStringExtra("updateProductId");

        if (updateId!=null){
            List<MyType>list=readDataForUpdate(updateId);
            if (list!=null){
                if (list.size()!=0) {
                    productName.setText(list.get(0).name);
                    description.setText(list.get(0).description);
                    price.setText(list.get(0).price);
                }
            }
        }
    }
    public List<MyType> readDataForUpdate(String ide)
    {
        DbHelper dbHelper=new DbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT * FROM " + FeedEntry.Entry.TABLE_PRODUCTS
                +" WHERE "+FeedEntry.Entry.COLUMN_NAME_PRODUCT_ID+" = "+ide,null);

        cursor1.moveToFirst();
        List<MyType> itemIds = new ArrayList<>();
        int count = cursor1.getCount();
        if (count > 0) {
            String productNames= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME));
            String productDes = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION));
            String productPrice=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE));

            returnedProviderId=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            MyType type=new MyType();
            type.price=productPrice;
            type.name=productNames;
            type.description=productDes;

            Toast.makeText(getApplicationContext(), ""+productNames, Toast.LENGTH_SHORT).show();
            itemIds.add(type);
        }
        cursor1.close();
        return itemIds;
    }
    public void updateDatabase(String ide){
        String sname=productName.getText().toString();
        String sdescription=description.getText().toString();
        String sprice=price.getText().toString();

        DbHelper dbHelper=new DbHelper(getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME,sname);
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION,sdescription);
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE,sprice);

        db.update(FeedEntry.Entry.TABLE_PRODUCTS, values, FeedEntry.Entry.COLUMN_NAME_PRODUCT_ID+" = ?", new String[] {ide});
        db.close();
        Intent intents=new Intent(getApplicationContext(), DetailActivity.class);
        intents.putExtra("id",returnedProviderId);
        intents.putExtra("ide",returnedProviderId);
        startActivity(intents);
        finish();
        /*
        long newRowId=db.delete(FeedEntry.Entry.TABLE_PROVIDERS,FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+"="+id,null);

        db.execSQL("UPDATE table_name SET col_name='new_value' WHERE
                primary_column='primary_id'");

                db.execSQL("UPDATE "+FeedEntry.Entry.TABLE_PROVIDERS+" SET "+FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID
                        +"="+id+" WHERE "+FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+" = "+id,null);
        db.update(FeedEntry.Entry.TABLE_PROVIDERS,
                values,
                String.format("%s = ?", "primary_column"),
                new String[]{"primary_id"});


        UPDATE employees
        SET email = LOWER(
                firstname || "." || lastname || "@chinookcorp.com"
        );*/
    }

    @OnClick({R.id.next,R.id.backButton2})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.next:
                if (validations()){
                    if (updateId!=null){
                        updateDatabase(updateId);
                    }else{
                        writeData();
                    }
                }
                break;
            case R.id.backButton2:
                finish();
        }
    }
    public void requestFocus(View view){
        if (view instanceof EditText){
            view.requestFocus();
        }
    }
    public void animateEditor(EditText editText) {
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(900);
        TranslateAnimation slider = new TranslateAnimation(0, 20, 0,0 );
        slider.setDuration(1000);
        //slider.setFillAfter(true);
        //slider.setFillAfter(false);
        //slider.setFillBefore(false);
        slider.setInterpolator(new CycleInterpolator(6));
        editText.startAnimation(slider);

    }
    public boolean validations(){
        String sname=productName.getText().toString();
        String sdescription=description.getText().toString();
        String sprice=price.getText().toString();

        if (sname.matches("")){
            productName.setError("name can't be null");
            animateEditor(productName);
            requestFocus(productName);
            return false;
        }else if (sdescription.matches(""))
        {
            description.setError("email can't be null");
            animateEditor(description);
            requestFocus(description);
            return false;
        }else if (sprice.matches("")){
            price.setError("Phone no. cant be null");
            animateEditor(price);
            requestFocus(price);
            return false;
        }else
            return true;
    }

    public List<MyType3> readData() {

        DbHelper dbHelper=new DbHelper(getApplicationContext());
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
                        + " = pd." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID
                //+" WHERE pd." +FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+" = "+id
                ,null);

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
            //long itemid = cursor1.getLong(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));

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
        //Toast.makeText(getApplicationContext(), "sixe"+itemIds.size(), Toast.LENGTH_SHORT).show();
        return itemIds;
    }
    public void writeData(){
        DbHelper dbHelper=new DbHelper(getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //insert values into database table
        ContentValues values=new ContentValues();
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME,productName.getText().toString());
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION,description.getText().toString());
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE,price.getText().toString());
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID,id);
        long newRowId=db.insert(FeedEntry.Entry.TABLE_PRODUCTS,null,values);
        //Toast.makeText(getApplicationContext(), " "+id+" new row "+newRowId, Toast.LENGTH_SHORT).show();
        Intent i=new Intent(this,DetailActivity.class);
        i.putExtra("id",id);
        startActivity(i);
        finish();
    }
}