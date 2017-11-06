package com.tokopedia.transaction.purchase.detail.model;

import java.util.List;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailData {

    private String orderStatus;

    private String purchaseDate;

    private String responseTimeLimit;

    private String buyerName;

    private String courierName;

    private String shippingAddress;

    private String partialOrderStatus;

    private String invoiceNumber;

    private List<OrderDetailItemData> itemList;

    private String totalItemQuantity;

    private String productPrice;

    private String deliveryPrice;

    private String insurancePrice;

    private String additionalFee;

    private String totalPayment;

    private List<ButtonAttribute> buttonList;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getResponseTimeLimit() {
        return responseTimeLimit;
    }

    public void setResponseTimeLimit(String responseTimeLimit) {
        this.responseTimeLimit = responseTimeLimit;
    }

    public String getDescriptionBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String ShippingAddress) {
        this.shippingAddress = ShippingAddress;
    }

    public String getPartialOrderStatus() {
        return partialOrderStatus;
    }

    public void setPartialOrderStatus(String PartialOrderStatus) {
        this.partialOrderStatus = PartialOrderStatus;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public List<OrderDetailItemData> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderDetailItemData> itemList) {
        this.itemList = itemList;
    }

    public String getTotalItemQuantity() {
        return totalItemQuantity;
    }

    public void setTotalItemQuantity(String totalItemQuantity) {
        this.totalItemQuantity = totalItemQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(String insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(String additionalFee) {
        this.additionalFee = additionalFee;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public List<ButtonAttribute> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<ButtonAttribute> buttonList) {
        this.buttonList = buttonList;
    }
}
