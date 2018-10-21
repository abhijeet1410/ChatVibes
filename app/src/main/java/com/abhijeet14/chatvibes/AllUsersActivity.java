package com.abhijeet14.chatvibes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllUsersActivity extends AppCompatActivity {
private RecyclerView listUser;
private DatabaseReference mRef;
private MyAllUsersAdapter m;
private static AllUsersActivity inst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        inst=this;
        listUser = findViewById(R.id.allUser_container);
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseRecyclerOptions<MyAllUsersData> option = new FirebaseRecyclerOptions.Builder<MyAllUsersData>()
                .setQuery(mRef,MyAllUsersData.class)
                .build();

        /*FirebaseRecyclerOptions.Builder<MyAllUsersData> option = new FirebaseRecyclerOptions.Builder<MyAllUsersData>();
        option.setQuery(mRef,MyAllUsersData.class);
        FirebaseRecyclerOptions<MyAllUsersData> options = option.build();*/
        m = new MyAllUsersAdapter(option,getApplicationContext());
        listUser.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listUser.setHasFixedSize(true);
        listUser.setAdapter(m);
    }

    public static AllUsersActivity getInstance() {
        return inst;
    }

    @Override
    protected void onStart() {
        super.onStart();
        m.startListening();
    }
}
