package com.abhijeet14.chatvibes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MyAllUsersAdapter extends FirebaseRecyclerAdapter<MyAllUsersData,MyAllUsersViewHolder> {
    Context c;

    public MyAllUsersAdapter(@NonNull FirebaseRecyclerOptions<MyAllUsersData> options,Context c) {
        super(options);
        this.c = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyAllUsersViewHolder holder, final int position, @NonNull final MyAllUsersData model) {
        if(getRef(position).getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
         //holder.myMain.setVisibility(View.GONE);
            holder.v.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else {
            holder.setTvEmail(model.getEmail());
            holder.setTvName(model.getName());
            String url = model.getDp();
            Picasso.with(AllUsersActivity.getInstance()).load(url).into(holder.setAllUsersDp());
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(AllUsersActivity.getInstance(),PersonalChatActivity.class);
                    i.putExtra("uid",getRef(position).getKey());
                    AllUsersActivity.getInstance().startActivity(i);
                    AllUsersActivity.getInstance().finish();
                }

            });
        }
    }
    @NonNull
    @Override
    public MyAllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.layout_row,parent,false);
        return new MyAllUsersViewHolder(v);
    }
}
