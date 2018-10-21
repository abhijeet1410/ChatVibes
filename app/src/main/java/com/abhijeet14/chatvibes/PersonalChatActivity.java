package com.abhijeet14.chatvibes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalChatActivity extends AppCompatActivity {
    private RecyclerView list;
    private EditText messageText;
    private Button sendButton;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef,friendRef,msgRef,userRef;
    private String myUid,friendUid;
    private Button backBtn;
    private TextView appName;
    private CircleImageView appDp;
    private FirebaseRecyclerAdapter<MessageData,ChatViewHolder> f;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

       // getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_app_bar);

        View customApp = getSupportActionBar().getCustomView();
        backBtn = customApp.findViewById(R.id.app_bar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        appDp = customApp.findViewById(R.id.app_bar_dp);
        appName = customApp.findViewById(R.id.app_bar_name);


        list=findViewById(R.id.message_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        list.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PersonalChatActivity.this);
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
                return false;
            }
        });
        messageText=findViewById(R.id.message_text);
        sendButton=findViewById(R.id.send_button);
        messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString().trim())){
                    sendButton.setEnabled(false);
                }else{
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        friendUid=getIntent().getExtras().getString("uid");
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(friendUid);
        userRef.keepSynced(true);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appName.setText(dataSnapshot.child("name").getValue(String.class));
                String url = dataSnapshot.child("dp").getValue(String.class);
                Picasso.with(PersonalChatActivity.this).load(url).into(appDp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PersonalChatActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PersonalChatActivity.this,FriendProfileActivity.class);
                i.putExtra("frienduid",friendUid);
                startActivity(i);
            }
        });
        mAuth=FirebaseAuth.getInstance();
        myUid=mAuth.getCurrentUser().getUid();
        msgRef= FirebaseDatabase.getInstance().getReference().child("message");
        myRef=FirebaseDatabase.getInstance().getReference().child("chat").child(myUid).child(friendUid);
        friendRef=FirebaseDatabase.getInstance().getReference().child("chat").child(friendUid).child(myUid);
        FirebaseRecyclerOptions<MessageData> options=new FirebaseRecyclerOptions.Builder<MessageData>().setQuery(myRef,MessageData.class).build();
        f=new FirebaseRecyclerAdapter<MessageData, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull MessageData model) {
                String id=model.getId();
                DatabaseReference db=msgRef.child(id);
                db.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RtlHardcoded")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String text=dataSnapshot.child("text").getValue().toString();
                        String time=dataSnapshot.child("time").getValue().toString();
                        String sender=dataSnapshot.child("sender").getValue().toString();
                        holder.setMessage(text);
                        holder.setTime(time);
                        holder.lin.setVisibility(View.VISIBLE);
                        holder.linPar.setVisibility(View.VISIBLE);
                        if(sender.equals(myUid)){
                            holder.lin.setGravity(Gravity.RIGHT);
                            holder.lin.setBackground(ContextCompat.getDrawable(PersonalChatActivity.this, R.drawable.your_msg));
                            holder.linPar.setGravity(Gravity.RIGHT);
                        }else{
                            holder.lin.setGravity(Gravity.LEFT);
                            holder.lin.setBackground(ContextCompat.getDrawable(PersonalChatActivity.this, R.drawable.friend_msg));
                            holder.linPar.setGravity(Gravity.LEFT);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(PersonalChatActivity.this).inflate(R.layout.message_row,parent,false);
                return new ChatViewHolder(v);
            }
        };
        list.setAdapter(f);
    }

    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
    }

    public void sendMessage(View view) {
        String msg=messageText.getText().toString().trim();
        messageText.setText("");
        Date d=new Date();
        SimpleDateFormat s=new SimpleDateFormat("hh:mm:ss");
        String time=s.format(d);
        Map m=new HashMap();
        m.put("text",msg);
        m.put("time",time);
        m.put("sender",myUid);
        final String key=msgRef.push().getKey();
        msgRef.child(key).updateChildren(m).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    myRef.push().child("id").setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                friendRef.push().child("id").setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(PersonalChatActivity.this, "sent successfully", Toast.LENGTH_SHORT).show();
                                            messageText.setText("");
                                        }else{
                                            Toast.makeText(PersonalChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(PersonalChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(PersonalChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
