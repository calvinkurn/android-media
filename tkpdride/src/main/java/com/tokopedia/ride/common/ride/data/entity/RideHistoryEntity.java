package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistoryEntity {

    @SerializedName("payment")
    @Expose
    PaymentEntity paymentEntity;
    @SerializedName("driver")
    @Expose
    DriverEntity driverEntity;
    @SerializedName("guest")
    @Expose
    GuestEntity guestEntity;
    @SerializedName("pickup")
    @Expose
    LocationLatLngEntity pickupEntity;
    @SerializedName("destination")
    @Expose
    LocationLatLngEntity destination;
    @SerializedName("request_id")
    @Expose
    String requestId;
    @SerializedName("product_id")
    @Expose
    String productId;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("shared")
    @Expose
    boolean shared;
    @SerializedName("vehicle")
    @Expose
    VehicleEntity vehicleEntity;
    @SerializedName("discount_amount")
    @Expose
    float discountAmount;
    @SerializedName("cashback_top_cash_amount")
    @Expose
    float cashbackAmount;
    @SerializedName("create_time")
    @Expose
    String requestTime;
    @SerializedName("rating")
    @Expose
    RatingEntity rating;
    @SerializedName("help_url")
    @Expose
    String helpUrl;

    public RideHistoryEntity() {
    }

    public DriverEntity getDriverEntity() {
        return driverEntity;
    }

    public GuestEntity getGuestEntity() {
        return guestEntity;
    }

    public LocationLatLngEntity getPickupEntity() {
        return pickupEntity;
    }

    public LocationLatLngEntity getDestination() {
        return destination;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getProductId() {
        return productId;
    }

    public String getStatus() {
        return status;
    }

    public boolean isShared() {
        return shared;
    }

    public VehicleEntity getVehicleEntity() {
        return vehicleEntity;
    }

    public PaymentEntity getPaymentEntity() {
        return paymentEntity;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public float getCashbackAmount() {
        return cashbackAmount;
    }

    public RatingEntity getRating() {
        return rating;
    }

    public String getHelpUrl() {
        return helpUrl;
    }
}
