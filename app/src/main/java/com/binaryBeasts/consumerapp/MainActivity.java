package com.binaryBeasts.consumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.binaryBeasts.consumerapp.Activities.LoginActivity;
import com.binaryBeasts.consumerapp.Activities.MyOrders;
import com.binaryBeasts.consumerapp.Activities.ProductDescription;
import com.binaryBeasts.consumerapp.Adapter.ProductListAdapter;
import com.binaryBeasts.consumerapp.Models.Consumer;
import com.binaryBeasts.consumerapp.Models.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProductListAdapter.ProductListAdapterClickHandler{
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView ProductList;
    ArrayList<Products> list;
    ProductListAdapter adapter;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        checkNewUser();
        fetch();
    }

    private void checkNewUser() {
        if(getIntent().hasExtra("Consumer")){
            Consumer consumer=(Consumer) getIntent().getSerializableExtra("Consumer");
            reference.child("Consumers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).setValue(consumer);
        }
    }

    private void fetch() {
        reference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Products products=(Products) dataSnapshot1.getValue(Products.class);
                    products.setKey(dataSnapshot1.getKey());

                    list.add(products);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.myOrders:
                startActivity(new Intent(this, MyOrders.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    private void init() {
        mAuth=FirebaseAuth.getInstance();

        list=new ArrayList<>();

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();

        ProductList=findViewById(R.id.FarmersProductList);
        ProductList.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ProductListAdapter(list,this,this);
        ProductList.setAdapter(adapter);
    }

    @Override
    public void onProductClicked(int position, ImageView imageView) {
        Intent intent=new Intent(MainActivity.this, ProductDescription.class);
        intent.putExtra("Product",list.get(position));
        ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(this,imageView, ViewCompat.getTransitionName(imageView));
        startActivity(intent,options.toBundle());
    }
}
