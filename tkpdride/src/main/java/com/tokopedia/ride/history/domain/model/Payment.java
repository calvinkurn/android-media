package com.tokopedia.ride.history.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 4/19/17.
 */

public class Payment implements Parcelable {
    private String currency;
    private String totalAmount;
    private int pendingAmount;
    private String paidAmount;
    private boolean receiptReady;
    private String paymentMethod;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String value) {
        this.totalAmount = value;
    }

    public boolean isReceiptReady() {
        return receiptReady;
    }

    public void setReceiptReady(boolean receiptReady) {
        this.receiptReady = receiptReady;
    }

    public int getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(int pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Payment() {

    }

    protected Payment(Parcel in) {
        currency = in.readString();
        totalAmount = in.readString();
        receiptReady = in.readByte() != 0;
        pendingAmount = in.readInt();
        paidAmount = in.readString();
        paymentMethod = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency);
        dest.writeString(totalAmount);
        dest.writeByte((byte) (receiptReady ? 1 : 0));
        dest.writeInt(pendingAmount);
        dest.writeString(paidAmount);
        dest.writeString(paymentMethod);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Payment> CREATOR = new Parcelable.Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };
}
