package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by alvarisi on 3/21/17.
 */

public class FareEstimate {
    private Fare fare;
    private Trip trip;
    private Estimate estimate;
    private String pickupEstimate;
    private boolean success;
    private String code;
    private int discountAmount;
    private int cashbackAmount;
    private int cashbackTopCashAmount;
    private int cashbackVoucherAmount;
    private int extraAmount;
    private String messageSuccess;
    private String type;
    private String id;
    private FareAttribute attributes;

    public FareEstimate() {
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getPickupEstimate() {
        return pickupEstimate;
    }

    public void setPickupEstimate(String pickupEstimate) {
        this.pickupEstimate = pickupEstimate;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(int cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public int getCashbackTopCashAmount() {
        return cashbackTopCashAmount;
    }

    public void setCashbackTopCashAmount(int cashbackTopCashAmount) {
        this.cashbackTopCashAmount = cashbackTopCashAmount;
    }

    public int getCashbackVoucherAmount() {
        return cashbackVoucherAmount;
    }

    public void setCashbackVoucherAmount(int cashbackVoucherAmount) {
        this.cashbackVoucherAmount = cashbackVoucherAmount;
    }

    public int getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(int extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FareAttribute getAttributes() {
        return attributes;
    }

    public void setAttributes(FareAttribute attributes) {
        this.attributes = attributes;
    }
}
