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
    private FlightPassengerViewModel flightPassengerViewModel;
    private String flightPassengerFmt;
    private FlightAirportDB departureAirport;
    private String departureAirportFmt;
    private FlightAirportDB arrivalAirport;
    private String arrivalAirportFmt;
    private FlightClassViewModel flightClass;

    public FlightDashboardViewModel() {
    }

    public FlightDashboardViewModel(String departureDate,
                                    String departureDateFmt,
                                    String returnDate,
                                    String returnDateFmt,
                                    boolean isOneWay,
                                    FlightPassengerViewModel flightPassengerViewModel,
                                    String flightPassengerFmt,
                                    FlightAirportDB departureAirport,
                                    String departureAirportFmt,
                                    FlightAirportDB arrivalAirport,
                                    String arrivalAirportFmt,
                                    FlightClassViewModel flightClass) {
        this.departureDate = departureDate;
        this.departureDateFmt = departureDateFmt;
        this.returnDate = returnDate;
        this.returnDateFmt = returnDateFmt;
        this.isOneWay = isOneWay;
        this.flightPassengerViewModel = flightPassengerViewModel;
        this.flightPassengerFmt = flightPassengerFmt;
        this.departureAirport = departureAirport;
        this.departureAirportFmt = departureAirportFmt;
        this.arrivalAirport = arrivalAirport;
        this.arrivalAirportFmt = arrivalAirportFmt;
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

    public FlightPassengerViewModel getFlightPassengerViewModel() {
        return flightPassengerViewModel;
    }

    public void setFlightPassengerViewModel(FlightPassengerViewModel flightPassengerViewModel) {
        this.flightPassengerViewModel = flightPassengerViewModel;
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

    public String getDepartureAirportFmt() {
        return departureAirportFmt;
    }

    public void setDepartureAirportFmt(String departureAirportFmt) {
        this.departureAirportFmt = departureAirportFmt;
    }

    public String getArrivalAirportFmt() {
        return arrivalAirportFmt;
    }

    public void setArrivalAirportFmt(String arrivalAirportFmt) {
        this.arrivalAirportFmt = arrivalAirportFmt;
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
        private FlightPassengerViewModel flightPassengerViewModel;
        private String flightPassengerFmt;
        private FlightAirportDB departureAirport;
        private String departureAirportFmt;
        private FlightAirportDB arrivalAirport;
        private String arrivalAirportFmt;
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

        public Builder setFlightPassengerViewModel(FlightPassengerViewModel flightPassengerViewModel) {
            this.flightPassengerViewModel = flightPassengerViewModel;
            return this;
        }

        public Builder setFlightPassengerFmt(String passengerFmt) {
            this.flightPassengerFmt = passengerFmt;
            return this;
        }

        public Builder setDepartureAirport(FlightAirportDB departureAirport) {
            this.departureAirport = departureAirport;
            return this;
        }

        public Builder setDepartureAirportFmt(String departureAirportFmt) {
            this.departureAirportFmt = departureAirportFmt;
            return this;
        }

        public Builder setArrivalAirport(FlightAirportDB arrivalAirport) {
            this.arrivalAirport = arrivalAirport;
            return this;
        }

        public Builder setArrivalAirportFmt(String arrivalAirportFmt) {
            this.arrivalAirportFmt = arrivalAirportFmt;
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
                    departureAirport,
                    departureAirportFmt,
                    arrivalAirport,
                    arrivalAirportFmt,
                    flightClass);
        }
    }
}
