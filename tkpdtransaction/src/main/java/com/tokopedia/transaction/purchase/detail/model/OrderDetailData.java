package com.tokopedia.transaction.purchase.detail.model;

import java.util.List;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailData {

    private String orderStatus;

    private String descriptionDate;

    private String descriptionResponseTime;

    private String descriptionBuyerName;

    private String descriptionCourierName;

    private String descriptionShippingAddress;

    private String descriptionPartialOrderStatus;

    private String invoiceNumber;

    private List<OrderDetailItemData> itemList;

    private String itemAmount;

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

    public String getDescriptionDate() {
        return descriptionDate;
    }

    public void setDescriptionDate(String descriptionDate) {
        this.descriptionDate = descriptionDate;
    }

    public String getDescriptionResponseTime() {
        return descriptionResponseTime;
    }

    public void setDescriptionResponseTime(String descriptionResponseTime) {
        this.descriptionResponseTime = descriptionResponseTime;
    }

    public String getDescriptionBuyerName() {
        return descriptionBuyerName;
    }

    public void setDescriptionBuyerName(String descriptionBuyerName) {
        this.descriptionBuyerName = descriptionBuyerName;
    }

    public String getDescriptionCourierName() {
        return descriptionCourierName;
    }

    public void setDescriptionCourierName(String descriptionCourierName) {
        this.descriptionCourierName = descriptionCourierName;
    }

    public String getDescriptionShippingAddress() {
        return descriptionShippingAddress;
    }

    public void setDescriptionShippingAddress(String descriptionShippingAddress) {
        this.descriptionShippingAddress = descriptionShippingAddress;
    }

    public String getDescriptionPartialOrderStatus() {
        return descriptionPartialOrderStatus;
    }

    public void setDescriptionPartialOrderStatus(String descriptionPartialOrderStatus) {
        this.descriptionPartialOrderStatus = descriptionPartialOrderStatus;
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

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
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
