package com.binaryBeasts.consumerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.binaryBeasts.consumerapp.Fragments.EditOrderDialog;
import com.binaryBeasts.consumerapp.Models.OrderRequest;
import com.binaryBeasts.consumerapp.Models.Products;
import com.binaryBeasts.consumerapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class ProductDescription extends AppCompatActivity implements View.OnClickListener,EditOrderDialog.DialogListener {
    TextView productName,productPrice,deliverCharge,maxQuantity;
    ImageView imageView;
    Products products;
    DatabaseReference mRef;
    FirebaseUser user;
    Button call,order,orderAndDeliver, searchDelivery,bidding;
    int price,deliveryCost=-1;
    String mobileno;
    OrderRequest orderRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        check();

        init();

        setListeners();

        checkFeatures();

        setLayout();
    }

    private void setListeners() {
        call.setOnClickListener(this);
        order.setOnClickListener(this);
        orderAndDeliver.setOnClickListener(this);
        searchDelivery.setOnClickListener(this);
        bidding.setOnClickListener(this);
    }

    private void checkFeatures() {
        mRef.child("Requests").child(products.getKey()).child(user.getUid()).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status=(String) dataSnapshot.getValue();
                if(status!=null) {
                    order.setEnabled(false);

                    if (status.equalsIgnoreCase("Pending...")) {
                        order.setBackgroundColor(Color.rgb(255, 204, 0));
                        order.setText("Ordered");
                    } else if (status.equalsIgnoreCase("Cancel")) {
                        order.setBackgroundColor(Color.rgb(255, 63, 0));
                        order.setText("Canceled");
                    } else {
                        order.setBackgroundColor(Color.rgb(136, 255, 0));
                        order.setText("Accepted");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setLayout() {
        if(products.getContact().equalsIgnoreCase("Bidding"))
        {
            call.setEnabled(false);
        }else if(products.getContact().equalsIgnoreCase("Call")){
            bidding.setEnabled(false);
        }
    }

    private void check() {
        Intent intent=getIntent();
        if(intent.hasExtra("Product")) {
            products=(Products) intent.getSerializableExtra("Product");
        }
        else {
            finish();
        }
    }

    private void init() {
        imageView=findViewById(R.id.ProductImage);
        productName=findViewById(R.id.productName);
        productPrice=findViewById(R.id.price);
        deliverCharge=findViewById(R.id.deliverCharge);
        maxQuantity=findViewById(R.id.maxQuantity);
        call=findViewById(R.id.call);
        order=findViewById(R.id.Order);
        bidding=findViewById(R.id.bidding);
        orderAndDeliver=findViewById(R.id.orderandDelivery);
        searchDelivery=findViewById(R.id.serchDelivery);
        user=FirebaseAuth.getInstance().getCurrentUser();

        orderRequest=new OrderRequest(mobileno,String.valueOf(price),String.valueOf(deliveryCost),"1kg");
        orderRequest.setStatus("Pending...");
        if(deliveryCost==-1){
            orderRequest.setDeliverPrice("N/A");
        }


        mobileno= user.getPhoneNumber();
        price=Integer.parseInt(products.getPrice());
        if(!products.getDelivery().equalsIgnoreCase("N/A"))
        {
            deliveryCost=Integer.parseInt(products.getDelivery());
        }

        mRef= FirebaseDatabase.getInstance().getReference();

        Glide.with(this).asBitmap().
                load(products.getImg()).
                fitCenter().
                error(R.drawable.vegi).
                fallback(R.drawable.vegi).
                placeholder(R.drawable.vegi).
                diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
        productName.setText(products.getProductName());
        productPrice.setText(products.getPrice());
        deliverCharge.setText(products.getDelivery());
        maxQuantity.setText(products.getQuality());


    }

    @Override
    public void onClick(View view) {
        if(view==call)
        {
            String phone = products.getPhoneNo();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        }else if(view==bidding){
            openDialog();
        }else if(view==order){
            Date currentTime = Calendar.getInstance().getTime();
            mRef.child("Products").child(products.getKey()).child("pendingRequests").setValue(products.getPendingRequests()+1);
            mRef.child("Requests").child(products.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(orderRequest);
            mRef.child("Consumers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("Orders").child(products.getKey()).setValue(currentTime.toString());
        }

    }

    private void openDialog() {
        Log.d("ak47", "openDialog: ");
        EditOrderDialog editOrderDialog=new EditOrderDialog();
        editOrderDialog.show(getSupportFragmentManager(),"Edit Order");

    }

    @Override
    public void applyEdits(String quantity, String price, String deliverPrice) {
        orderRequest.setAmount(quantity);
        orderRequest.setDeliverPrice(deliverPrice);
        orderRequest.setProductPrice(price);
    }

    @Override
    public Products getDetails() {
        return products;
    }
}
