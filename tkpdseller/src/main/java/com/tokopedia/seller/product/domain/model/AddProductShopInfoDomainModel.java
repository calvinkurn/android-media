package com.tokopedia.seller.product.domain.model;

/**
 * Created by Hendry on 4/25/2017.
 */

public class AddProductShopInfoDomainModel {
    private boolean isGoldMerchant;
    private boolean isFreeReturn;

    public AddProductShopInfoDomainModel(boolean isGoldMerchant, boolean isFreeReturn) {
        this.isGoldMerchant = isGoldMerchant;
        this.isFreeReturn = isFreeReturn;
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
}
