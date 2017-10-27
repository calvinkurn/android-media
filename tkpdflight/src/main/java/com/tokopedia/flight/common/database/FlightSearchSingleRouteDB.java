package com.tokopedia.flight.common.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightSearchSingleRouteDB extends BaseModel {
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

}
