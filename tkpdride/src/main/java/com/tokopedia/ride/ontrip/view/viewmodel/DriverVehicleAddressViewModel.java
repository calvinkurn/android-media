package com.tokopedia.ride.ontrip.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.RideRequestAddress;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

/**
 * Created by alvarisi on 4/4/17.
 */

public class DriverVehicleAddressViewModel implements Parcelable {
    private Driver driver;
    private Vehicle vehicle;
    private RideRequestAddress address;

    protected DriverVehicleAddressViewModel(Parcel in) {
        driver = in.readParcelable(Driver.class.getClassLoader());
        vehicle = in.readParcelable(Vehicle.class.getClassLoader());
        address = in.readParcelable(Vehicle.class.getClassLoader());
    }

    public static final Creator<DriverVehicleAddressViewModel> CREATOR = new Creator<DriverVehicleAddressViewModel>() {
        @Override
        public DriverVehicleAddressViewModel createFromParcel(Parcel in) {
            return new DriverVehicleAddressViewModel(in);
        }

        @Override
        public DriverVehicleAddressViewModel[] newArray(int size) {
            return new DriverVehicleAddressViewModel[size];
        }
    };

    public DriverVehicleAddressViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(driver, i);
        parcel.writeParcelable(vehicle, i);
        parcel.writeParcelable(address, i);
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

    public RideRequestAddress getAddress() {
        return address;
    }

    public void setAddress(RideRequestAddress address) {
        this.address = address;
    }
}
