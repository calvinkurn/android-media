
package com.tokopedia.flight.airport.data.source.cloud.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlightAirportAttributes {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone_code")
    @Expose
    private long phoneCode;
    @SerializedName("cities")
    @Expose
    private List<FlightAirportCity> cities = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(long phoneCode) {
        this.phoneCode = phoneCode;
    }

    public List<FlightAirportCity> getCities() {
        return cities;
    }

    public void setCities(List<FlightAirportCity> cities) {
        this.cities = cities;
    }

}
