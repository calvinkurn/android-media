package com.tokopedia.transaction.checkout.view.data;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class SingleAddressRecipientAddressModel {

    private String recipientName;
    private String recipientAddress;

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }
}
