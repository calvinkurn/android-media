package com.tokopedia.topads.dashboard.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 2/28/17.
 */

public class AdCreateGroupRequest {
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("ad_type")
    @Expose
    private String adType;
    @SerializedName("ad_id")
    @Expose
    private String adId;
    @SerializedName("group_id")
    @Expose
    private String groupId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

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
