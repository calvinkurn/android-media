package com.tokopedia.flight.search.data.cloud.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 10/26/2017.
 */

public class Attributes {
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("aid")
    @Expose
    private String aid;
    @SerializedName("departure_airport")
    @Expose
    private String departureAirport;
    @SerializedName("departure_time")
    @Expose
    private String departureTime;
    @SerializedName("departure_time_int")
    @Expose
    private int departureTimeInt;
    @SerializedName("arrival_airport")
    @Expose
    private String arrivalAirport;
    @SerializedName("arrival_time")
    @Expose
    private String arrivalTime;
    @SerializedName("arrival_time_int")
    @Expose
    private int arrivalTimeInt;
    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;
    @SerializedName("total_transit")
    @Expose
    private int totalTransit;
    @SerializedName("add_day_arrival")
    @Expose
    private int addDayArrival;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("duration_minute")
    @Expose
    private int durationMinute;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("total_numeric")
    @Expose
    private int totalNumeric;
    @SerializedName("before_total")
    @Expose
    private String beforeTotal;
    @SerializedName("fare")
    @Expose
    private Fare fare;

    public String getTerm() {
        return term;
    }

    public String getAid() {
        return aid;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getDepartureTimeInt() {
        return departureTimeInt;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getArrivalTimeInt() {
        return arrivalTimeInt;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public int getTotalTransit() {
        return totalTransit;
    }

    public int getAddDayArrival() {
        return addDayArrival;
    }

    public String getDuration() {
        return duration;
    }

    public int getDurationMinute() {
        return durationMinute;
    }

    public String getTotal() {
        return total;
    }

    public int getTotalNumeric() {
        return totalNumeric;
    }

    public String getBeforeTotal() {
        return beforeTotal;
    }

    public Fare getFare() {
        return fare;
    }
}
