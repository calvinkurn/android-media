package com.tokopedia.transaction.checkout.view.data;

import java.util.List;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressAdapterData {

    private String senderName;

    private String productImageUrl;

    private String productName;

    private String productPrice;

    private List<MultipleAddressItemData> itemListData;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
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

    public List<MultipleAddressItemData> getItemListData() {
        return itemListData;
    }

    public void setItemListData(List<MultipleAddressItemData> itemListData) {
        this.itemListData = itemListData;
    }
}
