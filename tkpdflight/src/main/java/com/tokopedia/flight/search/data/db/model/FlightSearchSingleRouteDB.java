package com.tokopedia.flight.search.data.db.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;
import com.tokopedia.flight.search.data.cloud.model.response.Attributes;
import com.tokopedia.flight.search.data.cloud.model.response.Fare;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.data.cloud.model.response.Route;

import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightSearchSingleRouteDB extends BaseModel implements ItemType {
    public static final String TOTAL_NUMERIC = "total_numeric";
    public static final String AIRLINE = "airline";
    public static final String IS_REFUNDABLE = "is_refundable";
    public static final String DEPARTURE_TIME = "departure_time";
    public static final String DEPARTURE_TIME_INT = "departure_time_int";
    public static final String DURATION_MINUTE = "duration_minute";
    public static final String TOTAL_TRANSIT = "total_transit";
    @PrimaryKey
    @Column(name = "id")
    String id;

    @Column(name = "type")
    String type;

    @Column(name = "aid")
    String aid;

    @Column(name = "departure_airport")
    String departureAirport;

    @Column(name = DEPARTURE_TIME)
    String departureTime;

    @Column(name = DEPARTURE_TIME_INT)
    int departureTimeInt;

    @Column(name = "arrival_airport")
    String arrivalAirport;

    @Column(name = "arrival_time")
    String arrivalTime;

    @Column(name = "arrival_time_int")
    int arrivalTimeInt;

    @Column(name = TOTAL_TRANSIT)
    int totalTransit;

    @Column(name = "add_day_arrival")
    int addDayArrival;

    @Column(name = "duration")
    String duration;

    @Column(name = DURATION_MINUTE)
    int durationMinute;

    @Column(name = "total")
    String total;

    @Column(name = TOTAL_NUMERIC)
    int totalNumeric;

    @Column(name = "before_total")
    String beforeTotal;

    @Column(name = "routes")
    String routes;

    @Column(name = "fare")
    String fare;

    @Column(name = AIRLINE)
    String airline;

    @Column(name = IS_REFUNDABLE)
    boolean isRefundable;

    @Override
    public int getType() {
        return 0;
    }

    public FlightSearchSingleRouteDB() {

    }

    public FlightSearchSingleRouteDB(FlightSearchData flightSearchData) {
        Gson gson = new Gson();

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
        this.routes = gson.toJson(routeList);

        Fare fare = attributes.getFare();
        this.fare = gson.toJson(fare);

        this.airline = "";
        this.isRefundable = true;
        for (int i = 0, sizei = routeList.size(); i < sizei; i++) {
            if (!routeList.get(i).getRefundable()) {
                isRefundable = false;
            }
            if (!TextUtils.isEmpty(airline)) {
                airline += "-";
            }
            airline += routeList.get(i).getAirline();
        }
    }

    public String getFlightType() {
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

    public String getFare() {
        return fare;
    }

    public String getAirline() {
        return airline;
    }

    public boolean isRefundable() {
        return isRefundable;
    }
}
