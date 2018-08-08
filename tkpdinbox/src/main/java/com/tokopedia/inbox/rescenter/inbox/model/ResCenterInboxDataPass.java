package com.tokopedia.inbox.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 4/7/16.
 */
public class ResCenterInboxDataPass implements Parcelable {

    private int requestAs;
    private String requestAsString;
    private int sortType;
    private String sortTypeString;
    private int filterStatus;
    private String filterStatusString;
    private int readUnreadStatus;
    private String readUnreadStatusString;
    private int requestPage;

    public ResCenterInboxDataPass() {
        // need constructor
    }

    public int getRequestAs() {
        return requestAs;
    }

    public void setRequestAs(int requestAs) {
        this.requestAs = requestAs;
    }

    public String getRequestAsString() {
        return requestAsString;
    }

    public void setRequestAsString(String requestAsString) {
        this.requestAsString = requestAsString;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public String getSortTypeString() {
        return sortTypeString;
    }

    public void setSortTypeString(String sortTypeString) {
        this.sortTypeString = sortTypeString;
    }

    public int getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(int filterStatus) {
        this.filterStatus = filterStatus;
    }

    public String getFilterStatusString() {
        return filterStatusString;
    }

    public void setFilterStatusString(String filterStatusString) {
        this.filterStatusString = filterStatusString;
    }

    public int getReadUnreadStatus() {
        return readUnreadStatus;
    }

    public void setReadUnreadStatus(int readUnreadStatus) {
        this.readUnreadStatus = readUnreadStatus;
    }

    public String getReadUnreadStatusString() {
        return readUnreadStatusString;
    }

    public void setReadUnreadStatusString(String readUnreadStatusString) {
        this.readUnreadStatusString = readUnreadStatusString;
    }

    public int getRequestPage() {
        return requestPage;
    }

    public void setRequestPage(int requestPage) {
        this.requestPage = requestPage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.requestAs);
        dest.writeString(this.requestAsString);
        dest.writeInt(this.sortType);
        dest.writeString(this.sortTypeString);
        dest.writeInt(this.filterStatus);
        dest.writeString(this.filterStatusString);
        dest.writeInt(this.readUnreadStatus);
        dest.writeString(this.readUnreadStatusString);
        dest.writeInt(this.requestPage);
    }

    protected ResCenterInboxDataPass(Parcel in) {
        this.requestAs = in.readInt();
        this.requestAsString = in.readString();
        this.sortType = in.readInt();
        this.sortTypeString = in.readString();
        this.filterStatus = in.readInt();
        this.filterStatusString = in.readString();
        this.readUnreadStatus = in.readInt();
        this.readUnreadStatusString = in.readString();
        this.requestPage = in.readInt();
    }

    public static final Parcelable.Creator<ResCenterInboxDataPass> CREATOR = new Parcelable.Creator<ResCenterInboxDataPass>() {
        @Override
        public ResCenterInboxDataPass createFromParcel(Parcel source) {
            return new ResCenterInboxDataPass(source);
        }

        @Override
        public ResCenterInboxDataPass[] newArray(int size) {
            return new ResCenterInboxDataPass[size];
        }
    };
}
