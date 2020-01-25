package com.binaryBeasts.consumerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.binaryBeasts.consumerapp.Adapter.MyOrderListAdapter;
import com.binaryBeasts.consumerapp.Models.DeliveryRequests;
import com.binaryBeasts.consumerapp.Models.MyOrderModel;
import com.binaryBeasts.consumerapp.Models.OrderRequest;
import com.binaryBeasts.consumerapp.Models.Products;
import com.binaryBeasts.consumerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrders extends AppCompatActivity implements MyOrderListAdapter.ClickHandler{
    RecyclerView recyclerView;
    MyOrderListAdapter adapter;
    ArrayList<MyOrderModel> list;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        init();
        fetch();
    }



    private void fetch() {
        mRef.child("Consumers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    list.add(new MyOrderModel(dataSnapshot1.getValue().toString(),"Checking","Checking","Checking",dataSnapshot1.getKey()));
                    getMore(dataSnapshot1.getValue().toString(),dataSnapshot1.getKey(),list.size()-1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMore(final String date, final String key, final int position) {
        mRef.child("Requests").child(key).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderRequest orderRequest=(OrderRequest) dataSnapshot.getValue(OrderRequest.class);
                for (MyOrderModel myOrderModel:list)
                {
                    if (myOrderModel.getDate().trim().equalsIgnoreCase(date.trim())&&myOrderModel.getProductId().trim().equalsIgnoreCase(key.trim())){
                        myOrderModel.setDeliveryCost(orderRequest.getDeliverPrice());
                        myOrderModel.setProductCost(orderRequest.getProductPrice());
                        myOrderModel.setStatus(orderRequest.getStatus());
                        Log.d("ak47", "onDataChange: "+orderRequest.getDeliverPrice());
                    }
                }
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        list=new ArrayList<>();

        mRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=findViewById(R.id.myOrdersList);
        adapter=new MyOrderListAdapter(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void delete(int position) {
        MyOrderModel myOrderModel=list.get(position);
        list.remove(position);
        adapter.notifyItemRemoved(position);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("Requests").child(myOrderModel.getProductId()).child(user.getUid()).removeValue();
        mRef.child("Consumers").child(user.getPhoneNumber()).child("Orders").child(myOrderModel.getProductId()).removeValue();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void seach(final int position) {
        final MyOrderModel myOrderModel=list.get(position);
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("Products").child(myOrderModel.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Products products=dataSnapshot.getValue(Products.class);
                DeliveryRequests deliveryRequests=new DeliveryRequests(products.getPhoneNo(),products.getLocation(),user.getPhoneNumber(),"my address","100");
                mRef.child("Delivery").child(user.getUid()+myOrderModel.getProductId()).setValue(deliveryRequests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(this,"Search Driver",Toast.LENGTH_LONG).show();
    }
}
