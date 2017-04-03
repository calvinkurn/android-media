package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/24/17.
 */

public class RideRequestEntity {
    /**
     * {
     * "destination": {
     * "latitude": -6.1753924,
     * "longitude": 106.8271528
     * },
     * "driver": null,
     * "eta": null,
     * "guest": {
     * "email": "jane.emfgS@email.com",
     * "first_name": "Jane",
     * "guest_id": "1bc5946f-928d-43bf-9b55-6c0d77e2ee50",
     * "last_name": "Smith",
     * "phone_number": "+14150001234"
     * },
     * "location": null,
     * "pickup": {
     * "latitude": -6.1901543,
     * "longitude": 106.7986657
     * },
     * "product_id": "89da0988-cb4f-4c85-b84f-aac2f5115068",
     * "request_id": "d87c9eb6-126f-4682-8789-ccfe5145760a",
     * "shared": false,
     * "status": "processing",
     * "vehicle": null
     * }
     */
    @SerializedName("request_id")
    @Expose
    String requestId;
    @SerializedName("product_id")
    @Expose
    String productId;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("vehicle")
    @Expose
    VehicleEntity vehicle;
    @SerializedName("driver")
    @Expose
    DriverEntity driver;
    @SerializedName("location")
    @Expose
    LocationEntity location;
    @SerializedName("surge_multiplier")
    @Expose
    double surgeMultiplier;
    @SerializedName("shared")
    @Expose
    boolean shared;
    @SerializedName("guest")
    @Expose
    GuestEntity guest;
    @SerializedName("pickup")
    @Expose
    LocationLatLngEntity pickupd;
    @SerializedName("destination")
    @Expose
    LocationLatLngEntity destination;

    public RideRequestEntity() {
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

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public DriverEntity getDriver() {
        return driver;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public double getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public boolean isShared() {
        return shared;
    }

    public GuestEntity getGuest() {
        return guest;
    }

    public LocationLatLngEntity getPickupd() {
        return pickupd;
    }

    public LocationLatLngEntity getDestination() {
        return destination;
    }
}
