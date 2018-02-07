package com.tokopedia.inbox.rescenter.historyaction.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/24/17.
 */

@SuppressWarnings("ALL")
public class HistoryActionViewItem implements Parcelable {
    private int actionBy;
    private boolean latest;
    private String actionByText;
    private String conversationID;
    private String date;
    private String dateTimestamp;
    private String dateNumber;
    private String month;
    private String createTimestampStr;
    private String timeNumber;

    public String getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(String dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    private String historyText;

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

    public String getDateNumber() {
        return dateNumber;
    }

    public void setDateNumber(String dateNumber) {
        this.dateNumber = dateNumber;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCreateTimestampStr() {
        return createTimestampStr;
    }

    public void setCreateTimestampStr(String createTimestampStr) {
        this.createTimestampStr = createTimestampStr;
    }

    public HistoryActionViewItem() {
    }

    public void setHistoryText(String historyText) {
        this.historyText = historyText;
    }

    public String getHistoryText() {
        return historyText;
    }

    public String getTimeNumber() {
        return timeNumber;
    }

    public void setTimeNumber(String timeNumber) {
        this.timeNumber = timeNumber;
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
        dest.writeString(this.dateTimestamp);
        dest.writeString(this.dateNumber);
        dest.writeString(this.month);
        dest.writeString(this.createTimestampStr);
        dest.writeString(this.historyText);
    }

    protected HistoryActionViewItem(Parcel in) {
        this.actionBy = in.readInt();
        this.latest = in.readByte() != 0;
        this.actionByText = in.readString();
        this.conversationID = in.readString();
        this.date = in.readString();
        this.dateTimestamp = in.readString();
        this.dateNumber = in.readString();
        this.month = in.readString();
        this.createTimestampStr = in.readString();
        this.historyText = in.readString();
    }

    public static final Creator<HistoryActionViewItem> CREATOR = new Creator<HistoryActionViewItem>() {
        @Override
        public HistoryActionViewItem createFromParcel(Parcel source) {
            return new HistoryActionViewItem(source);
        }

        @Override
        public HistoryActionViewItem[] newArray(int size) {
            return new HistoryActionViewItem[size];
        }
    };
}
