package com.tokopedia.transaction.checkout.view.data;

import java.util.List;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressAdapterData {

    private String senderName;

    private List<MultipleAddressItemData> itemListData;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public List<MultipleAddressItemData> getItemListData() {
        return itemListData;
    }

    public void setItemListData(List<MultipleAddressItemData> itemListData) {
        this.itemListData = itemListData;
    }
}
