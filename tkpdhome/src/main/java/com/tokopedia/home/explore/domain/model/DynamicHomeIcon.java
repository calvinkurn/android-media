package com.tokopedia.home.explore.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by errysuprayogi on 2/2/18.
 */
public class DynamicHomeIcon {
    @SerializedName("layoutSections")
    private List<LayoutSections> layoutSections;
    @SerializedName("favCategory")
    private List<LayoutRows> favCategory;

    public List<LayoutSections> getLayoutSections() {
        return layoutSections;
    }

    public void setLayoutSections(List<LayoutSections> layoutSections) {
        this.layoutSections = layoutSections;
    }

    public List<LayoutRows> getFavCategory() {
        return favCategory;
    }

    public void setFavCategory(List<LayoutRows> favCategory) {
        this.favCategory = favCategory;
    }
}
