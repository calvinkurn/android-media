package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 12/6/17.
 */

public class BaggageEntity {
    @SerializedName("is_up_to")
    @Expose
    private boolean isUpTo;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("value")
    @Expose
    private String value;

    public BaggageEntity() {
    }

    public boolean isUpTo() {
        return isUpTo;
    }

    public void setUpTo(boolean upTo) {
        isUpTo = upTo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
