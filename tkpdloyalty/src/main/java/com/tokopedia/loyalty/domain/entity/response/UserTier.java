package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 04/12/17.
 */

public class UserTier {

    @SerializedName("tier_id")
    @Expose
    private int tierId;
    @SerializedName("tier_name")
    @Expose
    private String tierName;
    @SerializedName("tier_image_url")
    @Expose
    private String tierImageUrl;
    @SerializedName("reward_points")
    @Expose
    private int rewardPoints;

    public int getTierId() {
        return tierId;
    }

    public String getTierName() {
        return tierName;
    }

    public String getTierImageUrl() {
        return tierImageUrl;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }
}
