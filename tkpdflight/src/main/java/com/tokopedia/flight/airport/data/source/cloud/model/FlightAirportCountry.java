
package com.tokopedia.flight.airport.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlightAirportCountry {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private FlightAirportAttributes attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FlightAirportAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(FlightAirportAttributes attributes) {
        this.attributes = attributes;
    }

}
