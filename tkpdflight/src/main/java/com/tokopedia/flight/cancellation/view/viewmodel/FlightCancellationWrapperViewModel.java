package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alvarisi on 3/29/18.
 */

public class FlightCancellationWrapperViewModel implements Parcelable {
    private List<FlightCancellationViewModel> viewModels;
    private String invoice;

    protected FlightCancellationWrapperViewModel(Parcel in) {
        viewModels = in.createTypedArrayList(FlightCancellationViewModel.CREATOR);
        invoice = in.readString();
    }

    public static final Creator<FlightCancellationWrapperViewModel> CREATOR = new Creator<FlightCancellationWrapperViewModel>() {
        @Override
        public FlightCancellationWrapperViewModel createFromParcel(Parcel in) {
            return new FlightCancellationWrapperViewModel(in);
        }

        @Override
        public FlightCancellationWrapperViewModel[] newArray(int size) {
            return new FlightCancellationWrapperViewModel[size];
        }
    };

    public FlightCancellationWrapperViewModel() {
    }

    public List<FlightCancellationViewModel> getViewModels() {
        return viewModels;
    }

    public void setViewModels(List<FlightCancellationViewModel> viewModels) {
        this.viewModels = viewModels;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(viewModels);
        parcel.writeString(invoice);
    }
}
