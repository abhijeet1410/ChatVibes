package com.abhijeet14.chatvibes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout lin;
    public LinearLayout linPar;
    private TextView messageText, timeText;

    public ChatViewHolder(View itemView) {
        super(itemView);
        lin = itemView.findViewById(R.id.lin);
        messageText = itemView.findViewById(R.id.message_text);
        timeText = itemView.findViewById(R.id.message_time);
        linPar = itemView.findViewById(R.id.linParent);
    }

    public void setMessage(String message) {
        messageText.setText(message);
    }

    public void setTime(String time) {
        timeText.setText(time);
    }
}