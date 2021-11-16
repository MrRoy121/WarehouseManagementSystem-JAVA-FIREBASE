package com.example.warehouseinventory.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestModel {
    String username;
    String status, image;
    String address;
    String rid, uid;
    Map<String, Object> products;
    Date date;

    public Map<String, Object> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Object> products) {
        this.products = products;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRid() {
        return rid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public RequestModel(String status, String address, String rid, Date date, Map<String, Object> products, String image) {
        this.status = status;
        this.address = address;
        this.rid = rid;
        this.products = products;
        this.image = image;
        this.date = date;
    }
    public RequestModel( String status, String address, Date date, String username, String uid, String rid, Map<String, Object> products, String image) {
        this.username = username;
        this.status = status;
        this.address = address;
        this.uid = uid;
        this.rid = rid;

        this.image = image;
        this.products = products;
        this.date = date;
    }
}
