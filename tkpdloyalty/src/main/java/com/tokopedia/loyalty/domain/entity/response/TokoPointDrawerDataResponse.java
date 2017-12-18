package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 04/12/17.
 */

public class TokoPointDrawerDataResponse {

    @SerializedName("off_flag")
    @Expose
    private int offFlag;
    @SerializedName("has_notif")
    @Expose
    private int hasNotif;
    @SerializedName("user_tier")
    @Expose
    private UserTier userTier;
    @SerializedName("pop_up_notif")
    @Expose
    private PopUpNotif popUpNotif;
    @SerializedName("mainpage_url")
    @Expose
    private String mainPageUrl;
    @SerializedName("mainpage_title")
    @Expose
    private String mainPageTitle;

    public int getOffFlag() {
        return offFlag;
    }

    public int getHasNotif() {
        return hasNotif;
    }

    public UserTier getUserTier() {
        return userTier;
    }

    public PopUpNotif getPopUpNotif() {
        return popUpNotif;
    }

    public String getMainPageUrl() {
        return mainPageUrl;
    }

    public String getMainPageTitle() {
        return mainPageTitle;
    }
}
