package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingPassengerViewModel implements Parcelable {
    private int passengerId; //passengerLocalNumber
    private boolean singleRoute;
    private int type;
    private String headerTitle;
    private String passengerName;
    private String passengerBirthdate;
    private FlightBookingLuggageViewModel departureLugage;
    private FlightBookingLuggageViewModel returnLugage;
    private List<FlightBookingMealViewModel> departureMeals;
    private List<FlightBookingMealViewModel> returnMeals;

    public FlightBookingPassengerViewModel() {
    }

    protected FlightBookingPassengerViewModel(Parcel in) {
        passengerId = in.readInt();
        singleRoute = in.readByte() != 0;
        type = in.readInt();
        headerTitle = in.readString();
        passengerName = in.readString();
        passengerBirthdate = in.readString();
        departureLugage = in.readParcelable(FlightBookingLuggageViewModel.class.getClassLoader());
        returnLugage = in.readParcelable(FlightBookingLuggageViewModel.class.getClassLoader());
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

    public FlightBookingLuggageViewModel getDepartureLugage() {
        return departureLugage;
    }

    public void setDepartureLugage(FlightBookingLuggageViewModel departureLugage) {
        this.departureLugage = departureLugage;
    }

    public FlightBookingLuggageViewModel getReturnLugage() {
        return returnLugage;
    }

    public void setReturnLugage(FlightBookingLuggageViewModel returnLugage) {
        this.returnLugage = returnLugage;
    }

    public List<FlightBookingMealViewModel> getDepartureMeals() {
        return departureMeals;
    }

    public void setDepartureMeals(List<FlightBookingMealViewModel> departureMeals) {
        this.departureMeals = departureMeals;
    }

    public List<FlightBookingMealViewModel> getReturnMeals() {
        return returnMeals;
    }

    public void setReturnMeals(List<FlightBookingMealViewModel> returnMeals) {
        this.returnMeals = returnMeals;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(passengerId);
        dest.writeByte((byte) (singleRoute ? 1 : 0));
        dest.writeInt(type);
        dest.writeString(headerTitle);
        dest.writeString(passengerName);
        dest.writeString(passengerBirthdate);
        dest.writeParcelable(departureLugage, flags);
        dest.writeParcelable(returnLugage, flags);
    }
}
