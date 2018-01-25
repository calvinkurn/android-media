
package com.tokopedia.flight.airport.data.source.cloud.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlightAirportCity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("airports")
    @Expose
    private List<FlightAirportDetail> flightAirportDetails = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FlightAirportDetail> getFlightAirportDetails() {
        return flightAirportDetails;
    }

    public void setFlightAirportDetails(List<FlightAirportDetail> flightAirportDetails) {
        this.flightAirportDetails = flightAirportDetails;
    }

}
