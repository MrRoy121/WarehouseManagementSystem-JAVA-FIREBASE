package com.example.warehouseinventory.model;

public class CartModel {
    String name, itemid;
    int item;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public CartModel(String name, String itemid, int item) {
        this.name = name;
        this.item = item;
        this.itemid = itemid;
    }

}
