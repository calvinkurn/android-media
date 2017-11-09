package com.tokopedia.flight.search.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.search.data.cloud.model.response.Fare;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchReturnRouteDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchViewModel implements ItemType, Parcelable {
    private String id;
    private String type;
    private String aid;
    private String departureAirport;
    private String departureAirportName; // merge result
    private String departureAirportCity; // merge result
    private String departureTime; //ini waktu berangkat 2018-01-01T14:45:00Z
    private int departureTimeInt; //1450
    private String arrivalAirport;
    private String arrivalTime;
    private String arrivalAirportName; // merge result
    private String arrivalAirportCity; // merge result
    private int arrivalTimeInt; //1450
    private int totalTransit;
    private int addDayArrival;

    private String duration; // 1 jam 50 menit
    private int durationMinute;
    private String total; // 693000
    private int totalNumeric; // Fare "Rp 693.000"
    private String beforeTotal;

    private boolean isRefundable;

    private boolean isReturning;

    private Fare fare;

    private List<Route> routeList;
    private List<FlightAirlineDB> airlineDataList; // merge result

    public FlightSearchViewModel(FlightSearchSingleRouteDB flightSearchSingleRouteDB){
        Gson gson = new Gson();

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
        this.routeList = gson.fromJson(routesJsonString, flightRoutesType);

        String fareString = flightSearchSingleRouteDB.getFare();
        Type fareType = new TypeToken<Fare> () {}.getType();
        this.fare = gson.fromJson(fareString, fareType);
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

    public long getArrivalTimeInt() {
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

    public Fare getFare() {
        return fare;
    }

    public void setArrivalAirportCity(String arrivalAirportCity) {
        this.arrivalAirportCity = arrivalAirportCity;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public void setDepartureAirportCity(String departureAirportCity) {
        this.departureAirportCity = departureAirportCity;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public String getDepartureAirportCity() {
        return departureAirportCity;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public String getArrivalAirportCity() {
        return arrivalAirportCity;
    }

    public List<FlightAirlineDB> getAirlineDataList() {
        return airlineDataList;
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
        dest.writeString(this.departureAirportName);
        dest.writeString(this.departureAirportCity);
        dest.writeString(this.departureTime);
        dest.writeInt(this.departureTimeInt);
        dest.writeString(this.arrivalAirport);
        dest.writeString(this.arrivalTime);
        dest.writeString(this.arrivalAirportName);
        dest.writeString(this.arrivalAirportCity);
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
        dest.writeParcelable(this.fare, flags);
        dest.writeTypedList(this.routeList);
        dest.writeTypedList(this.airlineDataList);
    }

    protected FlightSearchViewModel(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.aid = in.readString();
        this.departureAirport = in.readString();
        this.departureAirportName = in.readString();
        this.departureAirportCity = in.readString();
        this.departureTime = in.readString();
        this.departureTimeInt = in.readInt();
        this.arrivalAirport = in.readString();
        this.arrivalTime = in.readString();
        this.arrivalAirportName = in.readString();
        this.arrivalAirportCity = in.readString();
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
        this.fare = in.readParcelable(Fare.class.getClassLoader());
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
