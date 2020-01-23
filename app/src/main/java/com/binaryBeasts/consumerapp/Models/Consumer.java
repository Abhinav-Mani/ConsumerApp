package com.binaryBeasts.consumerapp.Models;

import java.io.Serializable;

public class Consumer implements Serializable {
    String PhoneNumber;
    String Name;
    String Address;
    public Consumer(String phoneNumber, String name, String address) {
        PhoneNumber = phoneNumber;
        Name = name;
        Address = address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
