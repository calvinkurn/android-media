package com.tokopedia.flight.booking.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.search.data.cloud.model.response.Fare;

/**
 * @author by alvarisi on 11/15/17.
 */

public class NewFarePrice {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fare")
    @Expose
    private Fare fare;

    public NewFarePrice() {
    }

    public NewFarePrice(String id, Fare fare) {
        this.id = id;
        this.fare = fare;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }
}
