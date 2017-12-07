package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by alvarisi on 12/6/17.
 */

public class FlightEntity {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("total_adult")
    @Expose
    private String totalAdult;
    @SerializedName("total_child")
    @Expose
    private String totalChild;
    @SerializedName("total_infant")
    @Expose
    private String totalInfant;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("journeys")
    @Expose
    private List<JourneyEntity> journeys;
    @SerializedName("passengers")
    @Expose
    private List<PassengerEntity> passengers;

    public FlightEntity() {
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getTotalAdult() {
        return totalAdult;
    }

    public String getTotalChild() {
        return totalChild;
    }

    public String getTotalInfant() {
        return totalInfant;
    }

    public String getCurrency() {
        return currency;
    }

    public List<JourneyEntity> getJourneys() {
        return journeys;
    }

    public List<PassengerEntity> getPassengers() {
        return passengers;
    }
}
