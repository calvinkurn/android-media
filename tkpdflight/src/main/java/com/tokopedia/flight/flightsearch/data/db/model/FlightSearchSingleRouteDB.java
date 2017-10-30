package com.tokopedia.flight.flightsearch.data.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;

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
    int total;

    @Column(name = "total_numeric")
    int totalNumeric;

    @Column(name = "before_total")
    int beforeTotal;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalNumeric() {
        return totalNumeric;
    }

    public void setTotalNumeric(int totalNumeric) {
        this.totalNumeric = totalNumeric;
    }

    public int getBeforeTotal() {
        return beforeTotal;
    }

    public void setBeforeTotal(int beforeTotal) {
        this.beforeTotal = beforeTotal;
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }
}
