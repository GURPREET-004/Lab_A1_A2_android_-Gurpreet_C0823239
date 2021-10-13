package com.example.assignment1.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment1.R;
import com.example.assignment1.type.MyType3;

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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,description;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.productName);
            description=itemView.findViewById(R.id.customDescription);
            price=itemView.findViewById(R.id.customPrice);

            cardView=itemView.findViewById(R.id.productCardView);

        }
        @SuppressLint("SetTextI18n")
        public void setData(String dataName, String dataDescription, String dataPrice){
            name.setText(dataName);
            price.setText("$ "+dataPrice);
            description.setText(dataDescription);
        }
    }
}
