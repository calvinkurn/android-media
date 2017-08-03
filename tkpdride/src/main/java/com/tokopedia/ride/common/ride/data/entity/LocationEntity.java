package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/24/17.
 */

public class LocationEntity {
    /*"location": {
        "latitude": 37.3382129093,
                "longitude": -121.8863287568,
                "bearing": 328
    },*/

    @SerializedName("latitude")
    @Expose
    double latitude;

    @SerializedName("longitude")
    @Expose
    double longitude;
    @SerializedName("bearing")
    @Expose
    int bearing;

    public LocationEntity() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getBearing() {
        return bearing;
    }
}
