package com.binaryBeasts.consumerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.binaryBeasts.consumerapp.Adapter.GroceryListAdapter;
import com.binaryBeasts.consumerapp.Models.GroceryItem;
import com.binaryBeasts.consumerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroceryList extends AppCompatActivity implements GroceryListAdapter.ClickHandler, View.OnClickListener {
    DatabaseReference mRef;
    ArrayList<GroceryItem> list;
    GroceryListAdapter adapter;
    EditText editText;
    ImageView addItem;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        init();
        fetch();
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
        getSupportActionBar().setTitle("GroceryList");
        mRef=FirebaseDatabase.getInstance().getReference().child("GroceryList").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        list=new ArrayList<>();
        adapter=new GroceryListAdapter(list,GroceryList.this);
        editText=findViewById(R.id.newItem);
        addItem=findViewById(R.id.addItem);
        addItem.setOnClickListener(this);
        recyclerView=findViewById(R.id.GroceryList);
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
}
