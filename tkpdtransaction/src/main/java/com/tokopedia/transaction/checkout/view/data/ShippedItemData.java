package com.tokopedia.transaction.checkout.view.data;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShippedItemData {

    private String productWeight;
    private String productQty;
    private String productNotes;
    private String addressTitle;
    private String recipientName;
    private String address;


    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressReceiverName() {
        return recipientName;
    }

    public void setAddressReceiverName(String addressReceiverName) {
        this.recipientName = addressReceiverName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
