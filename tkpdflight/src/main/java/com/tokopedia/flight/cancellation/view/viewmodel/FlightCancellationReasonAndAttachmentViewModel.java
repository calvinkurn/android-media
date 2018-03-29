package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentTypeFactory;

import java.util.List;

/**
 * @author  by alvarisi on 3/26/18.
 */

public class FlightCancellationReasonAndAttachmentViewModel implements Parcelable {

    private List<FlightCancellationAttachmentViewModel> attachments;
    private String reason;

    public FlightCancellationReasonAndAttachmentViewModel() {
    }


    protected FlightCancellationReasonAndAttachmentViewModel(Parcel in) {
        attachments = in.createTypedArrayList(FlightCancellationAttachmentViewModel.CREATOR);
        reason = in.readString();
    }

    public static final Creator<FlightCancellationReasonAndAttachmentViewModel> CREATOR = new Creator<FlightCancellationReasonAndAttachmentViewModel>() {
        @Override
        public FlightCancellationReasonAndAttachmentViewModel createFromParcel(Parcel in) {
            return new FlightCancellationReasonAndAttachmentViewModel(in);
        }

        @Override
        public FlightCancellationReasonAndAttachmentViewModel[] newArray(int size) {
            return new FlightCancellationReasonAndAttachmentViewModel[size];
        }
    };

    public List<FlightCancellationAttachmentViewModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<FlightCancellationAttachmentViewModel> attachments) {
        this.attachments = attachments;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(attachments);
        parcel.writeString(reason);
    }
}
