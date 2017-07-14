package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/21/17.
 */

public class TripEntity {
    @SerializedName("distance_unit")
    @Expose
    String distanceUnit;
    @SerializedName("duration_estimate")
    @Expose
    int durationEstimate;
    @SerializedName("distance_estimate")
    @Expose
    String distanceEstimate;

    public TripEntity() {
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
