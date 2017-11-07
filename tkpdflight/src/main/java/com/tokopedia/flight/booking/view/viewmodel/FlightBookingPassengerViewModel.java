package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingPassengerViewModel implements Parcelable {
    private int type;
    private String headerTitle;
    private String passengerName;
    private String passengerBirthdate;
    private String passportNumber;
    private List<FlightBookingLuggageRouteViewModel> luggageViewModels;
    private List<FlightBookingMealRouteViewModel> mealRouteViewModels;

    protected FlightBookingPassengerViewModel(Parcel in) {
        type = in.readInt();
        headerTitle = in.readString();
        passengerName = in.readString();
        passengerBirthdate = in.readString();
        passportNumber = in.readString();
        luggageViewModels = in.createTypedArrayList(FlightBookingLuggageRouteViewModel.CREATOR);
        mealRouteViewModels = in.createTypedArrayList(FlightBookingMealRouteViewModel.CREATOR);
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

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public List<FlightBookingLuggageRouteViewModel> getLuggageViewModels() {
        return luggageViewModels;
    }

    public void setLuggageViewModels(List<FlightBookingLuggageRouteViewModel> luggageViewModels) {
        this.luggageViewModels = luggageViewModels;
    }

    public List<FlightBookingMealRouteViewModel> getMealRouteViewModels() {
        return mealRouteViewModels;
    }

    public void setMealRouteViewModels(List<FlightBookingMealRouteViewModel> mealRouteViewModels) {
        this.mealRouteViewModels = mealRouteViewModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(headerTitle);
        dest.writeString(passengerName);
        dest.writeString(passengerBirthdate);
        dest.writeString(passportNumber);
        dest.writeTypedList(luggageViewModels);
        dest.writeTypedList(mealRouteViewModels);
    }
}
