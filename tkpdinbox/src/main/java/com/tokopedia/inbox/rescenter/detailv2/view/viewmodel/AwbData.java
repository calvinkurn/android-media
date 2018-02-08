package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hangnadi on 3/13/17.
 */

public class AwbData implements Parcelable {

    private String shipmentRef;
    private String shipmentName;
    private String awbDate;
    private String awbDateTimestamp;
    private String shipmentID;
    private List<AwbAttachmentViewModel> attachments;
    private boolean isAddButtonAvailable;
    public AwbData() {
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

    public String getAwbDateTimestamp() {
        return awbDateTimestamp;
    }

    public void setAwbDateTimestamp(String awbDateTimestamp) {
        this.awbDateTimestamp = awbDateTimestamp;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public boolean isAddButtonAvailable() {
        return isAddButtonAvailable;
    }

    public void setAddButtonAvailable(boolean addButtonAvailable) {
        isAddButtonAvailable = addButtonAvailable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shipmentRef);
        dest.writeString(this.shipmentName);
        dest.writeString(this.awbDate);
        dest.writeString(this.awbDateTimestamp);
        dest.writeString(this.shipmentID);
        dest.writeTypedList(this.attachments);
        dest.writeByte(this.isAddButtonAvailable ? (byte) 1 : (byte) 0);
    }

    protected AwbData(Parcel in) {
        this.shipmentRef = in.readString();
        this.shipmentName = in.readString();
        this.awbDate = in.readString();
        this.awbDateTimestamp = in.readString();
        this.shipmentID = in.readString();
        this.attachments = in.createTypedArrayList(AwbAttachmentViewModel.CREATOR);
        this.isAddButtonAvailable = in.readByte() != 0;
    }

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
}
