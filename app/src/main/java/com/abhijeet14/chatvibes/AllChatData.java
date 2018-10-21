package com.abhijeet14.chatvibes;

import com.google.firebase.database.PropertyName;

public class AllChatData {
    @PropertyName("id")
    private String id;
    private String dp;

    public AllChatData(String id, String dp) {
        this.id = id;
        this.dp = dp;
    }

    public AllChatData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
