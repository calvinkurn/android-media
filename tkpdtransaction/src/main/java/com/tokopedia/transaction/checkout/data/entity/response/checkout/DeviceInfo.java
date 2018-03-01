package com.tokopedia.transaction.checkout.data.entity.response.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class DeviceInfo {
    @SerializedName("device_time")
    @Expose
    private String deviceTime;
    @SerializedName("device_version")
    @Expose
    private String deviceVersion;

    public String getDeviceTime() {
        return deviceTime;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }
}
