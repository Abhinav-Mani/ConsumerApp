package com.binaryBeasts.consumerapp.Models;

public class MyOrderModel {
    String Date,Status,DeliveryCost,ProductCost,ProductId,ProductName,Img;

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public MyOrderModel(OrderRequest orderRequest){
        DeliveryCost=orderRequest.getDeliverPrice();
        ProductCost=orderRequest.getProductPrice();
    }

    public MyOrderModel(String date, String status, String deliveryCost, String productCost,String productId) {
        ProductId=productId;
        Date = date;
        Status = status;
        DeliveryCost = deliveryCost;
        ProductCost = productCost;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDeliveryCost() {
        return DeliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        DeliveryCost = deliveryCost;
    }

    public String getProductCost() {
        return ProductCost;
    }

    public void setProductCost(String productCost) {
        ProductCost = productCost;
    }
}
