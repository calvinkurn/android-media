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

    @SerializedName("estimate")
    @Expose
    private EstimateEntity estimate;

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

    public EstimateEntity getEstimate() {
        return estimate;
    }

    public void setEstimate(EstimateEntity estimate) {
        this.estimate = estimate;
    }

    public void setPickupEstimate(String pickupEstimate) {
        this.pickupEstimate = pickupEstimate;
    }
}
