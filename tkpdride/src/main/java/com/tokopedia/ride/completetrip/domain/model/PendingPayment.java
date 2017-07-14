package com.tokopedia.ride.completetrip.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 7/10/17.
 */

public class PendingPayment implements Parcelable {
    private String operatorId;
    private String categoryId;
    private String pendingAmount;
    private String balance;
    private String currencyCode;
    private String topUpOptions;
    private String topupUrl;

    public PendingPayment() {
    }

    protected PendingPayment(Parcel in) {
        operatorId = in.readString();
        categoryId = in.readString();
        pendingAmount = in.readString();
        balance = in.readString();
        currencyCode = in.readString();
        topUpOptions = in.readString();
        topupUrl = in.readString();
    }

    public static final Creator<PendingPayment> CREATOR = new Creator<PendingPayment>() {
        @Override
        public PendingPayment createFromParcel(Parcel in) {
            return new PendingPayment(in);
        }

        @Override
        public PendingPayment[] newArray(int size) {
            return new PendingPayment[size];
        }
    };

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTopUpOptions() {
        return topUpOptions;
    }

    public void setTopUpOptions(String topUpOptions) {
        this.topUpOptions = topUpOptions;
    }

    public String getTopupUrl() {
        return topupUrl;
    }

    public void setTopupUrl(String topupUrl) {
        this.topupUrl = topupUrl;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(operatorId);
        parcel.writeString(categoryId);
        parcel.writeString(pendingAmount);
        parcel.writeString(balance);
        parcel.writeString(currencyCode);
        parcel.writeString(topUpOptions);
        parcel.writeString(topupUrl);
    }
}
