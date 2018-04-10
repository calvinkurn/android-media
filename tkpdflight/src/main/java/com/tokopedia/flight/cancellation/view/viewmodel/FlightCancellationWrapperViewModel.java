package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alvarisi on 3/29/18.
 */

public class FlightCancellationWrapperViewModel implements Parcelable {
    private FlightCancellationReasonAndAttachmentViewModel cancellationReasonAndAttachment;
    private List<FlightCancellationViewModel> viewModels;
    private String invoice;
    private String totalPrice;

    public FlightCancellationWrapperViewModel() {
    }

    protected FlightCancellationWrapperViewModel(Parcel in) {
        cancellationReasonAndAttachment = in.readParcelable(FlightCancellationReasonAndAttachmentViewModel.class.getClassLoader());
        viewModels = in.createTypedArrayList(FlightCancellationViewModel.CREATOR);
        invoice = in.readString();
        totalPrice = in.readString();
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

    public FlightCancellationReasonAndAttachmentViewModel getCancellationReasonAndAttachment() {
        return cancellationReasonAndAttachment;
    }

    public void setCancellationReasonAndAttachment(FlightCancellationReasonAndAttachmentViewModel cancellationReasonAndAttachment) {
        this.cancellationReasonAndAttachment = cancellationReasonAndAttachment;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(cancellationReasonAndAttachment, i);
        parcel.writeTypedList(viewModels);
        parcel.writeString(invoice);
        parcel.writeString(totalPrice);
    }
}
