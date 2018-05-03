package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.List;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationDetail implements Parcelable {
    private long refundId;
    private String createTime;
    private String estimatedRefund;
    private long estimatedRefundNumeric;
    private String realRefund;
    private long realRefundNumeric;
    private int status;
    private List<FlightCancellationListPassengerViewModel> passengers;
    private List<FlightOrderJourney> journeys;

    public FlightCancellationDetail() {
    }

    protected FlightCancellationDetail(Parcel in) {
        refundId = in.readInt();
        createTime = in.readString();
        estimatedRefund = in.readString();
        estimatedRefundNumeric = in.readLong();
        realRefund = in.readString();
        realRefundNumeric = in.readLong();
        status = in.readInt();
        passengers = in.createTypedArrayList(FlightCancellationListPassengerViewModel.CREATOR);
        journeys = in.createTypedArrayList(FlightOrderJourney.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(refundId);
        dest.writeString(createTime);
        dest.writeString(estimatedRefund);
        dest.writeLong(estimatedRefundNumeric);
        dest.writeString(realRefund);
        dest.writeLong(realRefundNumeric);
        dest.writeInt(status);
        dest.writeTypedList(passengers);
        dest.writeTypedList(journeys);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightCancellationDetail> CREATOR = new Creator<FlightCancellationDetail>() {
        @Override
        public FlightCancellationDetail createFromParcel(Parcel in) {
            return new FlightCancellationDetail(in);
        }

        @Override
        public FlightCancellationDetail[] newArray(int size) {
            return new FlightCancellationDetail[size];
        }
    };

    public long getRefundId() {
        return refundId;
    }

    public void setRefundId(long refundId) {
        this.refundId = refundId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEstimatedRefund() {
        return estimatedRefund;
    }

    public void setEstimatedRefund(String estimatedRefund) {
        this.estimatedRefund = estimatedRefund;
    }

    public long getEstimatedRefundNumeric() {
        return estimatedRefundNumeric;
    }

    public void setEstimatedRefundNumeric(long estimatedRefundNumeric) {
        this.estimatedRefundNumeric = estimatedRefundNumeric;
    }

    public String getRealRefund() {
        return realRefund;
    }

    public void setRealRefund(String realRefund) {
        this.realRefund = realRefund;
    }

    public long getRealRefundNumeric() {
        return realRefundNumeric;
    }

    public void setRealRefundNumeric(long realRefundNumeric) {
        this.realRefundNumeric = realRefundNumeric;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<FlightCancellationListPassengerViewModel> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<FlightCancellationListPassengerViewModel> passengers) {
        this.passengers = passengers;
    }

    public List<FlightOrderJourney> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<FlightOrderJourney> journeys) {
        this.journeys = journeys;
    }
}
