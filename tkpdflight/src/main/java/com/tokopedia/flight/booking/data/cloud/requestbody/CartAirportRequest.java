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
    @SerializedName("departure")
    @Expose
    private String departure;
    @SerializedName("arrival")
    @Expose
    private String arrival;
    @SerializedName("date")
    @Expose
    private String date;

    public CartAirportRequest() {
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
