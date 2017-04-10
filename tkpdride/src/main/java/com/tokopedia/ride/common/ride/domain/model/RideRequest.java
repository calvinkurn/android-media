package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/24/17.
 */

public class RideRequest implements Parcelable {
    String requestId;
    String productId;
    String status;
    Vehicle vehicle;
    Driver driver;
    Location location;
    LocationLatLng pickup;
    LocationLatLng destination;
    double surgeMultiplier;
    boolean shared;

    public RideRequest() {
    }

    protected RideRequest(Parcel in) {
        requestId = in.readString();
        productId = in.readString();
        status = in.readString();
        vehicle = in.readParcelable(Vehicle.class.getClassLoader());
        driver = in.readParcelable(Driver.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());
        pickup = in.readParcelable(LocationLatLng.class.getClassLoader());
        destination = in.readParcelable(LocationLatLng.class.getClassLoader());
        surgeMultiplier = in.readDouble();
        shared = in.readByte() != 0;
    }

    public static final Creator<RideRequest> CREATOR = new Creator<RideRequest>() {
        @Override
        public RideRequest createFromParcel(Parcel in) {
            return new RideRequest(in);
        }

        @Override
        public RideRequest[] newArray(int size) {
            return new RideRequest[size];
        }
    };

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public double getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(double surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    public LocationLatLng getPickup() {
        return pickup;
    }

    public void setPickup(LocationLatLng pickup) {
        this.pickup = pickup;
    }

    public LocationLatLng getDestination() {
        return destination;
    }

    public void setDestination(LocationLatLng destination) {
        this.destination = destination;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(requestId);
        parcel.writeString(productId);
        parcel.writeString(status);
        parcel.writeParcelable(vehicle, i);
        parcel.writeParcelable(driver, i);
        parcel.writeParcelable(location, i);
        parcel.writeParcelable(pickup, i);
        parcel.writeParcelable(destination, i);
        parcel.writeDouble(surgeMultiplier);
        parcel.writeByte((byte) (shared ? 1 : 0));
    }
}
