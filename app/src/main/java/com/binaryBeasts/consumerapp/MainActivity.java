package com.binaryBeasts.consumerapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.binaryBeasts.consumerapp.Activities.GroceryList;
import com.binaryBeasts.consumerapp.Activities.LoginActivity;
import com.binaryBeasts.consumerapp.Activities.MyOrders;
import com.binaryBeasts.consumerapp.Activities.ProductDescription;
import com.binaryBeasts.consumerapp.Activities.ProfileActivity;
import com.binaryBeasts.consumerapp.Adapter.ProductListAdapter;
import com.binaryBeasts.consumerapp.Models.Consumer;
import com.binaryBeasts.consumerapp.Models.Products;
import com.binaryBeasts.consumerapp.Utils.SetPersistence;
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
    LocationManager locationManager;
    LocationListener locationListener;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("ak47", "onRequestPermissionsResult: 1");
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.e("ak47", "onRequestPermissionsResult: 2");
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("ak47", "onRequestPermissionsResult: 3");
                return;
            }
            Log.e("ak47", "onRequestPermissionsResult: 4");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Log.e("ak47", "onRequestPermissionsResult: 5");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Log.e("ak47", "onRequestPermissionsResult: 6");
            Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.e("ak47", "onRequestPermissionsResult: 7");
            reference.child("Farmers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("address").setValue(lastKnownLocation.getLatitude()+" "+lastKnownLocation.getLongitude());
        }
    }

    private void setLocation() {
        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        reference.child("Farmers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("address").setValue(lastKnownLocation.getLatitude()+" "+lastKnownLocation.getLongitude());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListeners();
        checkNewUser();
        fetch();
    }

    private void setListeners() {
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    private void checkNewUser() {
        if(getIntent().hasExtra("Consumer")){
            Consumer consumer=(Consumer) getIntent().getSerializableExtra("Consumer");
            reference.child("Consumers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).setValue(consumer);
            //Set Location for new User
            setLocation();
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
                break;
            case R.id.myGroceryList:
                startActivity(new Intent(MainActivity.this, GroceryList.class));
                break;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
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
        //SetPersistence persistence=new SetPersistence();
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
