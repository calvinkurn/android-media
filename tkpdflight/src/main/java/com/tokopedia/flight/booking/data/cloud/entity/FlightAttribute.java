package com.tokopedia.flight.booking.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 11/13/17.
 */

public class FlightAttribute {
    @SerializedName("new_price")
    @Expose
    private List<NewFarePrice> newPrices;
    @SerializedName("is_domestic")
    @Expose
    private boolean isDomestic;
    @SerializedName("refresh_time")
    @Expose
    private int refreshTime;
    @SerializedName("amenities")
    @Expose
    private List<Amenity> amenities;

    public FlightAttribute() {
    }

    public List<NewFarePrice> getNewPrices() {
        return newPrices;
    }

    public boolean isDomestic() {
        return isDomestic;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }
}
