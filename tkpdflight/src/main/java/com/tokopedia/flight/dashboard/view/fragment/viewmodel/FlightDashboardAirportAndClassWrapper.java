package com.tokopedia.flight.dashboard.view.fragment.viewmodel;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;

/**
 * Created by alvarisi on 2/7/18.
 */

public class FlightDashboardAirportAndClassWrapper {
    private FlightAirportDB departureAirport;
    private FlightAirportDB arrivalAirport;
    private FlightClassEntity flightClassEntity;

    public FlightDashboardAirportAndClassWrapper() {
    }

    public FlightAirportDB getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(FlightAirportDB departureAirport) {
        this.departureAirport = departureAirport;
    }

    public FlightAirportDB getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(FlightAirportDB arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public FlightClassEntity getFlightClassEntity() {
        return flightClassEntity;
    }

    public void setFlightClassEntity(FlightClassEntity flightClassEntity) {
        this.flightClassEntity = flightClassEntity;
    }
}
