package com.tokopedia.seller.shop.setting.view.model;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public class ShopSettingLocationModel {
    private int districtCode;
    private int postalCode;

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public int getDistrictCode() {
        return districtCode;
    }
}
