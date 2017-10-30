package com.tokopedia.flight.search.data.db.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;
import com.tokopedia.flight.search.data.cloud.model.Attributes;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.search.data.cloud.model.Route;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightSearchSingleRouteDB extends BaseModel implements ItemType {
    @PrimaryKey
    @Column(name = "id")
    String id;

    @Column(name = "type")
    String type;

    @Column(name = "aid")
    String aid;

    @Column(name = "departure_airport")
    String departureAirport;

    @Column(name = "departure_time")
    String departureTime;

    @Column(name = "departure_time_int")
    int departureTimeInt;

    @Column(name = "arrival_airport")
    String arrivalAirport;

    @Column(name = "arrival_time")
    String arrivalTime;

    @Column(name = "arrival_time_int")
    int arrivalTimeInt;

    @Column(name = "total_transit")
    int totalTransit;

    @Column(name = "add_day_arrival")
    int addDayArrival;

    @Column(name = "duration")
    String duration;

    @Column(name = "duration_minute")
    int durationMinute;

    @Column(name = "total")
    String total;

    @Column(name = "total_numeric")
    int totalNumeric;

    @Column(name = "before_total")
    String beforeTotal;

    @Column(name = "routes")
    String routes;

    @Column(name = "airline")
    String airline;

    @Column(name = "is_refundable")
    boolean isRefundable;

    @Override
    public int getType() {
        return 0;
    }

    public FlightSearchSingleRouteDB(){

    }

    public FlightSearchSingleRouteDB(FlightSearchData flightSearchData){
        this.id = flightSearchData.getId();
        this.type = flightSearchData.getFlightType();
        Attributes attributes = flightSearchData.getAttributes();
        this.aid = attributes.getAid();
        this.departureAirport = attributes.getDepartureAirport();
        this.departureTime = attributes.getDepartureTime();
        this.departureTimeInt = attributes.getDepartureTimeInt();
        this.arrivalAirport = attributes.getArrivalAirport();
        this.arrivalTime = attributes.getArrivalTime();
        this.arrivalTimeInt = attributes.getArrivalTimeInt();
        this.totalTransit = attributes.getTotalTransit();
        this.addDayArrival = attributes.getAddDayArrival();
        this.duration = attributes.getDuration();
        this.durationMinute = attributes.getDurationMinute();
        this.total = attributes.getTotal();
        this.totalNumeric = attributes.getTotalNumeric();
        this.beforeTotal = attributes.getBeforeTotal();

        List<Route> routeList = attributes.getRoutes();
        this.routes = new Gson().toJson(routeList);

        this.airline = "";
        this.isRefundable = true;
        for (int i = 0, sizei = routeList.size(); i<sizei; i++) {
            if (! routeList.get(i).getRefundable()){
                isRefundable = false;
            }
            if (!TextUtils.isEmpty(airline)) {
                airline+="-";
            }
            airline+=routeList.get(i).getAirline();
        }
    }

    public String getFlightType(){
        return type;
    }

    public String getId() {
        return id;
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

    public String getRoutes() {
        return routes;
    }

    public String getAirline() {
        return airline;
    }

    public boolean isRefundable() {
        return isRefundable;
    }
}
