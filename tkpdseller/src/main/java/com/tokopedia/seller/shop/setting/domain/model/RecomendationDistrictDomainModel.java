package com.tokopedia.seller.shop.setting.domain.model;

import java.util.List;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class RecomendationDistrictDomainModel {
    private List<RecommendationDistrictItemDomainModel> items;
    private String stringTyped;

    public List<RecommendationDistrictItemDomainModel> getItems() {
        return items;
    }

    public void setItems(List<RecommendationDistrictItemDomainModel> items) {
        this.items = items;
    }

    public String getStringTyped() {
        return stringTyped;
    }

    public void setStringTyped(String stringTyped) {
        this.stringTyped = stringTyped;
    }
}
