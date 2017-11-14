package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vishal Gupta on 11/13/17.
 */

public class GetPending implements Parcelable {
    private String uid;
    private String guestId;
    private String pendingAmountFormatted;
    private String paymentMethod;
    private String token;
    private String ccActiveToken;
    private String lastRequestId;
    private String lastRideAmount;
    private String lastRidePayment;
    private String lastRidePaymentMethod;
    private String pickupAddressName;
    private String destinationAddressName;
    private String date;
    private int pendingAmount;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getPendingAmountFormatted() {
        return pendingAmountFormatted;
    }

    public void setPendingAmountFormatted(String pendingAmount) {
        this.pendingAmountFormatted = pendingAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCcActiveToken() {
        return ccActiveToken;
    }

    public void setCcActiveToken(String ccActiveToken) {
        this.ccActiveToken = ccActiveToken;
    }

    public String getLastRequestId() {
        return lastRequestId;
    }

    public void setLastRequestId(String lastRequestId) {
        this.lastRequestId = lastRequestId;
    }

    public String getLastRideAmount() {
        return lastRideAmount;
    }

    public void setLastRideAmount(String lastRideAmount) {
        this.lastRideAmount = lastRideAmount;
    }

    public String getLastRidePayment() {
        return lastRidePayment;
    }

    public void setLastRidePayment(String lastRidePayment) {
        this.lastRidePayment = lastRidePayment;
    }

    public String getLastRidePaymentMethod() {
        return lastRidePaymentMethod;
    }

    public void setLastRidePaymentMethod(String lastRidePaymentMethod) {
        this.lastRidePaymentMethod = lastRidePaymentMethod;
    }

    public String getPickupAddressName() {
        return pickupAddressName;
    }

    public void setPickupAddressName(String pickupAddressName) {
        this.pickupAddressName = pickupAddressName;
    }

    public String getDestinationAddressName() {
        return destinationAddressName;
    }

    public void setDestinationAddressName(String destinationAddressName) {
        this.destinationAddressName = destinationAddressName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(int pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public GetPending() {
    }

    protected GetPending(Parcel in) {
        uid = in.readString();
        guestId = in.readString();
        pendingAmountFormatted = in.readString();
        paymentMethod = in.readString();
        token = in.readString();
        ccActiveToken = in.readString();
        lastRequestId = in.readString();
        lastRideAmount = in.readString();
        lastRidePayment = in.readString();
        lastRidePaymentMethod = in.readString();
        pickupAddressName = in.readString();
        destinationAddressName = in.readString();
        date = in.readString();
        pendingAmount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(guestId);
        dest.writeString(pendingAmountFormatted);
        dest.writeString(paymentMethod);
        dest.writeString(token);
        dest.writeString(ccActiveToken);
        dest.writeString(lastRequestId);
        dest.writeString(lastRideAmount);
        dest.writeString(lastRidePayment);
        dest.writeString(lastRidePaymentMethod);
        dest.writeString(pickupAddressName);
        dest.writeString(destinationAddressName);
        dest.writeString(date);
        dest.writeInt(pendingAmount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GetPending> CREATOR = new Parcelable.Creator<GetPending>() {
        @Override
        public GetPending createFromParcel(Parcel in) {
            return new GetPending(in);
        }

        @Override
        public GetPending[] newArray(int size) {
            return new GetPending[size];
        }
    };
}
