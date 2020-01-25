package com.binaryBeasts.consumerapp.Models;

import com.google.firebase.database.annotations.NotNull;

public class DeliveryRequests {
    String FarmersNumber,FarmersAddress,CustomerPhoneNo, CustomerAddress,Amount;

    public DeliveryRequests(@NotNull String farmersNumber,@NotNull String farmersAddress,@NotNull String customerPhoneNo,@NotNull String customerAddress,@NotNull String amount) {
        FarmersNumber = farmersNumber;
        FarmersAddress = farmersAddress;
        CustomerPhoneNo = customerPhoneNo;
        CustomerAddress = customerAddress;
        Amount = amount;
    }

    public String getFarmersNumber() {
        return FarmersNumber;
    }

    public void setFarmersNumber(String farmersNumber) {
        FarmersNumber = farmersNumber;
    }

    public String getFarmersAddress() {
        return FarmersAddress;
    }

    public void setFarmersAddress(String farmersAddress) {
        FarmersAddress = farmersAddress;
    }

    public String getCustomerPhoneNo() {
        return CustomerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        CustomerPhoneNo = customerPhoneNo;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
