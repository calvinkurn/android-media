package com.tokopedia.transaction.checkout.view.data;

/**
 * @author Aghny A. Putra on 24/01/18
 */

public class SingleAddressShippedItemData {

    private String senderName;
    private String productImageUrl;
    private String productPrice;
    private String productName;
    private ShippedItemData itemData;
    private String courier;
    private String subTotalAmount;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public ShippedItemData getItemData() {
        return itemData;
    }

    public void setItemData(ShippedItemData itemData) {
        this.itemData = itemData;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(String subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }
}
