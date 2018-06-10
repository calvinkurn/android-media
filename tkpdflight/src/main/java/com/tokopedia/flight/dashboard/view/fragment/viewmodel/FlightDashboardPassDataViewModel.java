package com.tokopedia.flight.dashboard.view.fragment.viewmodel;

/**
 * @author by furqan on 05/02/18.
 */

public class FlightDashboardPassDataViewModel {
    private String departureAirportId;
    private String arrivalAirportId;
    private String departureDate;
    private String returnDate;
    private int adultPassengerCount;
    private int childPassengerCount;
    private int infantPassengerCount;
    private int flightClass;
    private boolean isRoundTrip;

    public String getDepartureAirportId() {
        return departureAirportId;
    }

    public void setDepartureAirportId(String departureAirportId) {
        this.departureAirportId = departureAirportId;
    }

    public String getArrivalAirportId() {
        return arrivalAirportId;
    }

    public void setArrivalAirportId(String arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
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

    public int getAdultPassengerCount() {
        return adultPassengerCount;
    }

    public void setAdultPassengerCount(int adultPassengerCount) {
        this.adultPassengerCount = adultPassengerCount;
    }

    public int getChildPassengerCount() {
        return childPassengerCount;
    }

    public void setChildPassengerCount(int childPassengerCount) {
        this.childPassengerCount = childPassengerCount;
    }

    public int getInfantPassengerCount() {
        return infantPassengerCount;
    }

    public void setInfantPassengerCount(int infantPassengerCount) {
        this.infantPassengerCount = infantPassengerCount;
    }

    public int getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(int flightClass) {
        this.flightClass = flightClass;
    }

    public boolean isRoundTrip() {
        return isRoundTrip;
    }

    public void setRoundTrip(boolean roundTrip) {
        isRoundTrip = roundTrip;
    }
}
