package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/24/17.
 */

public class RideRequestEntity {
    /**
     * {
     * "request_id": "852b8fdd-4369-4659-9628-e122662ad257",
     * "product_id": "a1111c8c-c720-46c3-8534-2fcdd730040d",
     * "status": "processing",
     * "vehicle": null,
     * "driver": null,
     * "location": null,
     * "eta": 5,
     * "surge_multiplier": null
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
    @SerializedName("eta")
    @Expose
    int eta;
    @SerializedName("surge_multiplier")
    @Expose
    double surgeMultiplier;

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

    public int getEta() {
        return eta;
    }

    public double getSurgeMultiplier() {
        return surgeMultiplier;
    }
}
