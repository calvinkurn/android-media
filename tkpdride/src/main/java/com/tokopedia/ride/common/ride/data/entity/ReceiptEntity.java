package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 3/31/17.
 */

public class ReceiptEntity {
    /**
     * "charge_adjustments": [
     * {
     * "amount": "2.25",
     * "name": "Booking Fee",
     * "type": "booking_fee"
     * }
     * ],
     * "currency_code": "USD",
     * "distance": "1.87",
     * "distanceLabel": "miles",
     * "duration": "00:11:32",
     * "guest": {
     * "email": "farwa.guest@email.com",
     * "first_name": "Farwa",
     * "guest_id": "215ce9ff-d76d-4535-b795-33a3b80b9154",
     * "last_name": "Guest",
     * "phone_number": "+15555555555"
     * },
     * "request_id": "e4f9d735-e317-4fef-bc93-79b28d69e514",
     * "subtotal": "$9.00",
     * "total_charged": "$9.00",
     * "total_fare": "$9.00",
     * "total_owed": null
     */
    @SerializedName("charge_adjustments")
    @Expose
    List<ChargeAdjustmentEntity> chargeAdjustments;
    @SerializedName("currency_code")
    @Expose
    String currencyCode;
    @SerializedName("distance")
    @Expose
    String distance;
    @SerializedName("distance_label")
    @Expose
    String distanceLabel;
    @SerializedName("duration")
    @Expose
    String duration;
    @SerializedName("guest")
    @Expose
    GuestEntity guest;
    @SerializedName("request_id")
    @Expose
    String requestId;
    @SerializedName("subtotal")
    @Expose
    String subtotal;
    @SerializedName("total_charged")
    @Expose
    String totalCharged;
    @SerializedName("total_fare")
    @Expose
    String totalFare;
    @SerializedName("total_owe")
    @Expose
    String totalOwe;
    @SerializedName("payment")
    @Expose
    RecieptPaymentEntity payment;
    @SerializedName("ride_offer")
    @Expose
    RideOfferEntity rideOffer;
    @SerializedName("discount_amount")
    @Expose
    float discountAmount;
    @SerializedName("cashback_amount")
    @Expose
    float cashbackAmount;
    @SerializedName("pending_payment")
    @Expose
    PendingPaymentEntity pendingPayment;
    @SerializedName("tip_list")
    @Expose
    TipListEntity tipList;

    public ReceiptEntity() {
    }

    public List<ChargeAdjustmentEntity> getChargeAdjustments() {
        return chargeAdjustments;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getDistance() {
        return distance;
    }

    public String getDistanceLabel() {
        return distanceLabel;
    }

    public String getDuration() {
        return duration;
    }

    public GuestEntity getGuest() {
        return guest;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getTotalCharged() {
        return totalCharged;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public String getTotalOwe() {
        return totalOwe;
    }

    public RecieptPaymentEntity getPayment() {
        return payment;
    }

    public RideOfferEntity getRideOffer() {
        return rideOffer;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public float getCashbackAmount() {
        return cashbackAmount;
    }

    public PendingPaymentEntity getPendingPayment() {
        return pendingPayment;
    }

    public TipListEntity getTipList() {
        return tipList;
    }
}
