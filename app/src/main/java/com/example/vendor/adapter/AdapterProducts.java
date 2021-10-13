package com.example.vendor.adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.MyType3;
import com.example.vendor.R;
import com.example.vendor.database.DbHelper;
import com.example.vendor.database.FeedEntry;

import java.util.List;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.ViewHolder> {
    List<MyType3> list;
    Context context;
    public AdapterProducts(List<MyType3> list){
        this.list=list;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_view,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setData(list.get(position).productName,list.get(position).productDescription,list.get(position).productPrice);

        /*
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context.getApplicationContext(), DetailActivity.class);
                i.putExtra("id",list.get(position).providerId);
                context.startActivity(i);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,description;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.productName);
            description=itemView.findViewById(R.id.customDescription);
            price=itemView.findViewById(R.id.customPrice);

            cardView=itemView.findViewById(R.id.productCardView);

        }
        public void setData(String dataName,String dataDescription,String dataPrice){
            name.setText(dataName);
            price.setText("$ "+dataPrice);
            description.setText(dataDescription);
        }
    }
    public MyType3 removeItem(String productId,int position) {

        MyType3 type3=list.get(position);
        list.remove(position);
        DbHelper dbHelper=new DbHelper(context.getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        long newRowId=db.delete(FeedEntry.Entry.TABLE_PRODUCTS,FeedEntry.Entry.COLUMN_NAME_PRODUCT_ID+"="+productId,null);
        notifyItemRemoved(position);
        return type3;
    }

    public List<MyType3> getData(){
        return list;
    }
    public void restoreItem(MyType3 type3, int position) {
        list.add(position,type3);
        writeData(type3);
        //data.add(position, item);
        notifyItemInserted(position);
    }
    public void writeData(MyType3 type3){


        DbHelper dbHelper=new DbHelper(context.getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_NAME,type3.productName);
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_DESCRIPTION,type3.productDescription);
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_PRICE,type3.productPrice);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID,type3.providerId);
        values.put(FeedEntry.Entry.COLUMN_NAME_PRODUCT_ID,type3.productId);
        long newRowId=db.insert(FeedEntry.Entry.TABLE_PRODUCTS,null,values);

    }
}
