package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/13/17.
 */

public class HistoryItem implements Parcelable {
    private String provider;
    private int providerId;
    private String date;
    private String dateTimestamp;
    private String historyText;
    private boolean latest;

    public String getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(String dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHistoryText() {
        return historyText;
    }

    public void setHistoryText(String historyText) {
        this.historyText = historyText;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public HistoryItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.provider);
        dest.writeInt(this.providerId);
        dest.writeString(this.date);
        dest.writeString(this.dateTimestamp);
        dest.writeString(this.historyText);
        dest.writeByte(this.latest ? (byte) 1 : (byte) 0);
    }

    protected HistoryItem(Parcel in) {
        this.provider = in.readString();
        this.providerId = in.readInt();
        this.date = in.readString();
        this.dateTimestamp = in.readString();
        this.historyText = in.readString();
        this.latest = in.readByte() != 0;
    }

    public static final Creator<HistoryItem> CREATOR = new Creator<HistoryItem>() {
        @Override
        public HistoryItem createFromParcel(Parcel source) {
            return new HistoryItem(source);
        }

        @Override
        public HistoryItem[] newArray(int size) {
            return new HistoryItem[size];
        }
    };
}
