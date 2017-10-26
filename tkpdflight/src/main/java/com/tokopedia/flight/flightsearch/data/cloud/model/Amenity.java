package com.tokopedia.flight.flightsearch.data.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/26/2017.
 */

public class Amenity {

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("label")
    @Expose
    private String label;

    public String getIcon() {
        return icon;
    }
    public String getLabel() {
        return label;
    }

}
