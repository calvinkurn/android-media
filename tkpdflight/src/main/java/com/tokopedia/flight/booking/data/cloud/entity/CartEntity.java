package com.tokopedia.flight.booking.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 11/13/17.
 */

public class CartEntity {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private Attribute attribute;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Attribute getAttribute() {
        return attribute;
    }
}

