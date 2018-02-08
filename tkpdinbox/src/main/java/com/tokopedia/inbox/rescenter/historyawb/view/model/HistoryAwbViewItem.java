package com.tokopedia.inbox.rescenter.historyawb.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by hangnadi on 3/24/17.
 */

@SuppressWarnings("ALL")
public class HistoryAwbViewItem implements Parcelable {
    private int actionBy;
    private String actionByText;
    private ArrayList<Attachment> attachment;
    private String conversationID;
    private String date;
    private String createTimestamp;
    private String remark;
    private String shipmentID;
    private String shippingRefNumber;
    private boolean latest;
    private boolean showEdit;
    private boolean showTrack;

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionByText(String actionByText) {
        this.actionByText = actionByText;
    }

    public String getActionByText() {
        return actionByText;
    }

    public void setAttachment(ArrayList<Attachment> attachment) {
        this.attachment = attachment;
    }

    public ArrayList<Attachment> getAttachment() {
        return attachment;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public void setShippingRefNumber(String shippingRefNumber) {
        this.shippingRefNumber = shippingRefNumber;
    }

    public String getShippingRefNumber() {
        return shippingRefNumber;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public HistoryAwbViewItem() {
    }

    public void setShowEdit(boolean showEdit) {
        this.showEdit = showEdit;
    }

    public boolean isShowEdit() {
        return showEdit;
    }

    public void setShowTrack(boolean showTrack) {
        this.showTrack = showTrack;
    }

    public boolean isShowTrack() {
        return showTrack;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.actionBy);
        dest.writeString(this.actionByText);
        dest.writeTypedList(this.attachment);
        dest.writeString(this.conversationID);
        dest.writeString(this.date);
        dest.writeString(this.createTimestamp);
        dest.writeString(this.remark);
        dest.writeString(this.shipmentID);
        dest.writeString(this.shippingRefNumber);
        dest.writeByte(this.latest ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showEdit ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showTrack ? (byte) 1 : (byte) 0);
    }

    protected HistoryAwbViewItem(Parcel in) {
        this.actionBy = in.readInt();
        this.actionByText = in.readString();
        this.attachment = in.createTypedArrayList(Attachment.CREATOR);
        this.conversationID = in.readString();
        this.date = in.readString();
        this.createTimestamp = in.readString();
        this.remark = in.readString();
        this.shipmentID = in.readString();
        this.shippingRefNumber = in.readString();
        this.latest = in.readByte() != 0;
        this.showEdit = in.readByte() != 0;
        this.showTrack = in.readByte() != 0;
    }

    public static final Creator<HistoryAwbViewItem> CREATOR = new Creator<HistoryAwbViewItem>() {
        @Override
        public HistoryAwbViewItem createFromParcel(Parcel source) {
            return new HistoryAwbViewItem(source);
        }

        @Override
        public HistoryAwbViewItem[] newArray(int size) {
            return new HistoryAwbViewItem[size];
        }
    };
}
