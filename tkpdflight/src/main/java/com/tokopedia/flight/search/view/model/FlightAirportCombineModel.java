package com.tokopedia.flight.search.view.model;

/**
 * Created by User on 11/16/2017.
 */

public class FlightAirportCombineModel {
    String depAirport;
    String arrAirport;
    String date;

    public FlightAirportCombineModel(String depAirport, String arrAirport, String date) {
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.date = date;
    }

    public String getDepAirport() {
        return depAirport;
    }

    public String getArrAirport() {
        return arrAirport;
    }

    public String getDate() {
        return date;
    }
}
