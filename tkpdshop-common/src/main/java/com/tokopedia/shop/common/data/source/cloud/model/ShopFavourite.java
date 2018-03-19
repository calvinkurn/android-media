
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopFavourite {

    @SerializedName("is_success")
    @Expose
    private String isSuccess;

    public String getIsSuccess() {
        return isSuccess;
    }
}
