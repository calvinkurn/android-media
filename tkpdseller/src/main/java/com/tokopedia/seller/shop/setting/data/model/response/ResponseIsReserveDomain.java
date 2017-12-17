package com.tokopedia.seller.shop.setting.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class ResponseIsReserveDomain {

    @SerializedName("reserve_status")
    @Expose
    private int reserveStatus;

    @SerializedName("user_data")
    @Expose
    private ResponseUserData responseUserData = null;

    public boolean isDomainAlreadyReserved(){
        return reserveStatus!= 0;
    }

    public ResponseUserData getResponseUserData() {
        return responseUserData;
    }

}
