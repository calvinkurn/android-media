package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hangnadi on 3/13/17.
 */

public class AwbData implements Parcelable {

    public static final Creator<AwbData> CREATOR = new Creator<AwbData>() {
        @Override
        public AwbData createFromParcel(Parcel source) {
            return new AwbData(source);
        }

        @Override
        public AwbData[] newArray(int size) {
            return new AwbData[size];
        }
    };
    private String shipmentRef;
    private String awbDate;
    private String shipmentID;
    private List<AwbAttachmentViewModel> attachments;

    public AwbData() {
    }

    protected AwbData(Parcel in) {
        this.shipmentRef = in.readString();
        this.awbDate = in.readString();
        this.shipmentID = in.readString();
        this.attachments = in.createTypedArrayList(AwbAttachmentViewModel.CREATOR);
    }

    public String getShipmentRef() {
        return shipmentRef;
    }

    public void setShipmentRef(String shipmentRef) {
        this.shipmentRef = shipmentRef;
    }

    public String getAwbDate() {
        return awbDate;
    }

    public void setAwbDate(String awbDate) {
        this.awbDate = awbDate;
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public List<AwbAttachmentViewModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AwbAttachmentViewModel> attachment) {
        this.attachments = attachment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shipmentRef);
        dest.writeString(this.awbDate);
        dest.writeString(this.shipmentID);
        dest.writeTypedList(this.attachments);
    }
}
