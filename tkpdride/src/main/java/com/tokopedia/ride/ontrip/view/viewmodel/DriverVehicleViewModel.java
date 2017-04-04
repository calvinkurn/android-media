package com.tokopedia.ride.ontrip.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

/**
 * Created by alvarisi on 4/4/17.
 */

public class DriverVehicleViewModel implements Parcelable {
    private Driver driver;
    private Vehicle vehicle;

    protected DriverVehicleViewModel(Parcel in) {
        driver = in.readParcelable(Driver.class.getClassLoader());
        vehicle = in.readParcelable(Vehicle.class.getClassLoader());
    }

    public static final Creator<DriverVehicleViewModel> CREATOR = new Creator<DriverVehicleViewModel>() {
        @Override
        public DriverVehicleViewModel createFromParcel(Parcel in) {
            return new DriverVehicleViewModel(in);
        }

        @Override
        public DriverVehicleViewModel[] newArray(int size) {
            return new DriverVehicleViewModel[size];
        }
    };

    public DriverVehicleViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(driver, i);
        parcel.writeParcelable(vehicle, i);
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
