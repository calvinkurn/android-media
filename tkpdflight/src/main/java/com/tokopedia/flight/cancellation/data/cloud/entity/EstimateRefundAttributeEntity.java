package com.tokopedia.flight.cancellation.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/9/18.
 */

public class EstimateRefundAttributeEntity {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("value_numeric")
    @Expose
    private long valueNumeric;

    public EstimateRefundAttributeEntity() {
    }

    public String getValue() {
        return value;
    }

    public long getValueNumeric() {
        return valueNumeric;
    }
}
