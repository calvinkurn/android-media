package com.tokopedia.flight.cancellation.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/9/18.
 */

public class FlightCancellationDetailRequestBody {
    @SerializedName("journey_id")
    @Expose
    private long journeyId;

    @SerializedName("passenger_id")
    @Expose
    private long passengerId;

    public FlightCancellationDetailRequestBody() {
    }

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(long passengerId) {
        this.passengerId = passengerId;
    }
}
