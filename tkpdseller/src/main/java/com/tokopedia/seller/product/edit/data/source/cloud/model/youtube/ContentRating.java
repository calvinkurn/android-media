package com.tokopedia.seller.product.edit.data.source.cloud.model.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentRating {

    @SerializedName("ytRating")
    @Expose
    private String ytRating;

    public String getYtRating() {
        return ytRating;
    }

    public void setYtRating(String ytRating) {
        this.ytRating = ytRating;
    }

}
