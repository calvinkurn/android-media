package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/27/17.
 */

public class LocationLatLngEntity {
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("eta")
    @Expose
    private float eta;
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("address_name")
    @Expose
    private String addressName;

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

    public String getAddressName() {
        return addressName;
    }
}
