package com.example.vendor.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.vendor.MainActivity;
import com.example.vendor.MyType2;
import com.example.vendor.MyType3;
import com.example.vendor.base.BaseActivity;
import com.example.vendor.products.ProductsActivity;
import com.example.vendor.R;
import com.example.vendor.adapter.AdapterProducts;
import com.example.vendor.callback.SwipeToDeleteCallback;
import com.example.vendor.callback.SwipeToUpdateCallback;
import com.example.vendor.database.DbHelper;
import com.example.vendor.database.FeedEntry;
import com.example.vendor.maps.MapsActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class DetailActivity extends BaseActivity<DetailViewModel> {

    String name,email,phone,id;
    @BindView(R.id.popupName)
    TextView providerName;
    @BindView(R.id.popupEmail)
    TextView  providerEmail;
    @BindView(R.id.popupContact)
    TextView providerPhone;
    @BindView(R.id.popupRecyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.popupAddress)
    TextView addressLine;

    int REQUEST_PHONE_CALL=1;



    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    List<MyType2> list;

    AdapterProducts adapterProducts;

    @Override
    protected DetailViewModel createViewModel() {
        return new ViewModelProvider(this).get(DetailViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        viewModel.setContext(DetailActivity.this);
        enableSwipeToDeleteAndUndo();
        enableSwipeToUpdateAndUndo();

        id=getIntent().getStringExtra("id");

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapterProducts=new AdapterProducts(viewModel.readData(id));
        list=viewModel.readData2(id);
        if (list!=null){
            if (list.size()!=0) {
                providerName.setText(list.get(0).name);
                providerEmail.setText(list.get(0).email);
                providerPhone.setText(list.get(0).phone);
                addressLine.setText(list.get(0).location);
            }
        }
        recyclerView.setAdapter(adapterProducts);

    }

    private void enableSwipeToUpdateAndUndo(){
        SwipeToUpdateCallback swipeToUpdateCallback=new SwipeToUpdateCallback(this){
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                super.onSwiped(viewHolder, direction);
                final int position = viewHolder.getAdapterPosition();
                final MyType3 item = adapterProducts.getData().get(position);

                Intent i=new Intent(DetailActivity.this, ProductsActivity.class);
                i.putExtra("updateProductId",item.productId);
                startActivity(i);
                finish();
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToUpdateCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final MyType3 item = adapterProducts.getData().get(position);
                //Toast.makeText(getApplicationContext(), " name "+item.productId, Toast.LENGTH_SHORT).show();
                MyType3 type3=adapterProducts.removeItem(item.productId,position);
                //adapterProducts.notifyItemRemoved(position);
                Snackbar snackbar = Snackbar.make(constraintLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapterProducts.restoreItem(type3, position);
                        recyclerView.scrollToPosition(position);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    @OnClick({R.id.fab3,R.id.popupImage2,R.id.tempContact,R.id.tempEmail,R.id.popupImage,R.id.tempAddress,
            R.id.popupImage3,R.id.popupAddress,R.id.popupEmail,R.id.popupContact,R.id.sendEmailTextView2,R.id.callTextView2,R.id.viewMap2})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.fab3:
                viewModel.floatActionButton(id);
                break;
            case R.id.callTextView2:
            case R.id.popupContact:
            case R.id.popupImage2:
            case R.id.tempContact:
                viewModel.call(DetailActivity.this,providerPhone.getText().toString());
                break;
            case R.id.sendEmailTextView2:
            case R.id.popupEmail:
            case R.id.tempEmail:
            case R.id.popupImage:
                viewModel.sendMail(providerEmail.getText().toString(),"Subject");
                break;
            case R.id.viewMap2:
            case R.id.popupAddress:
            case R.id.tempAddress:
            case R.id.popupImage3:
                viewModel.viewLocation(list);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }

}