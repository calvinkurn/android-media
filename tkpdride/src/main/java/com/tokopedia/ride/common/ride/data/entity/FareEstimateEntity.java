package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/21/17.
 */

public class FareEstimateEntity {
    @SerializedName("fare")
    @Expose
    private FareEntity fare;
    @SerializedName("trip")
    @Expose
    private TripEntity trip;

    @SerializedName("estimate")
    @Expose
    private EstimateEntity estimate;

    @SerializedName("pickup_estimate")
    @Expose
    private String pickupEstimate;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("discount_amount")
    @Expose
    private int discountAmount;
    @SerializedName("cashback_amount")
    @Expose
    private int cashbackAmount;
    @SerializedName("cashback_top_cash_amount")
    @Expose
    private int cashbackTopCashAmount;
    @SerializedName("cashback_voucher_amount")
    @Expose
    private int cashbackVoucherAmount;
    @SerializedName("extra_amount")
    @Expose
    private int extraAmount;
    @SerializedName("message_success")
    @Expose
    private String messageSuccess;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private FareAttributeEntity attributes;

    public FareEstimateEntity() {
    }

    public FareEntity getFare() {
        return fare;
    }

    public void setFare(FareEntity fare) {
        this.fare = fare;
    }

    public TripEntity getTrip() {
        return trip;
    }

    public void setTrip(TripEntity trip) {
        this.trip = trip;
    }

    public String getPickupEstimate() {
        return pickupEstimate;
    }

    public EstimateEntity getEstimate() {
        return estimate;
    }

    public void setEstimate(EstimateEntity estimate) {
        this.estimate = estimate;
    }

    public void setPickupEstimate(String pickupEstimate) {
        this.pickupEstimate = pickupEstimate;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getCashbackAmount() {
        return cashbackAmount;
    }

    public int getCashbackTopCashAmount() {
        return cashbackTopCashAmount;
    }

    public int getCashbackVoucherAmount() {
        return cashbackVoucherAmount;
    }

    public int getExtraAmount() {
        return extraAmount;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public FareAttributeEntity getAttributes() {
        return attributes;
    }
}
