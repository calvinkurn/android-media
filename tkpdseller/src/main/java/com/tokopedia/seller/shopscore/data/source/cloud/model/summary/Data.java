
package com.tokopedia.seller.shopscore.data.source.cloud.model.summary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("Data")
    @Expose
    private DetailData data;
    @SerializedName("BadgeScore")
    @Expose
    private Integer badgeScore;

    public DetailData getData() {
        return data;
    }

    public void setData(DetailData data) {
        this.data = data;
    }

    public Integer getBadgeScore() {
        return badgeScore;
    }

    public void setBadgeScore(Integer badgeScore) {
        this.badgeScore = badgeScore;
    }

}
