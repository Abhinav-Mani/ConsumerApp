package com.binaryBeasts.consumerapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binaryBeasts.consumerapp.Activities.MyOrders;
import com.binaryBeasts.consumerapp.Models.MyOrderModel;
import com.binaryBeasts.consumerapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.MyViewHolder> {
    ArrayList<MyOrderModel> list;
    ClickHandler clickHandler;
    Context context;

    public MyOrderListAdapter(ArrayList<MyOrderModel> list, MyOrders myOrders)
    {
        this.list = list;
        clickHandler=myOrders;
        context=myOrders.getApplicationContext();
    }
    public interface ClickHandler{
        void delete(int position);
        void search(int position);
        void call(int position);
        void feedback(int position);
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
        MyOrderModel myOrderModel=list.get(position);
        if(myOrderModel.getProductName()!=null){
            holder.productName.setText(myOrderModel.getProductName());
        }
        if(myOrderModel.getStatus().trim().equalsIgnoreCase("Pending...")||myOrderModel.getStatus().trim().equalsIgnoreCase("Cancel")){
            holder.searchDriver.setVisibility(View.GONE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.callDriver.setVisibility(View.GONE);
            holder.feedback.setVisibility(View.GONE);
        } else if(myOrderModel.getStatus().trim().equalsIgnoreCase("Accepted")){
            holder.delete.setVisibility(View.GONE);
            holder.searchDriver.setVisibility(View.VISIBLE);
            holder.callDriver.setVisibility(View.GONE);
            holder.feedback.setVisibility(View.GONE);
        }else if(myOrderModel.getStatus().trim().equalsIgnoreCase("Delivered")){
            holder.delete.setVisibility(View.GONE);
            holder.searchDriver.setVisibility(View.GONE);
            holder.callDriver.setVisibility(View.GONE);
            holder.feedback.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
            holder.searchDriver.setVisibility(View.GONE);
            holder.callDriver.setVisibility(View.VISIBLE);
            holder.feedback.setVisibility(View.GONE);
        }
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
                clickHandler.search(position);
            }
        });
        holder.callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.call(position);
            }
        });
        holder.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.feedback(position);
            }
        });
        Glide.with(context).asBitmap().
                load(myOrderModel.getImg()).
                fitCenter().
                error(R.drawable.vegi).
                fallback(R.drawable.ic_delete_forever_black_24dp).
                placeholder(R.drawable.ic_block_black_24dp).
                diskCacheStrategy(DiskCacheStrategy.RESOURCE).
                into(holder.photo);
        Log.e("ak47",list.get(position).getImg()+" ");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,status,DeliveryCost,ProductCost,productName;
        Button delete, searchDriver,callDriver,feedback;
        ImageView photo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.singleDate);
            status=itemView.findViewById(R.id.singleStatus);
            DeliveryCost=itemView.findViewById(R.id.singleDeliveryCost);
            ProductCost=itemView.findViewById(R.id.singleProductCost);
            delete=itemView.findViewById(R.id.delete);
            searchDriver=itemView.findViewById(R.id.serchDriver);
            callDriver=itemView.findViewById(R.id.callDriver);
            feedback=itemView.findViewById(R.id.feedBack);
            productName=itemView.findViewById(R.id.singleProductName);
            photo=itemView.findViewById(R.id.productImage);
        }
    }
}
