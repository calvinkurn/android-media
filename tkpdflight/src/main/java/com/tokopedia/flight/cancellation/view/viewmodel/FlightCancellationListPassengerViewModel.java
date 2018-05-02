package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

import java.util.List;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListPassengerViewModel extends FlightCancellationPassengerViewModel {


    private List<FlightBookingAmenityViewModel> amenities;

    public FlightCancellationListPassengerViewModel() {
    }

    public List<FlightBookingAmenityViewModel> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<FlightBookingAmenityViewModel> amenities) {
        this.amenities = amenities;
    }

    public FlightCancellationListPassengerViewModel(Parcel in) {
        super(in);
        amenities = in.createTypedArrayList(FlightBookingAmenityViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(amenities);
    }

    public static final Creator<FlightCancellationListPassengerViewModel> CREATOR = new Creator<FlightCancellationListPassengerViewModel>() {
        @Override
        public FlightCancellationListPassengerViewModel createFromParcel(Parcel in) {
            return new FlightCancellationListPassengerViewModel(in);
        }

        @Override
        public FlightCancellationListPassengerViewModel[] newArray(int size) {
            return new FlightCancellationListPassengerViewModel[size];
        }
    };
}
