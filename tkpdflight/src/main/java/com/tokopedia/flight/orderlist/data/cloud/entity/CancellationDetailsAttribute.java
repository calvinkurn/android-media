package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 30/04/18.
 */

public class CancellationDetailsAttribute {
    @SerializedName("journey_id")
    @Expose
    private int journeyId;
    @SerializedName("passenger_id")
    @Expose
    private int passengerId;

    public int getJourneyId() {
        return journeyId;
    }

    public int getPassengerId() {
        return passengerId;
    }
}
