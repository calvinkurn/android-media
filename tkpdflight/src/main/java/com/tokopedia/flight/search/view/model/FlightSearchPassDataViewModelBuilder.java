package com.tokopedia.flight.search.view.model;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;

public class FlightSearchPassDataViewModelBuilder {
    private String departureDate;
    private String returnDate;
    private boolean isOneWay;
    private FlightPassengerViewModel flightPassengerViewModel;
    private FlightAirportDB origin;
    private FlightAirportDB destination;
    private FlightClassViewModel flightClass;

    public FlightSearchPassDataViewModelBuilder setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public FlightSearchPassDataViewModelBuilder setReturnDate(String returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public FlightSearchPassDataViewModelBuilder setIsOneWay(boolean isOneWay) {
        this.isOneWay = isOneWay;
        return this;
    }

    public FlightSearchPassDataViewModelBuilder setFlightPassengerViewModel(FlightPassengerViewModel flightPassengerViewModel) {
        this.flightPassengerViewModel = flightPassengerViewModel;
        return this;
    }

    public FlightSearchPassDataViewModelBuilder setOrigin(FlightAirportDB origin) {
        this.origin = origin;
        return this;
    }

    public FlightSearchPassDataViewModelBuilder setDestination(FlightAirportDB destination) {
        this.destination = destination;
        return this;
    }

    public FlightSearchPassDataViewModelBuilder setFlightClass(FlightClassViewModel flightClass) {
        this.flightClass = flightClass;
        return this;
    }

    public FlightSearchPassDataViewModel createFlightSearchPassDataViewModel() {
        return new FlightSearchPassDataViewModel(departureDate, returnDate, isOneWay, flightPassengerViewModel, origin, destination, flightClass);
    }
}