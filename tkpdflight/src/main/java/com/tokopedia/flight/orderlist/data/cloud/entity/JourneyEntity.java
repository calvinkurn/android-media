package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 12/6/17.
 */

public class JourneyEntity {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("departure_id")
    @Expose
    private String departureAirportId;
    @SerializedName("departure_time")
    @Expose
    private String departureTime;
    @SerializedName("arrival_id")
    @Expose
    private String arrivalAirportId;
    @SerializedName("arrival_time")
    @Expose
    private String arrivalTime;
    @SerializedName("routes")
    @Expose
    private List<RouteEntity> routes;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("add_day_arrival")
    @Expose
    private String addDayArrival;

    public JourneyEntity() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDepartureAirportId() {
        return departureAirportId;
    }

    public void setDepartureAirportId(String departureAirportId) {
        this.departureAirportId = departureAirportId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalAirportId() {
        return arrivalAirportId;
    }

    public void setArrivalAirportId(String arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    public String getDuration() {
        return duration;
    }

    public String getAddDayArrival() {
        return addDayArrival;
    }
}
