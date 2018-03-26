package com.tokopedia.session.register.registerphonenumber.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckMsisdnResponse {

    @SerializedName("isExist")
    private boolean isExist;

    @SerializedName("phone_view")
    private String phoneView;

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public String getPhoneView() {
        return phoneView;
    }

    public void setPhoneView(String phoneView) {
        this.phoneView = phoneView;
    }
}
