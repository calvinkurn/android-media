package com.tokopedia.seller.shop.setting.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class ResponseIsReserveDomain {

    @SerializedName("reserve_status")
    @Expose
    private String reserveStatus = null;

    @SerializedName("user_data")
    @Expose
    private ResponseUserData responseUserData = null;

    public String getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(String reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    public ResponseUserData getResponseUserData() {
        return responseUserData;
    }

    public void setResponseUserData(ResponseUserData responseUserData) {
        this.responseUserData = responseUserData;
    }
}
