package com.tokopedia.flight.search.data.cloud.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.booking.data.cloud.entity.Attribute;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;

/**
 * Created by User on 11/8/2017.
 */

public class Attributes {
    @SerializedName("departure")
    @Expose
    private String departure;
    @SerializedName("arrival")
    @Expose
    private String arrival;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("adult")
    @Expose
    private int adult;
    @SerializedName("child")
    @Expose
    private int child;
    @SerializedName("infant")
    @Expose
    private int infant;
    @SerializedName("class")
    @Expose
    private int _class;

    public Attributes(int _class,
                      int adult, int child, int infant){
        this.adult = adult;
        this.child = child;
        this.infant = infant;
        this._class = _class;
    }

    public Attributes(String departure, String arrival, String date, int _class,
                      int adult, int child, int infant) {
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
        this.adult = adult;
        this.child = child;
        this.infant = infant;
        this._class = _class;
    }

    public Attributes(FlightSearchApiRequestModel flightSearchApiRequestModel) {
        this.departure = flightSearchApiRequestModel.getDepAirport();
        this.arrival = flightSearchApiRequestModel.getArrAirport();
        this.date = flightSearchApiRequestModel.getDate();
        this.adult = flightSearchApiRequestModel.getAdult();
        this.child = flightSearchApiRequestModel.getChildren();
        this.infant = flightSearchApiRequestModel.getInfant();
        this._class = flightSearchApiRequestModel.getClassID();
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDate() {
        return date;
    }
}
