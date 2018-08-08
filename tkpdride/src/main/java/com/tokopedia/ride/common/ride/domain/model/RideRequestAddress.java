package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 5/8/17.
 */

public class RideRequestAddress implements Parcelable {
    private String startAddress;
    private String startAddressName;
    private String endAddress;
    private String endAddressName;

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getStartAddressName() {
        return startAddressName;
    }

    public void setStartAddressName(String startAddressName) {
        this.startAddressName = startAddressName;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getEndAddressName() {
        return endAddressName;
    }

    public void setEndAddressName(String endAddressName) {
        this.endAddressName = endAddressName;
    }

    public RideRequestAddress() {

    }

    protected RideRequestAddress(Parcel in) {
        startAddress = in.readString();
        startAddressName = in.readString();
        endAddress = in.readString();
        endAddressName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startAddress);
        dest.writeString(startAddressName);
        dest.writeString(endAddress);
        dest.writeString(endAddressName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RideRequestAddress> CREATOR = new Parcelable.Creator<RideRequestAddress>() {
        @Override
        public RideRequestAddress createFromParcel(Parcel in) {
            return new RideRequestAddress(in);
        }

        @Override
        public RideRequestAddress[] newArray(int size) {
            return new RideRequestAddress[size];
        }
    };
}
