package com.abhijeet14.chatvibes;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

class MyAllUsersViewHolder extends RecyclerView.ViewHolder {
private TextView tvName,tvEmail;
public View v;
private CircleImageView allUsersDp;
    public MyAllUsersViewHolder(View itemView) {
        super(itemView);
        v = itemView;
        tvName = v.findViewById(R.id.row_name);
        tvEmail = v.findViewById(R.id.row_email);
        allUsersDp = itemView.findViewById(R.id.all_users_dp);
    }
    public void setTvName(String name){
        tvName.setText(name);
    }
    public void setTvEmail(String email){
        tvEmail.setText(email);
    }

    public CircleImageView setAllUsersDp() {
        return this.allUsersDp;

    }
}
