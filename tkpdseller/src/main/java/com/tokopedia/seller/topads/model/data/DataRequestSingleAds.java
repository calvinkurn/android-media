package com.tokopedia.seller.topads.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 12/14/16.
 */
public class DataRequestSingleAds {
    @SerializedName("ad_id")
    @Expose
    String adId;

    @SerializedName("group_id")
    @Expose
    String groupId;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
