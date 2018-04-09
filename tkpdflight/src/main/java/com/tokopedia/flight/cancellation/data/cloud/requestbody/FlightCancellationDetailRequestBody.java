package com.tokopedia.flight.cancellation.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/9/18.
 */

public class FlightCancellationDetailRequestBody {
    @SerializedName("journey_id")
    @Expose
    private String journeyId;

    @SerializedName("passenger_id")
    @Expose
    private String passengerId;

    public FlightCancellationDetailRequestBody() {
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }
}
