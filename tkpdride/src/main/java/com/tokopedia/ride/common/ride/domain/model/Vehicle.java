package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/24/17.
 */

public class Vehicle implements Parcelable {
    private String make;
    String vehicleModel;
    private String licensePlate;
    private String pictureUrl;

    public Vehicle() {
    }

    protected Vehicle(Parcel in) {
        make = in.readString();
        vehicleModel = in.readString();
        licensePlate = in.readString();
        pictureUrl = in.readString();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(make);
        parcel.writeString(vehicleModel);
        parcel.writeString(licensePlate);
        parcel.writeString(pictureUrl);
    }
}
