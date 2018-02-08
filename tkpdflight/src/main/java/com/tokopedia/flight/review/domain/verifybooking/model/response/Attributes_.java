
package com.tokopedia.flight.review.domain.verifybooking.model.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes_ {

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
    @SerializedName("fare")
    @Expose
    private Fare fare;
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
    @SerializedName("amenities")
    @Expose
    private List<Object> amenities = null;
    @SerializedName("similar_journeys")
    @Expose
    private List<Object> similarJourneys = null;

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getDepartureTimeInt() {
        return departureTimeInt;
    }

    public void setDepartureTimeInt(int departureTimeInt) {
        this.departureTimeInt = departureTimeInt;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getArrivalTimeInt() {
        return arrivalTimeInt;
    }

    public void setArrivalTimeInt(int arrivalTimeInt) {
        this.arrivalTimeInt = arrivalTimeInt;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public int getTotalTransit() {
        return totalTransit;
    }

    public void setTotalTransit(int totalTransit) {
        this.totalTransit = totalTransit;
    }

    public int getAddDayArrival() {
        return addDayArrival;
    }

    public void setAddDayArrival(int addDayArrival) {
        this.addDayArrival = addDayArrival;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getDurationMinute() {
        return durationMinute;
    }

    public void setDurationMinute(int durationMinute) {
        this.durationMinute = durationMinute;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getTotalNumeric() {
        return totalNumeric;
    }

    public void setTotalNumeric(int totalNumeric) {
        this.totalNumeric = totalNumeric;
    }

    public String getBeforeTotal() {
        return beforeTotal;
    }

    public void setBeforeTotal(String beforeTotal) {
        this.beforeTotal = beforeTotal;
    }

    public List<Object> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Object> amenities) {
        this.amenities = amenities;
    }

    public List<Object> getSimilarJourneys() {
        return similarJourneys;
    }

    public void setSimilarJourneys(List<Object> similarJourneys) {
        this.similarJourneys = similarJourneys;
    }

}
