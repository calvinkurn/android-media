package com.tokopedia.flight.search.view.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.search.data.cloud.model.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchReturnRouteDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchViewModel implements ItemType {
    private String id;
    private String type;
    private String aid;
    private String departureAirport;
    private String departureTime;
    private int departureTimeInt;
    private String arrivalAirport;
    private String arrivalTime;
    private int arrivalTimeInt;
    private int totalTransit;
    private int addDayArrival;

    private String duration;
    private int durationMinute;
    private String total;
    private int totalNumeric;
    private String beforeTotal;

    private String airline;
    private boolean isRefundable;

    private boolean isReturning;

    private List<Route> routeList;

    private String airlineName;
    private String airlineLogo;

    public FlightSearchViewModel(FlightSearchSingleRouteDB flightSearchSingleRouteDB){
        this.id = flightSearchSingleRouteDB.getId();
        this.type = flightSearchSingleRouteDB.getFlightType();
        this.aid = flightSearchSingleRouteDB.getAid();
        this.departureAirport = flightSearchSingleRouteDB.getDepartureAirport();
        this.departureTime = flightSearchSingleRouteDB.getDepartureTime();
        this.departureTimeInt = flightSearchSingleRouteDB.getDepartureTimeInt();
        this.arrivalAirport = flightSearchSingleRouteDB.getArrivalAirport();
        this.arrivalTime = flightSearchSingleRouteDB.getArrivalTime();
        this.arrivalTimeInt = flightSearchSingleRouteDB.getArrivalTimeInt();
        this.totalTransit = flightSearchSingleRouteDB.getTotalTransit();
        this.addDayArrival = flightSearchSingleRouteDB.getAddDayArrival();

        this.duration = flightSearchSingleRouteDB.getDuration();
        this.durationMinute = flightSearchSingleRouteDB.getDurationMinute();
        this.total = flightSearchSingleRouteDB.getTotal();
        this.totalNumeric = flightSearchSingleRouteDB.getTotalNumeric();
        this.beforeTotal = flightSearchSingleRouteDB.getBeforeTotal();

        this.airline = flightSearchSingleRouteDB.getAirline();
        this.isRefundable = flightSearchSingleRouteDB.isRefundable();
        this.isReturning = flightSearchSingleRouteDB instanceof FlightSearchReturnRouteDB;

        String routesJsonString = flightSearchSingleRouteDB.getRoutes();
        Type flightRoutesType = new TypeToken<List<Route>> () {}.getType();
        this.routeList = new Gson().fromJson(routesJsonString, flightRoutesType);
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
        return 0;
    }

    public String getId() {
        return id;
    }

    public String getFlightType() {
        return type;
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

    public String getAirline() {
        return airline;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public boolean isReturning() {
        return isReturning;
    }

    public List<Route> getRouteList() {
        return routeList;
    }
}
