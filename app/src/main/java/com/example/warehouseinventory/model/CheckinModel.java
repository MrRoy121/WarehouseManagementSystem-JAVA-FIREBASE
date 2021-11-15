package com.example.warehouseinventory.model;

public class CheckinModel {

    String name, brand, quantity, cid, pid, vendor;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public CheckinModel(String name, String brand, String quantity, String cid, String pid, String vendor) {
        this.name = name;
        this.brand = brand;
        this.quantity = quantity;
        this.cid = cid;
        this.vendor = vendor;
        this.pid = pid;
    }
}
