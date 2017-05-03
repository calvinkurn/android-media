package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/27/17.
 */

public class LocationLatLngEntity {
    @SerializedName("latitude")
    @Expose
    double latitude;
    @SerializedName("longitude")
    @Expose
    double longitude;
    @SerializedName("eta")
    @Expose
    float eta;
    @SerializedName("address")
    @Expose
    String address;

    public LocationLatLngEntity() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getEta() {
        return eta;
    }

    public void setEta(float eta) {
        this.eta = eta;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
