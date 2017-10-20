package com.tokopedia.transaction.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 8/25/17. Tokopedia
 */

public class OrderDriver implements Parcelable{

    @SerializedName("driver_name")
    @Expose
    private String driverName;

    @SerializedName("driver_phone")
    @Expose
    private String driverPhone;

    @SerializedName("driver_photo")
    @Expose
    private String driverPhoto;

    @SerializedName("license_number")
    @Expose
    private String licenseNumber;

    public OrderDriver() {
    }

    protected OrderDriver(Parcel in) {
        driverName = in.readString();
        driverPhone = in.readString();
        driverPhoto = in.readString();
        licenseNumber = in.readString();
    }

    public static final Creator<OrderDriver> CREATOR = new Creator<OrderDriver>() {
        @Override
        public OrderDriver createFromParcel(Parcel in) {
            return new OrderDriver(in);
        }

        @Override
        public OrderDriver[] newArray(int size) {
            return new OrderDriver[size];
        }
    };

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverPhoto() {
        return driverPhoto;
    }

    public void setDriverPhoto(String driverPhoto) {
        this.driverPhoto = driverPhoto;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(driverName);
        dest.writeString(driverPhone);
        dest.writeString(driverPhoto);
        dest.writeString(licenseNumber);
    }
}
