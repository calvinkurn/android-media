package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationTypeFactory;

import java.util.List;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationViewModel implements Parcelable,
        Visitable<FlightCancellationTypeFactory> {

    private FlightCancellationJourney flightCancellationJourney;
    private List<FlightCancellationPassengerViewModel> passengerViewModelList;

    public FlightCancellationViewModel() {
    }

    protected FlightCancellationViewModel(Parcel in) {
        flightCancellationJourney = in.readParcelable(FlightCancellationJourney.class.getClassLoader());
        passengerViewModelList = in.createTypedArrayList(FlightCancellationPassengerViewModel.CREATOR);
    }

    public static final Creator<FlightCancellationViewModel> CREATOR = new Creator<FlightCancellationViewModel>() {
        @Override
        public FlightCancellationViewModel createFromParcel(Parcel in) {
            return new FlightCancellationViewModel(in);
        }

        @Override
        public FlightCancellationViewModel[] newArray(int size) {
            return new FlightCancellationViewModel[size];
        }
    };

    @Override
    public int type(FlightCancellationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(flightCancellationJourney, flags);
        dest.writeTypedList(passengerViewModelList);
    }

    public FlightCancellationJourney getFlightCancellationJourney() {
        return flightCancellationJourney;
    }

    public void setFlightCancellationJourney(FlightCancellationJourney flightCancellationJourney) {
        this.flightCancellationJourney = flightCancellationJourney;
    }

    public List<FlightCancellationPassengerViewModel> getPassengerViewModelList() {
        return passengerViewModelList;
    }

    public void setPassengerViewModelList(List<FlightCancellationPassengerViewModel> passengerViewModelList) {
        this.passengerViewModelList = passengerViewModelList;
    }
}
