package com.tokopedia.seller.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 09/01/18.
 */

public class Info {
    @SerializedName("driver")
    @Expose
    private Driver driver;
    @SerializedName("pickup_info")
    @Expose
    private PickupInfo pickupInfo;
    @SerializedName("online_booking")
    @Expose
    private OnlineBooking onlineBooking;

    public Info() {
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public PickupInfo getPickupInfo() {
        return pickupInfo;
    }

    public void setPickupInfo(PickupInfo pickupInfo) {
        this.pickupInfo = pickupInfo;
    }

    public OnlineBooking getOnlineBooking() {
        return onlineBooking;
    }

    public void setOnlineBooking(OnlineBooking onlineBooking) {
        this.onlineBooking = onlineBooking;
    }
}