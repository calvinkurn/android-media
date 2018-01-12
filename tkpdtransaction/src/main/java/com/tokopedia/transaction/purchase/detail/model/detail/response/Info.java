package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 12/01/18.
 */

public class Info {
    @SerializedName("pickup_info")
    @Expose
    private PickupInfo pickupInfo;

    public Info() {
    }

    public PickupInfo getPickupInfo() {
        return pickupInfo;
    }

    public void setPickupInfo(PickupInfo pickupInfo) {
        this.pickupInfo = pickupInfo;
    }
}
