package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by alvarisi on 3/21/17.
 */

public class FareEstimate {
    private Fare fare;
    private Trip trip;
    private String pickupEstimate;

    public FareEstimate() {
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getPickupEstimate() {
        return pickupEstimate;
    }

    public void setPickupEstimate(String pickupEstimate) {
        this.pickupEstimate = pickupEstimate;
    }
}
