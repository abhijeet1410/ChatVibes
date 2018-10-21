package com.abhijeet14.chatvibes;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AllChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef,userRef;
    private RecyclerView allChatList;
    private String myUid;
    private FirebaseRecyclerAdapter<AllChatData,AllChatViewHolder> f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chat);
        mAuth = FirebaseAuth.getInstance();
        allChatList = findViewById(R.id.allChat_container);
        allChatList.hasFixedSize();
        allChatList.setLayoutManager(new LinearLayoutManager(AllChatActivity.this,LinearLayoutManager.VERTICAL,false));
        myUid = mAuth.getCurrentUser().getUid();
        myRef  = FirebaseDatabase.getInstance().getReference().child("chat").child(myUid);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseRecyclerOptions<AllChatData> options = new FirebaseRecyclerOptions.Builder<AllChatData>().setQuery(myRef.orderByValue(),AllChatData.class).build();
        f = new FirebaseRecyclerAdapter<AllChatData, AllChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AllChatViewHolder holder, final int position, @NonNull AllChatData model) {
                final String id = getRef(position).getKey();

                DatabaseReference db = userRef.child(id);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        holder.setChatUserName(name);
                        String url=dataSnapshot.child("dp").getValue(String.class);
                        Picasso.with(AllChatActivity.this).load(url).into(holder.getAllChatDp());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AllChatActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),PersonalChatActivity.class);
                        i.putExtra("uid",id);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public AllChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(AllChatActivity.this).inflate(R.layout.all_chat_row,parent,false);
                return new AllChatViewHolder(view);
            }
        };
        allChatList.setAdapter(f);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.allchat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_profile:
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                finish();
                break;
            case R.id.menu_logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to logout !")
                        .setTitle("Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }



    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE).getState() != NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED){
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("Network connection failed!");
            builder.setCancelable(false);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
        }
    }

    public void doChange(View view) {
        startActivity(new Intent(AllChatActivity.this,AllUsersActivity.class));
    }
}
