package com.tokopedia.flight.search.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.search.data.cloud.model.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchReturnRouteDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchViewModel implements ItemType, Parcelable {
    private String id;
    private String type;
    private String aid;
    private String departureAirport;
    private String departureTime; //ini waktu berangkat 2018-01-01T14:45:00Z
    private int departureTimeInt;
    private String arrivalAirport; //
    private String arrivalTime;
    private int arrivalTimeInt;
    private int totalTransit;
    private int addDayArrival;

    private String duration; // 1 jam 50 menit
    private int durationMinute;
    private String total; // 693000
    private int totalNumeric; // Fare "Rp 693.000"
    private String beforeTotal;

    private boolean isRefundable;

    private boolean isReturning;

    private List<Route> routeList;
    private List<FlightAirlineDB> airlineDataList;

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

        this.isRefundable = flightSearchSingleRouteDB.isRefundable();
        this.isReturning = flightSearchSingleRouteDB instanceof FlightSearchReturnRouteDB;

        String routesJsonString = flightSearchSingleRouteDB.getRoutes();
        Type flightRoutesType = new TypeToken<List<Route>> () {}.getType();
        this.routeList = new Gson().fromJson(routesJsonString, flightRoutesType);
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

    public List<FlightAirlineDB> getAirlineList(){
        return airlineDataList;
    }

    public void setAirlineDataList(List<FlightAirlineDB> airlineDataList) {
        this.airlineDataList = airlineDataList;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.aid);
        dest.writeString(this.departureAirport);
        dest.writeString(this.departureTime);
        dest.writeInt(this.departureTimeInt);
        dest.writeString(this.arrivalAirport);
        dest.writeString(this.arrivalTime);
        dest.writeInt(this.arrivalTimeInt);
        dest.writeInt(this.totalTransit);
        dest.writeInt(this.addDayArrival);
        dest.writeString(this.duration);
        dest.writeInt(this.durationMinute);
        dest.writeString(this.total);
        dest.writeInt(this.totalNumeric);
        dest.writeString(this.beforeTotal);
        dest.writeByte(this.isRefundable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isReturning ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.routeList);
        dest.writeTypedList(this.airlineDataList);
    }

    protected FlightSearchViewModel(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.aid = in.readString();
        this.departureAirport = in.readString();
        this.departureTime = in.readString();
        this.departureTimeInt = in.readInt();
        this.arrivalAirport = in.readString();
        this.arrivalTime = in.readString();
        this.arrivalTimeInt = in.readInt();
        this.totalTransit = in.readInt();
        this.addDayArrival = in.readInt();
        this.duration = in.readString();
        this.durationMinute = in.readInt();
        this.total = in.readString();
        this.totalNumeric = in.readInt();
        this.beforeTotal = in.readString();
        this.isRefundable = in.readByte() != 0;
        this.isReturning = in.readByte() != 0;
        this.routeList = in.createTypedArrayList(Route.CREATOR);
        this.airlineDataList = in.createTypedArrayList(FlightAirlineDB.CREATOR);
    }

    public static final Parcelable.Creator<FlightSearchViewModel> CREATOR = new Parcelable.Creator<FlightSearchViewModel>() {
        @Override
        public FlightSearchViewModel createFromParcel(Parcel source) {
            return new FlightSearchViewModel(source);
        }

        @Override
        public FlightSearchViewModel[] newArray(int size) {
            return new FlightSearchViewModel[size];
        }
    };
}
