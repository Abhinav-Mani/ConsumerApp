package com.binaryBeasts.consumerapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binaryBeasts.consumerapp.Models.Products;
import com.binaryBeasts.consumerapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{
    ArrayList<Products> list;
    Context context;
    ProductListAdapterClickHandler productListAdapterClickHandler;
    public interface ProductListAdapterClickHandler{
        public void onProductClicked(int position,ImageView imageView);
    }
    public ProductListAdapter(ArrayList<Products> list, Context context,ProductListAdapterClickHandler handler) {
        this.list = list;
        this.context = context;
        this.productListAdapterClickHandler=handler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_layout,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.productName.setText(list.get(position).getProductName());
        holder.DeliverCharge.setText(list.get(position).getDelivery());
        holder.Price.setText(list.get(position).getPrice());
        holder.product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productListAdapterClickHandler.onProductClicked(position,holder.imageView);
            }
        });

        Glide.with(context).asBitmap().
                load(list.get(position).getImg()).
                fitCenter().
                error(R.drawable.vegi).
                fallback(R.drawable.vegi).
                placeholder(R.drawable.vegi).
                diskCacheStrategy(DiskCacheStrategy.RESOURCE).
                into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView productName,Price,DeliverCharge;
        Button call,bagain,deliver,order;
        LinearLayout product;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.productImageSingle);
            productName=itemView.findViewById(R.id.productNameSingle);
            Price=itemView.findViewById(R.id.Price);
            DeliverCharge=itemView.findViewById(R.id.chargeDelivery);
            product=itemView.findViewById(R.id.Product);
        }
    }
}