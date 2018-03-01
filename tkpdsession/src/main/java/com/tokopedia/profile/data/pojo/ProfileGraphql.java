package com.tokopedia.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 20/02/18.
 */

public final class ProfileGraphql {
    @SerializedName("get_user_profile_data")
    @Expose
    private ProfileData profileData;
    @SerializedName("profile")
    @Expose
    private ProfileInfo profileInfo;
    @SerializedName("shopInfoByUserId")
    @Expose
    private ProfileShopInfo profileShopInfo;
    @SerializedName("reputationByUserId")
    @Expose
    private ProfileReputation profileReputation;

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }

    public ProfileShopInfo getProfileShopInfo() {
        return profileShopInfo;
    }

    public void setProfileShopInfo(ProfileShopInfo profileShopInfo) {
        this.profileShopInfo = profileShopInfo;
    }

    public ProfileReputation getProfileReputation() {
        return profileReputation;
    }

    public void setProfileReputation(ProfileReputation profileReputation) {
        this.profileReputation = profileReputation;
    }
}
