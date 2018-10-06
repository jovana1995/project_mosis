package com.example.aleksandra.backpack.Models;

public class FriendsModel {
    public String friendsKey;
    public  String user1ID;
    public  String user2ID;
    public  FriendsModel()
    {

    }

    public String getFriendsKey() {
        return friendsKey;
    }

    public void setFriendsKey(String friendsKey) {
        this.friendsKey = friendsKey;
    }

    public String getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(String user1ID) {
        this.user1ID = user1ID;
    }

    public String getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(String user2ID) {
        this.user2ID = user2ID;
    }
}
