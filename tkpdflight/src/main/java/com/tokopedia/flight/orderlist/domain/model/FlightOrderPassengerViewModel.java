package com.tokopedia.flight.orderlist.domain.model;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

import java.util.List;

/**
 * Created by alvarisi on 12/12/17.
 */

public class FlightOrderPassengerViewModel {
    private int type;
    private int passengerTitleId;
    private String passengerFirstName;
    private String passengerLastName;
    private String passengerBirthdate;
    private List<FlightBookingAmenityViewModel> amenities;

    public FlightOrderPassengerViewModel() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPassengerTitleId() {
        return passengerTitleId;
    }

    public void setPassengerTitleId(int passengerTitleId) {
        this.passengerTitleId = passengerTitleId;
    }

    public String getPassengerFirstName() {
        return passengerFirstName;
    }

    public void setPassengerFirstName(String passengerFirstName) {
        this.passengerFirstName = passengerFirstName;
    }

    public String getPassengerLastName() {
        return passengerLastName;
    }

    public void setPassengerLastName(String passengerLastName) {
        this.passengerLastName = passengerLastName;
    }

    public String getPassengerBirthdate() {
        return passengerBirthdate;
    }

    public void setPassengerBirthdate(String passengerBirthdate) {
        this.passengerBirthdate = passengerBirthdate;
    }

    public List<FlightBookingAmenityViewModel> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<FlightBookingAmenityViewModel> amenities) {
        this.amenities = amenities;
    }
}
