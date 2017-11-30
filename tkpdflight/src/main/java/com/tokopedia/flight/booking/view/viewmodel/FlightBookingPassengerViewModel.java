package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author by alvarisi on 11/7/17.
 */

public class FlightBookingPassengerViewModel implements Parcelable {
    private int passengerId; //passengerLocalNumber
    private boolean singleRoute;
    private int type;
    private String passengerTitle;
    private String headerTitle;
    private String passengerName;
    private String passengerBirthdate;
    private List<FlightBookingAmenityMetaViewModel> flightBookingLuggageMetaViewModels;
    private List<FlightBookingAmenityMetaViewModel> flightBookingMealMetaViewModels;

    public FlightBookingPassengerViewModel() {
    }

    protected FlightBookingPassengerViewModel(Parcel in) {
        passengerId = in.readInt();
        singleRoute = in.readByte() != 0;
        type = in.readInt();
        passengerTitle = in.readString();
        headerTitle = in.readString();
        passengerName = in.readString();
        passengerBirthdate = in.readString();
        flightBookingLuggageMetaViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
        flightBookingMealMetaViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
    }

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

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(passengerId);
        dest.writeByte((byte) (singleRoute ? 1 : 0));
        dest.writeInt(type);
        dest.writeString(passengerTitle);
        dest.writeString(headerTitle);
        dest.writeString(passengerName);
        dest.writeString(passengerBirthdate);
        dest.writeTypedList(flightBookingLuggageMetaViewModels);
        dest.writeTypedList(flightBookingMealMetaViewModels);
    }
}
