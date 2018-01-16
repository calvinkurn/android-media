
package com.tokopedia.flight.review.domain.verifybooking.model.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Route {

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
    private List<AmenityRoute> amenities = null;
    @SerializedName("stops")
    @Expose
    private int stops;
    @SerializedName("stop_detail")
    @Expose
    private List<Object> stopDetail = null;
    @SerializedName("operating_airline")
    @Expose
    private String operatingAirline;

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public void setDepartureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public void setArrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLayover() {
        return layover;
    }

    public void setLayover(String layover) {
        this.layover = layover;
    }

    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public boolean isIsRefundable() {
        return isRefundable;
    }

    public void setIsRefundable(boolean isRefundable) {
        this.isRefundable = isRefundable;
    }

    public List<AmenityRoute> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<AmenityRoute> amenities) {
        this.amenities = amenities;
    }

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public List<Object> getStopDetail() {
        return stopDetail;
    }

    public void setStopDetail(List<Object> stopDetail) {
        this.stopDetail = stopDetail;
    }

    public String getOperatingAirline() {
        return operatingAirline;
    }

    public void setOperatingAirline(String operatingAirline) {
        this.operatingAirline = operatingAirline;
    }

}
