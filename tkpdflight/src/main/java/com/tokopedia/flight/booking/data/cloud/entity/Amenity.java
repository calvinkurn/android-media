package com.tokopedia.flight.booking.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 11/13/17.
 */

public class Amenity {
    public static final int LUGGAGE = 1;
    public static final int MEAL = 2;
    /*{
        "key": "_CGK_DPS_7538",
            "amenity_type": 1,
            "description": "Jakarta - Denpasar",
            "items": [
        {*/
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("amenity_type")
    @Expose
    private int type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("items")
    @Expose
    private List<AmenityItem> items;

    public Amenity() {
    }

    public String getKey() {
        return key;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public List<AmenityItem> getItems() {
        return items;
    }
}
