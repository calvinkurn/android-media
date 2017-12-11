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
    private int passengerId; //passengerLocalNumber
    private boolean singleRoute;
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

    public void setPassengerTitleId(int passengerTitleId) {
        this.passengerTitleId = passengerTitleId;
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

    public boolean isSingleRoute() {
        return singleRoute;
    }

    public void setSingleRoute(boolean singleRoute) {
        this.singleRoute = singleRoute;
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

    public String getPassengerLastName() {
        return passengerLastName;
    }

    public void setPassengerLastName(String passengerLastName) {
        this.passengerLastName = passengerLastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.passengerId);
        dest.writeByte(this.singleRoute ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
        dest.writeString(this.passengerTitle);
        dest.writeString(this.headerTitle);
        dest.writeString(this.passengerName);
        dest.writeString(this.passengerBirthdate);
        dest.writeTypedList(this.flightBookingLuggageMetaViewModels);
        dest.writeTypedList(this.flightBookingMealMetaViewModels);
        dest.writeInt(this.passengerTitleId);
    }

    protected FlightBookingPassengerViewModel(Parcel in) {
        this.passengerId = in.readInt();
        this.singleRoute = in.readByte() != 0;
        this.type = in.readInt();
        this.passengerTitle = in.readString();
        this.headerTitle = in.readString();
        this.passengerName = in.readString();
        this.passengerBirthdate = in.readString();
        this.flightBookingLuggageMetaViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
        this.flightBookingMealMetaViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
        this.passengerTitleId = in.readInt();
    }

    public static final Creator<FlightBookingPassengerViewModel> CREATOR = new Creator<FlightBookingPassengerViewModel>() {
        @Override
        public FlightBookingPassengerViewModel createFromParcel(Parcel source) {
            return new FlightBookingPassengerViewModel(source);
        }

        @Override
        public FlightBookingPassengerViewModel[] newArray(int size) {
            return new FlightBookingPassengerViewModel[size];
        }
    };
}
