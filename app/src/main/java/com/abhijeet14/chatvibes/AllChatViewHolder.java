package com.abhijeet14.chatvibes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllChatViewHolder extends RecyclerView.ViewHolder {
    private TextView chatUserName, chatUserTime;
    private CircleImageView allChatDp;
    View v;

    public AllChatViewHolder(View itemView) {
        super(itemView);
        v = itemView;
        chatUserName = itemView.findViewById(R.id.all_chat_name);
        chatUserTime = itemView.findViewById(R.id.all_chat_time);
        allChatDp = itemView.findViewById(R.id.all_chat_dp);
    }
    public void setChatUserName(String name){
        this.chatUserName.setText(name);
    }

    public CircleImageView getAllChatDp() {
        return allChatDp;
    }
}
