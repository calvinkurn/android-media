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

    private String invoiceId;
    private FlightCancellationJourney flightCancellationJourney;
    private List<FlightCancellationPassengerViewModel> passengerViewModelList;
    private FlightCancellationReasonAndAttachmentViewModel reasonAndAttachments;

    public FlightCancellationViewModel() {
    }

    protected FlightCancellationViewModel(Parcel in) {
        invoiceId = in.readString();
        flightCancellationJourney = in.readParcelable(FlightCancellationJourney.class.getClassLoader());
        passengerViewModelList = in.createTypedArrayList(FlightCancellationPassengerViewModel.CREATOR);
        reasonAndAttachments = in.readParcelable(FlightCancellationReasonAndAttachmentViewModel.class.getClassLoader());
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

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(invoiceId);
        parcel.writeParcelable(flightCancellationJourney, i);
        parcel.writeTypedList(passengerViewModelList);
        parcel.writeParcelable(reasonAndAttachments, i);
    }

    public FlightCancellationReasonAndAttachmentViewModel getReasonAndAttachments() {
        return reasonAndAttachments;
    }

    public void setReasonAndAttachments(FlightCancellationReasonAndAttachmentViewModel reasonAndAttachments) {
        this.reasonAndAttachments = reasonAndAttachments;
    }
}
