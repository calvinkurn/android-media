
package com.tokopedia.ride.bookingride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyRidesEntity {

    @SerializedName("Car")
    @Expose
    private List<RideLocationEntity> car = null;
    @SerializedName("Bike")
    @Expose
    private List<RideLocationEntity> bike = null;

    public List<RideLocationEntity> getCar() {
        return car;
    }

    public void setCar(List<RideLocationEntity> car) {
        this.car = car;
    }

    public List<RideLocationEntity> getBike() {
        return bike;
    }

    public void setBike(List<RideLocationEntity> bike) {
        this.bike = bike;
    }

}
