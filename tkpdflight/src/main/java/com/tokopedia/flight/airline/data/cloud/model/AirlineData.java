package com.tokopedia.flight.airline.data.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/30/2017.
 */

public class AirlineData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public String getId() {
        return id;
    }

    public Attributes getAttributes() {
        return attributes;
    }
}
