package com.binaryBeasts.consumerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.binaryBeasts.consumerapp.Models.OrderRequest;
import com.binaryBeasts.consumerapp.Models.Products;
import com.binaryBeasts.consumerapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class ProductDescription extends AppCompatActivity implements View.OnClickListener {
    TextView productName,productPrice,deliverCharge,maxQuantity;
    ImageView imageView;
    Products products;
    DatabaseReference mRef;
    Button call,order,orderAndDeliver, searchDelivery,bidding;
    int price,deliveryCost=-1;
    String mobileno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        check();
        init();
        setLayout();
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

        mobileno= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
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

        call.setOnClickListener(this);
        order.setOnClickListener(this);
        orderAndDeliver.setOnClickListener(this);
        searchDelivery.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==call)
        {
            String phone = products.getPhoneNo();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        }else if(view==bidding){

        }else if(view==order){
            OrderRequest orderRequest=new OrderRequest(mobileno,String.valueOf(price),String.valueOf(deliveryCost),"1kg");
            orderRequest.setStatus("Pending...");
            if(deliveryCost==-1){
                orderRequest.setDeliverPrice("N/A");
            }
            Date currentTime = Calendar.getInstance().getTime();
            mRef.child("Requests").child(products.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(orderRequest);
            mRef.child("Consumers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("Orders").child(products.getKey()).setValue(currentTime.toString());
        }

    }

}
