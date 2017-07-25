package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 5/8/17.
 */

public class RideRequestAddressEntity {
    //    "start_address": "",
//            "start_address_name": "",
//            "end_address": "",
//            "end_address_name": ""
    @SerializedName("start_address")
    @Expose
    private String startAddress;
    @SerializedName("start_address_name")
    @Expose
    private String startAddressName;
    @SerializedName("end_address")
    @Expose
    private String endAddress;
    @SerializedName("end_address_name")
    @Expose
    private String endAddressName;

    public String getStartAddress() {
        return startAddress;
    }

    public String getStartAddressName() {
        return startAddressName;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public String getEndAddressName() {
        return endAddressName;
    }
}
