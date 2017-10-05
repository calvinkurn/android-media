package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 5/2/17.
 */

public class RideAddressEntity {
//    {
//        "addr_id": 4645924,
//            "addr_name": "khusus gojek",
//            "latitude": "-6.189771661393986",
//            "longitude": "106.79892852902412"
//    }

    @SerializedName("addr_id")
    @Expose
    private String addressId;
    @SerializedName("addr_name")
    @Expose
    private String addressName;
    @SerializedName("description")
    @Expose
    private String addressDescription;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("prefill")
    @Expose
    private boolean prefill;

    public RideAddressEntity() {
    }

    public String getAddressId() {
        return addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAddressDescription() {
        return addressDescription;
    }

    public boolean getPrefill() {
        return prefill;
    }
}
