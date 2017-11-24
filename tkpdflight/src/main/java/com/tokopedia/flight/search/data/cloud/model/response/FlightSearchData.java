package com.tokopedia.flight.search.data.cloud.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchData implements ItemType {
    public static final int TYPE = 6785;

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public String getId() {
        return id;
    }

    public String getFlightType() {
        return type;
    }

    public int getType() {
        return TYPE;
    }

    public Attributes getAttributes() {
        return attributes;
    }
}
