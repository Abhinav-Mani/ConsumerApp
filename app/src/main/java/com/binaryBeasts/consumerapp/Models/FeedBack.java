package com.binaryBeasts.consumerapp.Models;

public class FeedBack {
    String CustomerNumber,FarmerNumber,DeliveryPersonNumer,FarmerReview,DeliverReview;

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getFarmerReview() {
        return FarmerReview;
    }

    public void setFarmerReview(String farmerReview) {
        FarmerReview = farmerReview;
    }

    public String getDeliverReview() {
        return DeliverReview;
    }

    public void setDeliverReview(String deliverReview) {
        DeliverReview = deliverReview;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getFarmerNumber() {
        return FarmerNumber;
    }

    public void setFarmerNumber(String farmerNumber) {
        FarmerNumber = farmerNumber;
    }

    public String getDeliveryPersonNumer() {
        return DeliveryPersonNumer;
    }

    public void setDeliveryPersonNumer(String deliveryPersonNumer) {
        DeliveryPersonNumer = deliveryPersonNumer;
    }

    public FeedBack(String customerNumber, String farmerNumber, String deliveryPersonNumer, String farmerReview, String deliverReview) {
        CustomerNumber = customerNumber;
        FarmerNumber = farmerNumber;
        DeliveryPersonNumer = deliveryPersonNumer;
        FarmerReview = farmerReview;
        DeliverReview = deliverReview;
    }
}
