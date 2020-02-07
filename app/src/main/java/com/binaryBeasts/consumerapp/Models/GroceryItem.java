package com.binaryBeasts.consumerapp.Models;

public class GroceryItem {
    String key,item;

    public GroceryItem(String key, String item) {
        this.key = key;
        this.item = item;
    }

    public GroceryItem() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
