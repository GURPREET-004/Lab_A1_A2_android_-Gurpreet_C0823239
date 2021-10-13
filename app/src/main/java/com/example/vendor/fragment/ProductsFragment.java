package com.example.vendor.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.MyType;
import com.example.vendor.MyType3;
import com.example.vendor.R;
import com.example.vendor.adapter.AdapterProducts;
import com.example.vendor.database.DbHelper;
import com.example.vendor.database.FeedEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

@SuppressLint("NonConstantResourceId")
public class ProductsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private List<MyType3> list;
    private static final String ARG_PARAM3 = "param3";
    @BindView(R.id.productsRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.productNotAvailable)
    TextView productNotAva;
    @BindView(R.id.productNotAvailable2)
    TextView notAvalable;
    @BindView(R.id.search_editText)
    EditText search;

    public ProductsFragment() {
    }

    public static ProductsFragment newInstance(List<MyType3> list) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM3, (Serializable) list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //list= (List<MyType3>) getArguments().getSerializable(ARG_PARAM3);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this,view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list=readData();
        if (list.size()!=0){
            productNotAva.setVisibility(View.GONE);
            notAvalable.setVisibility(View.GONE);
            AdapterProducts adapter=new AdapterProducts(list);
            recyclerView.setAdapter(adapter);
        }else {
            productNotAva.setVisibility(View.VISIBLE);
            notAvalable.setVisibility(View.VISIBLE);
        }

        return view;
    }
    @OnTextChanged(R.id.search_editText)
    void onTextChanged(CharSequence text) {
        List<MyType3> listTemp=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            if (list.get(i).productName.contains(text)){
                listTemp.add(list.get(i));
            }
        }
        if (listTemp.size()!=0){
            AdapterProducts adapter2=new AdapterProducts(listTemp);
            recyclerView.setAdapter(adapter2);
        }else
            Toast.makeText(getActivity(), "No Match found",Toast.LENGTH_LONG).show();
    }

    @OnTextChanged(value = R.id.search_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onAfterTextChanged(CharSequence text) {
        try {


        }catch (Exception ignore){}

    }

    public List<MyType3> readData() {

        DbHelper dbHelper=new DbHelper(getActivity());
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

    public List<MyType> readData3() {
        DbHelper dbHelper=new DbHelper(getActivity());
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

        //Cursor cursor3 = db.rawQuery("SELECT * FROM " + FeedEntry.Entry.TABLE_NAME + " WHERE " + FeedEntry.Entry.COLUMN_NAME_EMAIL + "= '" + emailid + "'", null);

        Cursor cursor1 = db.rawQuery("SELECT * FROM " + FeedEntry.Entry.TABLE_PRODUCTS,null);
/*
        Cursor cursor2 = db.rawQuery("SELECT * FROM " + FeedEntry.Entry.TABLE_PROVIDERS + " pv INNER JOIN "
                + FeedEntry.Entry.TABLE_PRODUCTS + " pd ON pv." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID
                + " = pd." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID,null);
*/
        cursor1.moveToFirst();
        int count = cursor1.getCount();
        if (count > 0) {
            String name1 = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME));
            //Toast.makeText(getActivity(), "name of user" + name1, Toast.LENGTH_SHORT).show();
        }


        List<MyType> itemIds = new ArrayList<>();
        while (cursor1.moveToNext()) {
            String names= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME));
            String description= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION));
            String price= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE));
            MyType type=new MyType();
            type.name=names;
            type.description=description;
            type.price=price;
            long itemid = cursor1.getLong(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            //Toast.makeText(getActivity(), "<- " + cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID)), Toast.LENGTH_SHORT).show();
            itemIds.add(type);

        }
        cursor1.close();
        //Toast.makeText(getActivity(), "size of list  " + itemIds.size(), Toast.LENGTH_SHORT).show();
        //<-<-<-<-squilite database
        return itemIds;
    }

}