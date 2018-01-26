package com.tokopedia.transaction.checkout.view.data;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class SingleAddressShippedItemModel {

    private String senderName;
    private String productName;
    private String productPrice;
    private String productWeight;
    private String cashback;
    private String totalProductItem;
    private String noteToSeller;
    private String shipmentOption;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getTotalProductItem() {
        return totalProductItem;
    }

    public void setTotalProductItem(String totalProductItem) {
        this.totalProductItem = totalProductItem;
    }

    public String getNoteToSeller() {
        return noteToSeller;
    }

    public void setNoteToSeller(String noteToSeller) {
        this.noteToSeller = noteToSeller;
    }

    public String getShipmentOption() {
        return shipmentOption;
    }

    public void setShipmentOption(String shipmentOption) {
        this.shipmentOption = shipmentOption;
    }
}
