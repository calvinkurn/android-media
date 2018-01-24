package com.tokopedia.transaction.checkout.view.data;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressItemData {

    private String productWeight;

    private String productQty;

    private String productNotes;

    private String addressTitle;

    private String addressReceiverName;

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
        return addressReceiverName;
    }

    public void setAddressReceiverName(String addressReceiverName) {
        this.addressReceiverName = addressReceiverName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
