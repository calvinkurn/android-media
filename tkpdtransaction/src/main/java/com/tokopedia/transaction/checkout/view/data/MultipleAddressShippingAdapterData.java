package com.tokopedia.transaction.checkout.view.data;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressShippingAdapterData {

    private String senderName;

    private MultipleAddressItemData itemData;

    private String courier;

    private String subTotalAmount;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public MultipleAddressItemData getItemData() {
        return itemData;
    }

    public void setItemData(MultipleAddressItemData itemData) {
        this.itemData = itemData;
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
