package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/21/17.
 */

public class FareEstimateEntity {
    @SerializedName("fare")
    @Expose
    private FareEntity fare;
    @SerializedName("trip")
    @Expose
    private TripEntity trip;

    @SerializedName("pickup_estimate")
    @Expose
    private String pickupEstimate;

    public FareEstimateEntity() {
    }

    public FareEntity getFare() {
        return fare;
    }

    public void setFare(FareEntity fare) {
        this.fare = fare;
    }

    public TripEntity getTrip() {
        return trip;
    }

    public void setTrip(TripEntity trip) {
        this.trip = trip;
    }

    public String getPickupEstimate() {
        return pickupEstimate;
    }

    public void setPickupEstimate(String pickupEstimate) {
        this.pickupEstimate = pickupEstimate;
    }
}
