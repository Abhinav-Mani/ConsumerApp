package com.binaryBeasts.consumerapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binaryBeasts.consumerapp.Activities.MyOrders;
import com.binaryBeasts.consumerapp.Models.MyOrderModel;
import com.binaryBeasts.consumerapp.R;

import java.util.ArrayList;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.MyViewHolder> {
    ArrayList<MyOrderModel> list;
    ClickHandler clickHandler;

    public MyOrderListAdapter(ArrayList<MyOrderModel> list, MyOrders myOrders)
    {
        this.list = list;
        clickHandler=myOrders;
    }
    public static interface ClickHandler{
        public void delete(int position);
        public void seach(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_my_order,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.date.setText(list.get(position).getDate());
        holder.status.setText(list.get(position).getStatus());
        holder.DeliveryCost.setText(list.get(position).getDeliveryCost());
        holder.ProductCost.setText(list.get(position).getProductCost());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.delete(position);
            }
        });
        holder.searchDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.seach(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,status,DeliveryCost,ProductCost;
        Button delete, searchDriver;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.singleDate);
            status=itemView.findViewById(R.id.singleStatus);
            DeliveryCost=itemView.findViewById(R.id.singleDeliveryCost);
            ProductCost=itemView.findViewById(R.id.singleProductCost);
            delete=itemView.findViewById(R.id.delete);
            searchDriver=itemView.findViewById(R.id.serchDriver);
        }
    }
}
