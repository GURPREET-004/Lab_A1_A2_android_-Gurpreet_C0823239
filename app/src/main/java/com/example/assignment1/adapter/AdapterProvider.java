package com.example.assignment1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment1.R;
import com.example.assignment1.type.MyType2;

import java.util.List;

public class AdapterProvider extends RecyclerView.Adapter<AdapterProvider.ViewHolder>{
    List<MyType2> list;
    Context context;
    public AdapterProvider(List<MyType2> list){
        this.list=list;
    }
    @NonNull
    @Override
    public AdapterProvider.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProvider.ViewHolder holder, int position) {
        holder.setData(list.get(position).name,list.get(position).email,list.get(position).phone,list.get(position).location);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView cardView;
        TextView email,phone,address;

        TextView call,viewMap,sendEmail;
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
}
