package com.tokopedia.home.explore.domain.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon.UseCaseIcon;
import java.util.List;

/**
 * Created by errysuprayogi on 2/2/18.
 */
public class DynamicHomeIcon {

    @SerializedName("useCaseIcon")
    private List<UseCaseIcon> useCaseIcons;
    @SerializedName("layoutSections")
    private List<LayoutSections> layoutSections;
    @SerializedName("favCategory")
    private List<LayoutRows> favCategory;

    public List<UseCaseIcon> getUseCaseIcons() {
        return useCaseIcons;
    }

    public void setUseCaseIcons(List<UseCaseIcon> useCaseIcons) {
        this.useCaseIcons = useCaseIcons;
    }

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
