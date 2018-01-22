package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 12/6/17.
 */

public class OrderEntity {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private AttributeEntity attributes;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public AttributeEntity getAttributes() {
        return attributes;
    }
}
