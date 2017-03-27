package com.tokopedia.inbox.rescenter.historyaddress.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/24/17.
 */

@SuppressWarnings("ALL")
public class HistoryAddressViewItem implements Parcelable {
    private int actionBy;
    private boolean latest;
    private String actionByText;
    private String conversationID;
    private String date;
    private String address;

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

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public HistoryAddressViewItem() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.actionBy);
        dest.writeByte(this.latest ? (byte) 1 : (byte) 0);
        dest.writeString(this.actionByText);
        dest.writeString(this.conversationID);
        dest.writeString(this.date);
        dest.writeString(this.address);
    }

    protected HistoryAddressViewItem(Parcel in) {
        this.actionBy = in.readInt();
        this.latest = in.readByte() != 0;
        this.actionByText = in.readString();
        this.conversationID = in.readString();
        this.date = in.readString();
        this.address = in.readString();
    }

    public static final Creator<HistoryAddressViewItem> CREATOR = new Creator<HistoryAddressViewItem>() {
        @Override
        public HistoryAddressViewItem createFromParcel(Parcel source) {
            return new HistoryAddressViewItem(source);
        }

        @Override
        public HistoryAddressViewItem[] newArray(int size) {
            return new HistoryAddressViewItem[size];
        }
    };
}
