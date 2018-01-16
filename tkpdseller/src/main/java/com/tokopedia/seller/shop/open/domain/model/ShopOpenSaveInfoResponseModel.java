package com.tokopedia.seller.shop.open.domain.model;

/**
 * Created by zulfikarrahman on 1/9/18.
 */

public class ShopOpenSaveInfoResponseModel {
    private Boolean isSaveShopSuccess;
    private String picSrc;
    private String shopDesc;
    private String shopTagLine;

    public void setIsSaveShopSuccess(Boolean isSaveShopSuccess) {
        this.isSaveShopSuccess = isSaveShopSuccess;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public void setShopTagLine(String shopTagLine) {
        this.shopTagLine = shopTagLine;
    }

    public Boolean isSaveShopSuccess() {
        return isSaveShopSuccess;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public String getShopTagLine() {
        return shopTagLine;
    }
}
