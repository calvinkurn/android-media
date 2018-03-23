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

    private String journeyId;
    private List<FlightCancellationPassengerViewModel> passengerViewModelList;

    public FlightCancellationViewModel() {
    }

    protected FlightCancellationViewModel(Parcel in) {
        journeyId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(journeyId);
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

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public List<FlightCancellationPassengerViewModel> getPassengerViewModelList() {
        return passengerViewModelList;
    }

    public void setPassengerViewModelList(List<FlightCancellationPassengerViewModel> passengerViewModelList) {
        this.passengerViewModelList = passengerViewModelList;
    }
}
