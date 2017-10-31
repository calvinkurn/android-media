package com.tokopedia.flight.search.data.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

import java.util.List;

/**
 * Created by User on 10/26/2017.
 */

public class Route implements ItemType {
    public static final int TYPE = 893;
    @SerializedName("airline")
    @Expose
    private String airline;
    @SerializedName("departure_airport")
    @Expose
    private String departureAirport;
    @SerializedName("departure_timestamp")
    @Expose
    private String departureTimestamp;
    @SerializedName("arrival_airport")
    @Expose
    private String arrivalAirport;
    @SerializedName("arrival_timestamp")
    @Expose
    private String arrivalTimestamp;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("layover")
    @Expose
    private String layover;
    @SerializedName("infos")
    @Expose
    private List<Info> infos = null;
    @SerializedName("flight_number")
    @Expose
    private String flightNumber;
    @SerializedName("is_refundable")
    @Expose
    private boolean isRefundable;
    @SerializedName("amenities")
    @Expose
    private List<Amenity> amenities = null;

    private String airlineName;
    private String airlineLogo;

    public String getAirline() {
        return airline;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public String getDuration() {
        return duration;
    }

    public String getLayover() {
        return layover;
    }

    public List<Info> getInfos() {
        return infos;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Boolean getRefundable() {
        return isRefundable;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineLogo() {
        return airlineLogo;
    }

    public void setAirlineLogo(String airlineLogo) {
        this.airlineLogo = airlineLogo;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
