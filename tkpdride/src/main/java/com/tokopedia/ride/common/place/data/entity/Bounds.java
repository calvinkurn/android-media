package com.tokopedia.ride.common.place.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by zeta on 2/12/2017.
 */

public class Bounds {
    @SerializedName("northeast")
    @Expose
    private Location northeast;
    @SerializedName("southwest")
    @Expose
    private Location southwest;

    public Location getNortheast() {
        return northeast;
    }

    public void setNortheast(Location northeast) {
        this.northeast = northeast;
    }

    public Location getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Location southwest) {
        this.southwest = southwest;
    }
}
