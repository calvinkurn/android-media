package com.tokopedia.seller.shop.setting.view.model;

import java.util.List;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class RecommendationDistrictViewModel {
    private List<RecommendationDistrictItemViewModel> items;
    private String stringTyped;

    public void setItems(List<RecommendationDistrictItemViewModel> items) {
        this.items = items;
    }

    public void setStringTyped(String stringTyped) {
        this.stringTyped = stringTyped;
    }

    public List<RecommendationDistrictItemViewModel> getItems() {
        return items;
    }

    public String getStringTyped() {
        return stringTyped;
    }
}
