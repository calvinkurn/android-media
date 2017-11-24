package com.tokopedia.seller.shop.setting.domain.model;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class RecommendationDistrictItemDomainModel {
    private int districtId;
    private String districtString;

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public void setDistrictString(String districtString) {
        this.districtString = districtString;
    }

    public int getDistrictId() {
        return districtId;
    }

    public String getDistrictString() {
        return districtString;
    }
}
