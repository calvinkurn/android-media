package com.tokopedia.flight.booking.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 11/14/17.
 */

public class CartAirportRequest {
    @SerializedName("journey_id")
    @Expose
    private String journeyId;
    @SerializedName("term")
    @Expose
    private String term;

    public CartAirportRequest() {
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
