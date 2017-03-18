package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/14/17.
 */

public class ProductEntity {
    /**
    {
        "upfront_fare_enabled": true,
            "capacity": 2,
            "product_id": "26546650-e557-4a7b-86e7-6a3942445247",
            "image": "http://d1a3f4spazzrp4.cloudfront.net/car-types/mono/mono-uberx.png",
            "cash_enabled": false,
            "shared": true,
            "short_description": "POOL",
            "display_name": "POOL",
            "product_group": "rideshare",
            "description": "Share the ride, split the cost."
    }
     **/
    @SerializedName("upfront_fare_enabled")
    @Expose
    boolean upfrontFareEnabled;
    @SerializedName("capacity")
    @Expose
    int capacity;
    @SerializedName("product_id")
    @Expose
    String productId;
    @SerializedName("image")
    @Expose
    String image;
    @SerializedName("cash_enabled")
    @Expose
    boolean cashEnabled;
    @SerializedName("shared")
    @Expose
    boolean shared;
    @SerializedName("short_description")
    @Expose
    String shortDescription;
    @SerializedName("display_name")
    @Expose
    String displayName;
    @SerializedName("product_group")
    @Expose
    String productGroup;
    @SerializedName("description")
    @Expose
    String description;


    public ProductEntity() {
    }

    public boolean isUpfrontFareEnabled() {
        return upfrontFareEnabled;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getProductId() {
        return productId;
    }

    public String getImage() {
        return image;
    }

    public boolean isCashEnabled() {
        return cashEnabled;
    }

    public boolean isShared() {
        return shared;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public String getDescription() {
        return description;
    }
}
