package com.example.foodDelivery.Model;

import java.util.List;

/*
 * @class - Model
 */
public class Requests {
    private String phone;
    private String name;
    private String address;
    private String total;
    private List<Order> foods;
    private String status;
    private String pk;

    public Requests() {
    }

    public Requests(String phone, String name, String address, String total, List<Order> foods, String pk) {
        this.pk = pk;
        this.status = "0";
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

}
