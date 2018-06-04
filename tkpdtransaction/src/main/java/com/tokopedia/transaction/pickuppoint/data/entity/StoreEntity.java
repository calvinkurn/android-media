package com.tokopedia.transaction.pickuppoint.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class StoreEntity {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("district_id")
    @Expose
    private int districtId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("geolocation")
    @Expose
    private String geolocation;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("store_code")
    @Expose
    private String storeCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }
}
