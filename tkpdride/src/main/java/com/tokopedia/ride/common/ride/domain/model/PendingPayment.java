package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.ride.completetrip.view.viewmodel.TokoCashProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 7/10/17.
 */

public class PendingPayment implements Parcelable {
    private String operatorId;
    private String categoryId;
    private String pendingAmount;
    private String balance;
    private String currencyCode;
    private List<TokoCashProduct> topUpOptions;
    private String topupUrl;
    private boolean showTopupOptions;

    public PendingPayment() {
    }

    protected PendingPayment(Parcel in) {
        operatorId = in.readString();
        categoryId = in.readString();
        pendingAmount = in.readString();
        balance = in.readString();
        currencyCode = in.readString();
        if (in.readByte() == 0x01) {
            topUpOptions = new ArrayList<>();
            in.readList(topUpOptions, TokoCashProduct.class.getClassLoader());
        } else {
            topUpOptions = null;
        }
        topupUrl = in.readString();
        showTopupOptions = in.readByte() != 0x00;
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

    public List<TokoCashProduct> getTopUpOptions() {
        return topUpOptions;
    }

    public void setTopUpOptions(List<TokoCashProduct> topUpOptions) {
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

    public boolean isShowTopupOptions() {
        return showTopupOptions;
    }

    public void setShowTopupOptions(boolean showTopupOptions) {
        this.showTopupOptions = showTopupOptions;
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
        if (topUpOptions == null) {
            parcel.writeByte((byte) (0x00));
        } else {
            parcel.writeByte((byte) (0x01));
            parcel.writeList(topUpOptions);
        }
        parcel.writeString(topupUrl);
        parcel.writeByte((byte) (showTopupOptions ? 0x01 : 0x00));
    }
}
