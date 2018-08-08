package com.tokopedia.transaction.cart.model.savelocation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by zeta on 11/3/2016.
 */

public class LocationData implements Parcelable {
    private String latitude;
    private String longitude;
    private String manualAddress;
    private String generatedAddress;

    public LocationData() {
    }

    protected LocationData(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
        manualAddress = in.readString();
        generatedAddress = in.readString();
    }

    public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
        @Override
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getManualAddress() {
        return manualAddress;
    }

    public void setManualAddress(String manualAddress) {
        this.manualAddress = manualAddress;
    }

    public String getGeneratedAddress() {
        return generatedAddress;
    }

    public void setGeneratedAddress(String generatedAddress) {
        this.generatedAddress = generatedAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(manualAddress);
        parcel.writeString(generatedAddress);
    }
}
