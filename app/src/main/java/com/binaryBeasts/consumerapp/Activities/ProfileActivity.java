package com.binaryBeasts.consumerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.binaryBeasts.consumerapp.R;
import com.binaryBeasts.consumerapp.Utils.Checker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView bronze,silver,gold;
    TextView name,phone,address,points;
    FirebaseUser user;
    DatabaseReference reference;
    long Canceled,Orders;
    String Name,Address,Phone;
    Checker checker;
    long point,listPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_profile);

        init();

        fetch();
    }

    private void fetch() {
        reference.child("GroceryList").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    listPoint= 50;
                    point+=listPoint;
                    points.setText(point+"");
                    Log.d("ak47", "onDataChange: has list");
                }
                setUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Consumers").child(user.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Phone =dataSnapshot.getKey();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equalsIgnoreCase("Canceled")) {
                        Canceled = (long) dataSnapshot1.getValue();
                    } else if (dataSnapshot1.getKey().equalsIgnoreCase("address")) {
                        Address = (String) dataSnapshot1.getValue();
                    } else if (dataSnapshot1.getKey().equalsIgnoreCase("name")) {
                        Name = (String) dataSnapshot1.getValue();
                    } else if (dataSnapshot1.getKey().equalsIgnoreCase("Orders")) {
                        Orders = (long) dataSnapshot1.getChildrenCount();
                    }
                }
                setUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setUI() {
        phone.setText(Phone);
        name.setText(Name);
        if(!checker.isMapCoordinates(Address)) {
            address.setText(Address);
        }
        else {
            address.setText("See in Map");
            address.setOnClickListener(this);
        }
        Log.d("ak47", Orders+"setUI: "+Canceled);

        point=Orders*10+50+listPoint-(15*Canceled);
        points.setText(point+"");
        if(point>0&&point<100){
            bronze.setVisibility(View.VISIBLE);
            silver.setVisibility(View.GONE);
            gold.setVisibility(View.GONE);
        }else if(point>=100&&point<1000) {
            bronze.setVisibility(View.GONE);
            silver.setVisibility(View.VISIBLE);
            gold.setVisibility(View.GONE);
        }else {
            bronze.setVisibility(View.GONE);
            silver.setVisibility(View.GONE);
            gold.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        checker=new Checker();
        reference=FirebaseDatabase.getInstance().getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();
        bronze=findViewById(R.id.bronze_medal);
        silver=findViewById(R.id.silver_medal);
        gold=findViewById(R.id.gold_medal);
        name=findViewById(R.id.name_profile);
        phone=findViewById(R.id.phone_profile);
        address=findViewById(R.id.address_Profile);
        points=findViewById(R.id.points);
    }

    @Override
    public void onClick(View view) {
    }
}
