package com.binaryBeasts.consumerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.binaryBeasts.consumerapp.Adapter.MyOrderListAdapter;
import com.binaryBeasts.consumerapp.Fragments.FeedBackFormDialog;
import com.binaryBeasts.consumerapp.Models.DeliveryRequests;
import com.binaryBeasts.consumerapp.Models.FeedBack;
import com.binaryBeasts.consumerapp.Models.MyOrderModel;
import com.binaryBeasts.consumerapp.Models.OrderRequest;
import com.binaryBeasts.consumerapp.Models.Products;
import com.binaryBeasts.consumerapp.R;
import com.binaryBeasts.consumerapp.Utils.SetPersistence;
import com.binaryBeasts.consumerapp.Values.FeedbackValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrders extends AppCompatActivity implements MyOrderListAdapter.ClickHandler, FeedBackFormDialog.DialogListener {
    RecyclerView recyclerView;
    MyOrderListAdapter adapter;
    ArrayList<MyOrderModel> list;
    DatabaseReference mRef;
    String myAddress;
    FirebaseUser user;
    int amount;
    FeedbackValues values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        init();

        fetch();

    }



    private void fetch() {
        mRef.child("Consumers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myAddress=(String)dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef.child("Consumers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    list.add(new MyOrderModel(dataSnapshot1.getValue().toString(),"Checking","Checking","Checking",dataSnapshot1.getKey()));
                    getMore(dataSnapshot1.getValue().toString(),dataSnapshot1.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMore(final String date, final String key) {
        mRef.child("Products").child(key).child("productName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String productName=(String) dataSnapshot.getValue();
                int position=0;
                for(position=0;position<list.size();position++){
                    MyOrderModel myOrderModel=list.get(position);
                    if (myOrderModel.getDate().trim().equalsIgnoreCase(date.trim())&&myOrderModel.getProductId().trim().equalsIgnoreCase(key.trim())){
                        myOrderModel.setProductName(productName);
                        adapter.notifyItemChanged(position);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef.child("Requests").child(key).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderRequest orderRequest=(OrderRequest) dataSnapshot.getValue(OrderRequest.class);
                if(orderRequest==null)
                    return;
                int position=0;
                for (position=0;position<list.size();position++)
                {
                    MyOrderModel myOrderModel=list.get(position);
                    if (myOrderModel.getDate().trim().equalsIgnoreCase(date.trim())&&myOrderModel.getProductId().trim().equalsIgnoreCase(key.trim())){
                        myOrderModel.setDeliveryCost(orderRequest.getDeliverPrice());
                        myOrderModel.setProductCost(orderRequest.getProductPrice());
                        myOrderModel.setStatus(orderRequest.getStatus());
                        break;
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
        //SetPersistence persistence=new SetPersistence();
        list=new ArrayList<>();

        mRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=findViewById(R.id.myOrdersList);
        adapter=new MyOrderListAdapter(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        user=FirebaseAuth.getInstance().getCurrentUser();

        amount=1;

        values=new FeedbackValues();


    }

    @Override
    public void delete(int position) {
        MyOrderModel myOrderModel=list.get(position);
        list.remove(position);
        adapter.notifyItemRemoved(position);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("Consumers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("Canceled").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long val=(long)dataSnapshot.getValue()+1;
                mRef.child("Consumers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("Canceled").setValue(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef.child("Requests").child(myOrderModel.getProductId()).child(user.getUid()).removeValue();
        mRef.child("Consumers").child(user.getPhoneNumber()).child("Orders").child(myOrderModel.getProductId()).removeValue();
        final String id=myOrderModel.getProductId();
        if(myOrderModel.getStatus().trim().equalsIgnoreCase("Pending...")) {
            mRef.child("Products").child(id).child("pendingRequests").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long cv=(long) dataSnapshot.getValue();
                    mRef.child("Products").child(id).child("pendingRequests").setValue(cv-1);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        adapter.notifyDataSetChanged();
    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @Override
    public void search(final int position) {
        final MyOrderModel myOrderModel=list.get(position);
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("Products").child(myOrderModel.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Products products=dataSnapshot.getValue(Products.class);
                DeliveryRequests deliveryRequests=new DeliveryRequests(products.getPhoneNo(),products.getLocation(),user.getPhoneNumber(),myAddress,amount+"");
                mRef.child("Delivery").child(user.getUid()+"|-|-|"+myOrderModel.getProductId()).setValue(deliveryRequests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(this,"Search Driver",Toast.LENGTH_LONG).show();
    }

    @Override
    public void call(int position) {
        final MyOrderModel myOrderModel=list.get(position);
        mRef.child("Delivery").child(user.getUid()+"|-|-|"+myOrderModel.getProductId()).child("driver").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String[] phoneNo = {(String) dataSnapshot.getValue()};
                if(phoneNo[0] ==null)
                {
                    mRef.child("Products").child(myOrderModel.getProductId()).child("phoneNo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        phoneNo[0] =(String) dataSnapshot.getValue();
                        call(phoneNo[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                }else {
                    call(phoneNo[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Toast.makeText(this,"CAll",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void feedback(int position) {
        values.setPosition(position);
        openDialog();
        Toast.makeText(MyOrders.this,"Feedback",Toast.LENGTH_SHORT).show();
    }

    private void openDialog() {
        FeedBackFormDialog dialog=new FeedBackFormDialog();
        dialog.show(getSupportFragmentManager(),"Feedback");
    }

    @Override
    public void submit(String Farmer, String Delivery) {
        final MyOrderModel myOrderModel=list.get(values.getPosition());
        final FeedBack feedBack=new FeedBack("","","",Farmer,Delivery);
        Log.d("ak47", "submit: "+Farmer+" "+Delivery);
        mRef.child("Delivery").child(user.getUid()+"|-|-|"+myOrderModel.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DeliveryRequests deliveryRequests=dataSnapshot.getValue(DeliveryRequests.class);
                if(deliveryRequests!=null)
                {
                    feedBack.setFarmerNumber(deliveryRequests.getFarmersNumber());
                    feedBack.setDeliveryPersonNumer(deliveryRequests.getDriver());
                    submit(feedBack);
                }
                else {
                    mRef.child("Products").child(myOrderModel.getProductId()).child("phoneNo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String phoneno =(String) dataSnapshot.getValue();
                            feedBack.setFarmerNumber(phoneno);
                            feedBack.setDeliveryPersonNumer(phoneno+"(Self Delivery)");
                            submit(feedBack);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(MyOrders.this,"Submit"+values.getPosition(),Toast.LENGTH_SHORT).show();
    }

    private void submit(FeedBack feedBack) {
        feedBack.setCustomerNumber(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        Log.d("ak47", "submit: "+feedBack.getCustomerNumber()+" "+feedBack.getFarmerNumber()+" "+feedBack.getDeliveryPersonNumer()+" "+feedBack.getFarmerReview()+feedBack.getFarmerReview());
        mRef.child("FeedBack").push().setValue(feedBack);

    }

    @Override
    public void pass() {
        Toast.makeText(MyOrders.this,"Pass",Toast.LENGTH_SHORT).show();
    }
}
