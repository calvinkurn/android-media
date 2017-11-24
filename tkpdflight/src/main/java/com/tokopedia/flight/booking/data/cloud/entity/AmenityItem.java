package com.tokopedia.flight.booking.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 11/13/17.
 */

public class AmenityItem {
    /*{
        "id": "15.0;0.0",
            "price": "Rp 0",
            "price_numeric": 0,
            "description": "15kg",
            "currency": "IDR"
    },*/
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_numeric")
    @Expose
    private int priceNumeric;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("currency")
    @Expose
    private String currency;

    public AmenityItem() {
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public int getPriceNumeric() {
        return priceNumeric;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrency() {
        return currency;
    }
}
