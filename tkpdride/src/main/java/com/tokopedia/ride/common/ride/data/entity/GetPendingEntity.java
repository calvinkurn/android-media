package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishal Gupta on 11/13/17.
 */

public class GetPendingEntity {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("guest_id")
    @Expose
    private String guestId;
    @SerializedName("pending_amount")
    @Expose
    private int pendingAmount;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("cc_active_token")
    @Expose
    private String ccActiveToken;
    @SerializedName("last_request_id")
    @Expose
    private String lastRequestId;
    @SerializedName("last_ride_amount")
    @Expose
    private int lastRideAmount;
    @SerializedName("last_ride_payment")
    @Expose
    private int lastRidePayment;
    @SerializedName("last_ride_payment_method")
    @Expose
    private String lastRidePaymentMethod;
    @SerializedName("pickup_address_name")
    @Expose
    private String pickupAddressName;
    @SerializedName("destination_address_name")
    @Expose
    private String destinationAddressName;
    @SerializedName("date")
    @Expose
    private String date;

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

    public int getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(int pendingAmount) {
        this.pendingAmount = pendingAmount;
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

    public int getLastRideAmount() {
        return lastRideAmount;
    }

    public void setLastRideAmount(int lastRideAmount) {
        this.lastRideAmount = lastRideAmount;
    }

    public int getLastRidePayment() {
        return lastRidePayment;
    }

    public void setLastRidePayment(int lastRidePayment) {
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
}
