package com.example.vendor.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vendor.MyType2;
import com.example.vendor.MyType3;
import com.example.vendor.providers.ProvidersActivity;
import com.example.vendor.R;
import com.example.vendor.callback.SwipeToDeleteCallback;
import com.example.vendor.adapter.AdapterProvider;
import com.example.vendor.callback.SwipeToUpdateCallback;
import com.example.vendor.database.DbHelper;
import com.example.vendor.database.FeedEntry;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class ProvidersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.providersRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.providerNotAvailable)
    TextView providerNotAvailable;
    @BindView(R.id.providerNotAvailable2)
    TextView notAvailable;
    private String mParam1;
    private String mParam2;
    List<MyType2>liste=new ArrayList<>();
    AdapterProvider adapterProducts;
    public ProvidersFragment() {

    }


    public static ProvidersFragment newInstance(String param1, String param2) {
        ProvidersFragment fragment = new ProvidersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_providers, container, false);
        ButterKnife.bind(this,view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        enableSwipeToDeleteAndUndo();
        enableSwipeToUpdateAndUndo();
        List<MyType2> list=readData();
        if (list.size()!=0){
            notAvailable.setVisibility(View.GONE);
            providerNotAvailable.setVisibility(View.GONE);
            adapterProducts=new AdapterProvider(list);
        }else{
            notAvailable.setVisibility(View.VISIBLE);
            providerNotAvailable.setVisibility(View.VISIBLE);
        }

        recyclerView.setAdapter(adapterProducts);
        return view;

    }
    private void enableSwipeToUpdateAndUndo(){
        SwipeToUpdateCallback swipeToUpdateCallback=new SwipeToUpdateCallback(getActivity()){
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                super.onSwiped(viewHolder, direction);
                final int position = viewHolder.getAdapterPosition();
                final MyType2 item = adapterProducts.getData().get(position);

                Intent i=new Intent(getActivity(),ProvidersActivity.class);
                i.putExtra("id",item.id);
                startActivity(i);

            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToUpdateCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final MyType2 item = adapterProducts.getData().get(position);

                //Toast.makeText(getApplicationContext(), " name "+item.productId, Toast.LENGTH_SHORT).show();

                if (adapterProducts.readDeletedData(item.id).size()==0){
                    liste=adapterProducts.readDeletetedDataWhenNoProduct(item.id);
                }
                List<MyType3> type3=adapterProducts.removeItem(item.id,position);


                //adapterProducts.notifyItemRemoved(position);

                //Toast.makeText(getActivity(), ""+type3.size(), Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.frameLayoutProvider), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (type3.size()!=0){
                            adapterProducts.restoreItem(type3, position);
                        }else{
                            adapterProducts.restoreItemWhenNoProduct(liste,position);
                        }

                        recyclerView.scrollToPosition(position);

                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                //Intent intenti=new Intent(getActivity(), MainActivity.class);
                //startActivity(intenti);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
    @OnClick(R.id.fab1)
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.fab1:
                floatActionButton();
                break;
        }
    }
    public void floatActionButton(){
        Intent i=new Intent(getActivity(), ProvidersActivity.class);
        startActivity(i);
    }

    public List<MyType2> readData() {
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

        Cursor cursor1 = db.rawQuery("SELECT * FROM " + FeedEntry.Entry.TABLE_PROVIDERS,null);
/*
        Cursor cursor2 = db.rawQuery("SELECT * FROM " + FeedEntry.Entry.TABLE_PROVIDERS + " pv INNER JOIN "
                + FeedEntry.Entry.TABLE_PRODUCTS + " pd ON pv." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID
                + " = pd." + FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID,null);
*/
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
            long itemid = cursor1.getLong(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            //Toast.makeText(getActivity(), "<- " + cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID)), Toast.LENGTH_SHORT).show();
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

            long itemid = cursor1.getLong(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            //Toast.makeText(getActivity(), "<- " + cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID)), Toast.LENGTH_SHORT).show();
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