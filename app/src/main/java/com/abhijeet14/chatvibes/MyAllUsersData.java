package com.abhijeet14.chatvibes;

public class MyAllUsersData {
    String name,email,dp;

    public MyAllUsersData() {
    }

    public MyAllUsersData(String name, String email, String dp) {

        this.name = name;
        this.email = email;
        this.dp = dp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
