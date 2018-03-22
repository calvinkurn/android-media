package com.tokopedia.flight.cancellation.view;

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

    private long journeyId;
    private List<FlightBookingPassengerViewModel> passengerViewModelList;

    protected FlightCancellationViewModel(Parcel in) {
        journeyId = in.readLong();
        passengerViewModelList = in.createTypedArrayList(FlightBookingPassengerViewModel.CREATOR);
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
        dest.writeLong(journeyId);
        dest.writeTypedList(passengerViewModelList);
    }

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public List<FlightBookingPassengerViewModel> getPassengerViewModelList() {
        return passengerViewModelList;
    }

    public void setPassengerViewModelList(List<FlightBookingPassengerViewModel> passengerViewModelList) {
        this.passengerViewModelList = passengerViewModelList;
    }
}
