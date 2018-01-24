package com.tokopedia.transaction.pickuppoint.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class Store implements Parcelable {
    private int id;
    private int districtId;
    private String address;
    private String geolocation;
    private String storeName;
    private String storeCode;

    public Store() {
    }

    protected Store(Parcel in) {
        id = in.readInt();
        districtId = in.readInt();
        address = in.readString();
        geolocation = in.readString();
        storeName = in.readString();
        storeCode = in.readString();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(districtId);
        dest.writeString(address);
        dest.writeString(geolocation);
        dest.writeString(storeName);
        dest.writeString(storeCode);
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", districtId=" + districtId +
                ", address='" + address + '\'' +
                ", geolocation='" + geolocation + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeCode='" + storeCode + '\'' +
                '}';
    }
}
