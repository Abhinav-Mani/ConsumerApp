package com.binaryBeasts.consumerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.binaryBeasts.consumerapp.Adapter.GroceryListAdapter;
import com.binaryBeasts.consumerapp.BrodcastReciver.RemainderBroadCast;
import com.binaryBeasts.consumerapp.Models.GroceryItem;
import com.binaryBeasts.consumerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroceryList extends AppCompatActivity implements GroceryListAdapter.ClickHandler, View.OnClickListener, AdapterView.OnItemSelectedListener {
    DatabaseReference mRef;
    ArrayList<GroceryItem> list;
    GroceryListAdapter adapter;
    EditText editText;
    ImageView addItem;
    RecyclerView recyclerView;
    Spinner snozeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        init();

        setListeners();

        fetch();
    }

    private void setListeners() {
        addItem.setOnClickListener(this);
        snozeTime.setOnItemSelectedListener(this);
    }

    private void fetch() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    GroceryItem item=new GroceryItem(dataSnapshot1.getKey(),String.valueOf(dataSnapshot1.getValue()));
                    Log.d("ak47", "onDataChange: "+item.getKey());
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        createNotification();
        getSupportActionBar().setTitle("GroceryList");
        mRef=FirebaseDatabase.getInstance().getReference().child("GroceryList").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        list=new ArrayList<>();
        adapter=new GroceryListAdapter(list,GroceryList.this);
        editText=findViewById(R.id.newItem);
        addItem=findViewById(R.id.addItem);

        recyclerView=findViewById(R.id.GroceryList);
        snozeTime=findViewById(R.id.durationOf);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void Delete(int position) {
        mRef.child(list.get(position).getKey()).removeValue();
    }

    @Override
    public void onClick(View view) {
        if(view==addItem){
            String item=editText.getText().toString().trim();
            if(!TextUtils.isEmpty(item))
            {
                mRef.push().setValue(item);
                editText.setText("");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView==snozeTime){
            long ts=7*1000;
            if(i==0)
            {
                return;
            }
            Toast.makeText(GroceryList.this,"Remmainder Set",Toast.LENGTH_SHORT).show();
            switch (i){
                case 1:
                    ts=7*24*60*60*1000;
                    break;
                case 2:
                    ts=4*24*60*60*1000;
                    break;
                case 3:
                    ts=2*24*60*60*1000;
                    break;
                case 4:
                    ts=1*24*60*60*1000;
                    break;
                case 5:
                    ts=14*24*60*60*1000;
                    break;
                case 6:
                    ts=10*1000;
                    break;
            }
            Intent intent=new Intent(GroceryList.this, RemainderBroadCast.class);
            PendingIntent intent1=PendingIntent.getBroadcast(GroceryList.this,0,intent,0);
            AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
            long initTime=System.currentTimeMillis();

            alarmManager.set(AlarmManager.RTC_WAKEUP,initTime+ts,intent1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void createNotification() {
        Log.d("ak47","createNot");
        CharSequence name="Notify";
        String description="this is Description";
        int importance= NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.d("ak47","createNot b");
            NotificationChannel channel = new NotificationChannel("notifactionBrtoadcast",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
