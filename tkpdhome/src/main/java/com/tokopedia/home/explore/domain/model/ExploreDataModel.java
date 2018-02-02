package com.tokopedia.home.explore.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreDataModel {

    @SerializedName("dynamicHomeIcon")
    private DynamicHomeIcon dynamicHomeIcon;

    public DynamicHomeIcon getDynamicHomeIcon() {
        return dynamicHomeIcon;
    }

    public void setDynamicHomeIcon(DynamicHomeIcon dynamicHomeIcon) {
        this.dynamicHomeIcon = dynamicHomeIcon;
    }

}
