package com.tokopedia.flight.search.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.search.data.cloud.model.response.Fare;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchReturnRouteDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.view.adapter.FilterSearchAdapterTypeFactory;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchViewModel implements Parcelable, Visitable<FilterSearchAdapterTypeFactory> {
    public static final int ONE_HOURS_DAY = 2400;
    public static final Creator<FlightSearchViewModel> CREATOR = new Creator<FlightSearchViewModel>() {
        @Override
        public FlightSearchViewModel createFromParcel(Parcel in) {
            return new FlightSearchViewModel(in);
        }

        @Override
        public FlightSearchViewModel[] newArray(int size) {
            return new FlightSearchViewModel[size];
        }
    };
    private String term;
    private String id;
    private String type;
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
    private RefundableEnum isRefundable;
    private boolean isReturning;
    private Fare fare;
    private List<Route> routeList;
    private List<FlightAirlineDB> airlineDataList; // merge result

    public FlightSearchViewModel(FlightSearchSingleRouteDB flightSearchSingleRouteDB) {
        Gson gson = new Gson();
        this.term = flightSearchSingleRouteDB.getTerm();
        this.id = flightSearchSingleRouteDB.getId();
        this.type = flightSearchSingleRouteDB.getFlightType();
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

        this.isRefundable = flightSearchSingleRouteDB.getIsRefundable();
        this.isReturning = flightSearchSingleRouteDB instanceof FlightSearchReturnRouteDB;

        String routesJsonString = flightSearchSingleRouteDB.getRoutes();
        Type flightRoutesType = new TypeToken<List<Route>>() {
        }.getType();
        this.routeList = gson.fromJson(routesJsonString, flightRoutesType);

        this.fare = new Fare(flightSearchSingleRouteDB.getAdult(),
                flightSearchSingleRouteDB.getChild(),
                flightSearchSingleRouteDB.getInfant(),
                flightSearchSingleRouteDB.getAdultNumeric(),
                flightSearchSingleRouteDB.getChildNumeric(),
                flightSearchSingleRouteDB.getInfantNumeric());
    }

    protected FlightSearchViewModel(Parcel in) {
        term = in.readString();
        id = in.readString();
        type = in.readString();
        departureAirport = in.readString();
        departureAirportName = in.readString();
        departureAirportCity = in.readString();
        departureTime = in.readString();
        departureTimeInt = in.readInt();
        arrivalAirport = in.readString();
        arrivalTime = in.readString();
        arrivalAirportName = in.readString();
        arrivalAirportCity = in.readString();
        arrivalTimeInt = in.readInt();
        totalTransit = in.readInt();
        addDayArrival = in.readInt();
        duration = in.readString();
        durationMinute = in.readInt();
        total = in.readString();
        totalNumeric = in.readInt();
        beforeTotal = in.readString();
        isReturning = in.readByte() != 0;
        fare = in.readParcelable(Fare.class.getClassLoader());
        routeList = in.createTypedArrayList(Route.CREATOR);
        airlineDataList = in.createTypedArrayList(FlightAirlineDB.CREATOR);
    }

    public String getId() {
        return id;
    }

    public String getTerm() {
        return term;
    }

    public String getFlightType() {
        return type;
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

    public long getArrivalTimeIntPlusDay() {
        return addDayArrival * ONE_HOURS_DAY + arrivalTimeInt;
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

    public List<FlightAirlineDB> getAirlineList() {
        return airlineDataList;
    }

    public void setAirlineDataList(List<FlightAirlineDB> airlineDataList) {
        this.airlineDataList = airlineDataList;
    }

    public RefundableEnum isRefundable() {
        return isRefundable;
    }

    public boolean isReturning() {
        return isReturning;
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getDepartureAirportCity() {
        return departureAirportCity;
    }

    public void setDepartureAirportCity(String departureAirportCity) {
        this.departureAirportCity = departureAirportCity;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getArrivalAirportCity() {
        return arrivalAirportCity;
    }

    public void setArrivalAirportCity(String arrivalAirportCity) {
        this.arrivalAirportCity = arrivalAirportCity;
    }

    @Override
    public int type(FilterSearchAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(term);
        parcel.writeString(id);
        parcel.writeString(type);
        parcel.writeString(departureAirport);
        parcel.writeString(departureAirportName);
        parcel.writeString(departureAirportCity);
        parcel.writeString(departureTime);
        parcel.writeInt(departureTimeInt);
        parcel.writeString(arrivalAirport);
        parcel.writeString(arrivalTime);
        parcel.writeString(arrivalAirportName);
        parcel.writeString(arrivalAirportCity);
        parcel.writeInt(arrivalTimeInt);
        parcel.writeInt(totalTransit);
        parcel.writeInt(addDayArrival);
        parcel.writeString(duration);
        parcel.writeInt(durationMinute);
        parcel.writeString(total);
        parcel.writeInt(totalNumeric);
        parcel.writeString(beforeTotal);
        parcel.writeByte((byte) (isReturning ? 1 : 0));
        parcel.writeParcelable(fare, i);
        parcel.writeTypedList(routeList);
        parcel.writeTypedList(airlineDataList);
    }
}
