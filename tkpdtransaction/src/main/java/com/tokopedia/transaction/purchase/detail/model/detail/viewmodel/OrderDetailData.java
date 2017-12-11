package com.tokopedia.transaction.purchase.detail.model.detail.viewmodel;

import java.util.List;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailData {

    private String orderId;

    private String orderCode;

    private String orderStatus;

    private String resoId;

    private String orderImage;

    private String purchaseDate;

    private String responseTimeLimit;

    private String deadlineColorString;

    private String shopId;

    private String shopName;

    private String shopLogo;

    private String buyerName;

    private String courierName;

    private String shippingAddress;

    private String partialOrderStatus;

    private String preorderPeriod;

    private String preorderPeriodText;

    private boolean isPreorder;

    private String dropshipperName;

    private String dropshipperPhone;

    private String invoiceNumber;

    private String invoiceUrl;

    private List<OrderDetailItemData> itemList;

    private String totalItemQuantity;

    private String productPrice;

    private String deliveryPrice;

    private String insurancePrice;

    private String additionalFee;

    private String totalPayment;

    private ButtonData buttonData;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getResoId() {
        return resoId;
    }

    public void setResoId(String resoId) {
        this.resoId = resoId;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
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

    public String getDeadlineColorString() {
        return deadlineColorString;
    }

    public void setDeadlineColorString(String deadlineColorString) {
        this.deadlineColorString = deadlineColorString;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getBuyerName() {
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

    public boolean isPreorder() {
        return isPreorder;
    }

    public void setPreorder(boolean preorder) {
        isPreorder = preorder;
    }

    public String getPreorderPeriod() {
        return preorderPeriod;
    }

    public void setPreorderPeriod(String preorderPeriod) {
        this.preorderPeriod = preorderPeriod;
    }

    public String getPreorderPeriodText() {
        return preorderPeriodText;
    }

    public void setPreorderPeriodText(String preorderPeriodText) {
        this.preorderPeriodText = preorderPeriodText;
    }

    public String getDropshipperName() {
        return dropshipperName;
    }

    public void setDropshipperName(String dropshipperName) {
        this.dropshipperName = dropshipperName;
    }

    public String getDropshipperPhone() {
        return dropshipperPhone;
    }

    public void setDropshipperPhone(String dropshipperPhone) {
        this.dropshipperPhone = dropshipperPhone;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
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

    public ButtonData getButtonData() {
        return buttonData;
    }

    public void setButtonData(ButtonData buttonData) {
        this.buttonData = buttonData;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
