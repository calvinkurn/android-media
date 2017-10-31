package com.tokopedia.flight.dashboard.view.fragment.viewmodel;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightDashboardViewModel implements Cloneable {
    private String departureDate;
    private String departureDateFmt;
    private String returnDate;
    private String returnDateFmt;
    private boolean isOneWay;
    private FlightSelectPassengerViewModel flightPassengerViewModel;
    private String flightPassengerFmt;
    private FlightAirportDB origin;
    private String originFmt;
    private FlightAirportDB destination;
    private String destinationFmt;
    private FlightClassViewModel flightClass;

    public FlightDashboardViewModel() {
    }

    public FlightDashboardViewModel(String departureDate,
                                    String departureDateFmt,
                                    String returnDate,
                                    String returnDateFmt,
                                    boolean isOneWay,
                                    FlightSelectPassengerViewModel flightPassengerViewModel,
                                    String flightPassengerFmt,
                                    FlightAirportDB origin,
                                    String originFmt,
                                    FlightAirportDB destination,
                                    String destinationFmt,
                                    FlightClassViewModel flightClass) {
        this.departureDate = departureDate;
        this.departureDateFmt = departureDateFmt;
        this.returnDate = returnDate;
        this.returnDateFmt = returnDateFmt;
        this.isOneWay = isOneWay;
        this.flightPassengerViewModel = flightPassengerViewModel;
        this.flightPassengerFmt = flightPassengerFmt;
        this.origin = origin;
        this.originFmt = originFmt;
        this.destination = destination;
        this.destinationFmt = destinationFmt;
        this.flightClass = flightClass;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public FlightSelectPassengerViewModel getFlightPassengerViewModel() {
        return flightPassengerViewModel;
    }

    public void setFlightPassengerViewModel(FlightSelectPassengerViewModel flightPassengerViewModel) {
        this.flightPassengerViewModel = flightPassengerViewModel;
    }

    public FlightAirportDB getOrigin() {
        return origin;
    }

    public void setOrigin(FlightAirportDB origin) {
        this.origin = origin;
    }

    public FlightAirportDB getDestination() {
        return destination;
    }

    public void setDestination(FlightAirportDB destination) {
        this.destination = destination;
    }

    public CharSequence getDepartureDateFmt() {
        return departureDateFmt;
    }

    public CharSequence getReturnDateFmt() {
        return returnDateFmt;
    }

    public CharSequence getPassengerFmt() {
        return flightPassengerFmt;
    }

    public void setDepartureDateFmt(String departureDateFmt) {
        this.departureDateFmt = departureDateFmt;
    }

    public void setReturnDateFmt(String returnDateFmt) {
        this.returnDateFmt = returnDateFmt;
    }

    public String getFlightPassengerFmt() {
        return flightPassengerFmt;
    }

    public void setFlightPassengerFmt(String flightPassengerFmt) {
        this.flightPassengerFmt = flightPassengerFmt;
    }

    public String getOriginFmt() {
        return originFmt;
    }

    public void setOriginFmt(String originFmt) {
        this.originFmt = originFmt;
    }

    public String getDestinationFmt() {
        return destinationFmt;
    }

    public void setDestinationFmt(String destinationFmt) {
        this.destinationFmt = destinationFmt;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public FlightClassViewModel getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClassViewModel flightClass) {
        this.flightClass = flightClass;
    }

    public static class Builder {
        private String departureDate;
        private String departureDateFmt;
        private String returnDate;
        private String returnDateFmt;
        private boolean isOneWay;
        private FlightSelectPassengerViewModel flightPassengerViewModel;
        private String flightPassengerFmt;
        private FlightAirportDB origin;
        private String originFmt;
        private FlightAirportDB destination;
        private String destinationFmt;
        private FlightClassViewModel flightClass;

        public Builder() {
        }

        public Builder setDepartureDate(String departureDate) {
            this.departureDate = departureDate;
            return this;
        }

        public Builder setDepartureDateFmt(String departureDate) {
            this.departureDateFmt = departureDate;
            return this;
        }

        public Builder setReturnDate(String returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public Builder setReturnDateFmt(String returnDate) {
            this.returnDateFmt = returnDate;
            return this;
        }

        public Builder setIsOneWay(boolean isOneWay) {
            this.isOneWay = isOneWay;
            return this;
        }

        public Builder setFlightPassengerViewModel(FlightSelectPassengerViewModel flightPassengerViewModel) {
            this.flightPassengerViewModel = flightPassengerViewModel;
            return this;
        }

        public Builder setFlightPassengerFmt(String passengerFmt) {
            this.flightPassengerFmt = passengerFmt;
            return this;
        }

        public Builder setOrigin(FlightAirportDB origin) {
            this.origin = origin;
            return this;
        }

        public Builder setOriginFmt(String originFmt) {
            this.originFmt = originFmt;
            return this;
        }

        public Builder setDestination(FlightAirportDB destination) {
            this.destination = destination;
            return this;
        }

        public Builder setDestinationFmt(String destinationFmt) {
            this.destinationFmt = destinationFmt;
            return this;
        }

        public Builder setFlightClass(FlightClassViewModel flightClass) {
            this.flightClass = flightClass;
            return this;
        }


        public FlightDashboardViewModel build() {
            return new FlightDashboardViewModel(departureDate,
                    departureDateFmt,
                    returnDate,
                    returnDateFmt,
                    isOneWay,
                    flightPassengerViewModel,
                    flightPassengerFmt,
                    origin,
                    originFmt,
                    destination,
                    destinationFmt,
                    flightClass);
        }
    }
}
