package com.example.warehouseinventory.model;


import java.util.Date;

public class MessageModel {
    String message, uid;
    boolean sender;
    Date createdAt;

    public MessageModel(String message, String uid, boolean sender, Date createdAt) {
        this.message = message;
        this.uid = uid;
        this.sender = sender;
        this.createdAt = createdAt;
    }
}
