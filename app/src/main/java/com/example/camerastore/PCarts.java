package com.example.camerastore;

import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PCarts {

    String imageUrl,name;
    int price,quantity,TotalQuantity;
    public PCarts(){}
    public PCarts(String imageUrl, String name, int price, int quantity) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public PCarts(String imageUrl, String name, int price, int quantity,int TotalQuantity) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.TotalQuantity = TotalQuantity;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        TotalQuantity = totalQuantity;
    }
}
