package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vishal on 4/4/17.
 */

public class LocationLatLng implements Parcelable {
    private double latitude;
    private double longitude;
    private float eta;

    public LocationLatLng() {
    }

    protected LocationLatLng(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        eta = in.readFloat();
    }

    public static final Creator<LocationLatLng> CREATOR = new Creator<LocationLatLng>() {
        @Override
        public LocationLatLng createFromParcel(Parcel in) {
            return new LocationLatLng(in);
        }

        @Override
        public LocationLatLng[] newArray(int size) {
            return new LocationLatLng[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getEta() {
        return eta;
    }

    public void setEta(float eta) {
        this.eta = eta;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeFloat(eta);
    }
}
