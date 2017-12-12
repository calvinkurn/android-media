package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.booking.view.adapter.FlightBookingPassengerTypeFactory;

import java.util.List;

/**
 * @author by alvarisi on 11/7/17.
 */

public class FlightBookingPassengerViewModel implements Parcelable, Visitable<FlightBookingPassengerTypeFactory> {
    public static final Creator<FlightBookingPassengerViewModel> CREATOR = new Creator<FlightBookingPassengerViewModel>() {
        @Override
        public FlightBookingPassengerViewModel createFromParcel(Parcel in) {
            return new FlightBookingPassengerViewModel(in);
        }

        @Override
        public FlightBookingPassengerViewModel[] newArray(int size) {
            return new FlightBookingPassengerViewModel[size];
        }
    };
    private int passengerId; //passengerLocalNumber
    private int type;
    private String passengerTitle;
    private String headerTitle;
    private String passengerFirstName;
    private String passengerLastName;
    private String passengerBirthdate;
    private List<FlightBookingAmenityMetaViewModel> flightBookingLuggageMetaViewModels;
    private List<FlightBookingAmenityMetaViewModel> flightBookingMealMetaViewModels;
    private int passengerTitleId;

    public FlightBookingPassengerViewModel() {
    }

    protected FlightBookingPassengerViewModel(Parcel in) {
        passengerId = in.readInt();
        type = in.readInt();
        passengerTitle = in.readString();
        headerTitle = in.readString();
        passengerFirstName = in.readString();
        passengerLastName = in.readString();
        passengerBirthdate = in.readString();
        flightBookingLuggageMetaViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
        flightBookingMealMetaViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
        passengerTitleId = in.readInt();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getPassengerFirstName() {
        return passengerFirstName;
    }

    public void setPassengerFirstName(String passengerFirstName) {
        this.passengerFirstName = passengerFirstName;
    }

    public String getPassengerBirthdate() {
        return passengerBirthdate;
    }

    public void setPassengerBirthdate(String passengerBirthdate) {
        this.passengerBirthdate = passengerBirthdate;
    }

    public List<FlightBookingAmenityMetaViewModel> getFlightBookingMealMetaViewModels() {
        return flightBookingMealMetaViewModels;
    }

    public void setFlightBookingMealMetaViewModels(List<FlightBookingAmenityMetaViewModel> flightBookingMealMetaViewModels) {
        this.flightBookingMealMetaViewModels = flightBookingMealMetaViewModels;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingPassengerViewModel && ((FlightBookingPassengerViewModel) obj).getPassengerId() == passengerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result *= prime * passengerId * type;
        return result;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public List<FlightBookingAmenityMetaViewModel> getFlightBookingLuggageMetaViewModels() {
        return flightBookingLuggageMetaViewModels;
    }

    public void setFlightBookingLuggageMetaViewModels(List<FlightBookingAmenityMetaViewModel> flightBookingLuggageMetaViewModels) {
        this.flightBookingLuggageMetaViewModels = flightBookingLuggageMetaViewModels;
    }

    public String getPassengerTitle() {
        return passengerTitle;
    }

    public void setPassengerTitle(String passengerTitle) {
        this.passengerTitle = passengerTitle;
    }

    public int getPassengerTitleId() {
        return passengerTitleId;
    }

    public void setPassengerTitleId(int passengerTitleId) {
        this.passengerTitleId = passengerTitleId;
    }

    public String getPassengerLastName() {
        return passengerLastName;
    }

    public void setPassengerLastName(String passengerLastName) {
        this.passengerLastName = passengerLastName;
    }

    @Override
    public int type(FlightBookingPassengerTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(passengerId);
        parcel.writeInt(type);
        parcel.writeString(passengerTitle);
        parcel.writeString(headerTitle);
        parcel.writeString(passengerFirstName);
        parcel.writeString(passengerLastName);
        parcel.writeString(passengerBirthdate);
        parcel.writeTypedList(flightBookingLuggageMetaViewModels);
        parcel.writeTypedList(flightBookingMealMetaViewModels);
        parcel.writeInt(passengerTitleId);
    }
}
