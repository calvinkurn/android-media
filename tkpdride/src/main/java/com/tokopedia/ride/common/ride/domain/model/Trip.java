package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by alvarisi on 3/21/17.
 */

public class Trip {
    String distanceUnit;
    int durationEstimate;
    String distanceEstimate;

    public Trip() {
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public int getDurationEstimate() {
        return durationEstimate;
    }

    public void setDurationEstimate(int durationEstimate) {
        this.durationEstimate = durationEstimate;
    }

    public String getDistanceEstimate() {
        return distanceEstimate;
    }

    public void setDistanceEstimate(String distanceEstimate) {
        this.distanceEstimate = distanceEstimate;
    }
}
