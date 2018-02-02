package com.tokopedia.home.explore.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by errysuprayogi on 2/2/18.
 */
public class DynamicHomeIcon {
    @SerializedName("layoutSections")
    private List<LayoutSections> layoutSections;

    public List<LayoutSections> getLayoutSections() {
        return layoutSections;
    }

    public void setLayoutSections(List<LayoutSections> layoutSections) {
        this.layoutSections = layoutSections;
    }

}
