package com.tokopedia.seller.product.edit.domain.model;

/**
 * Created by Hendry on 4/25/2017.
 */

public class AddProductShopInfoDomainModel {
    private boolean isGoldMerchant;
    private boolean isFreeReturn;
    private String shopId;
    private boolean officialStore;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public boolean isFreeReturn() {
        return isFreeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        isFreeReturn = freeReturn;
    }

    public void setOfficialStore(boolean officialStore) {
        this.officialStore = officialStore;
    }

    public boolean isOfficialStore() {
        return officialStore;
    }
}
