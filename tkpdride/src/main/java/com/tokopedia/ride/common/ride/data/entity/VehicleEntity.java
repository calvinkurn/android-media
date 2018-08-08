package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/24/17.
 */

public class VehicleEntity {
    @SerializedName("make")
    @Expose
    String make;
    @SerializedName("model")
    @Expose
    String vehicleModel;
    @SerializedName("license_plate")
    @Expose
    String licensePlate;
    @SerializedName("picture_url")
    @Expose
    String pictureUrl;

    public VehicleEntity() {
    }

    public String getMake() {
        return make;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
