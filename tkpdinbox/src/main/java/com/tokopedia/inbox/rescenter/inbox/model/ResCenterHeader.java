package com.tokopedia.inbox.rescenter.inbox.model;

import android.os.Parcel;

/**
 * Created on 4/13/16.
 */
public class ResCenterHeader extends ResCenterInboxItem {

    public ResCenterCounterPending counterPending;
    public String pendingAmount;

    public ResCenterHeader() {
        setItemType(TYPE_HEADER);
    }

    public ResCenterCounterPending getCounterPending() {
        return counterPending;
    }

    public void setCounterPending(ResCenterCounterPending counterPending) {
        this.counterPending = counterPending;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.counterPending, flags);
        dest.writeString(this.pendingAmount);
    }

    protected ResCenterHeader(Parcel in) {
        super(in);
        this.counterPending = in.readParcelable(ResCenterCounterPending.class.getClassLoader());
        this.pendingAmount = in.readString();
    }

    public static final Creator<ResCenterHeader> CREATOR = new Creator<ResCenterHeader>() {
        @Override
        public ResCenterHeader createFromParcel(Parcel source) {
            return new ResCenterHeader(source);
        }

        @Override
        public ResCenterHeader[] newArray(int size) {
            return new ResCenterHeader[size];
        }
    };
}
