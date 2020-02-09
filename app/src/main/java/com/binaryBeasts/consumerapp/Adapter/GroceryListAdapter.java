package com.binaryBeasts.consumerapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binaryBeasts.consumerapp.Activities.GroceryList;
import com.binaryBeasts.consumerapp.Models.GroceryItem;
import com.binaryBeasts.consumerapp.R;

import java.util.ArrayList;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.MyViewHolder>{
    ArrayList<GroceryItem> list;
    ClickHandler clickHandler;
    public GroceryListAdapter(ArrayList<GroceryItem> list, GroceryList groceryList) {
        this.list = list;
        clickHandler=groceryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_grocery_list,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.item.setText(list.get(position).getItem());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.Delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface ClickHandler{
        public void Delete(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item;
        ImageView remove;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item=itemView.findViewById(R.id.itemGrocery);
            remove=itemView.findViewById(R.id.removeFromList);
        }
    }
}
