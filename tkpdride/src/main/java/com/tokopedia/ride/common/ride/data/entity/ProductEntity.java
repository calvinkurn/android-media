package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/14/17.
 */

public class ProductEntity {
    /**
     {
     "capacity": 1,
     "cash_enabled": true,
     "description": "FAST, CHEAP, RELIABLE",
     "display_name": "uberMotor",
     "image": "http://d1a3f4spazzrp4.cloudfront.net/car-types/mono/mono-scooter.png",
     "price_details": {
     "base": 1000,
     "cancellation_fee": 5000,
     "cost_per_distance": 1250,
     "cost_per_minute": 0,
     "currency_code": "IDR",
     "distance_unit": "km",
     "minimum": 5000,
     "service_fees": []
     },
     "product_group": "uberx",
     "product_id": "89da0988-cb4f-4c85-b84f-aac2f5115068",
     "shared": false,
     "short_description": "uberMOTOR"
     }
     **/
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
    @SerializedName("price_details")
    @Expose
    PriceDetailEntity priceDetailEntity;


    public ProductEntity() {
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

    public PriceDetailEntity getPriceDetailEntity() {
        return priceDetailEntity;
    }
}
