package com.tokopedia.home.explore.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class DataResponseModel {

    @SerializedName("dynamicHomeIcon")
    private DynamicHomeIcon dynamicHomeIcon;

    public DynamicHomeIcon getDynamicHomeIcon() {
        return dynamicHomeIcon;
    }

    public void setDynamicHomeIcon(DynamicHomeIcon dynamicHomeIcon) {
        this.dynamicHomeIcon = dynamicHomeIcon;
    }
}
