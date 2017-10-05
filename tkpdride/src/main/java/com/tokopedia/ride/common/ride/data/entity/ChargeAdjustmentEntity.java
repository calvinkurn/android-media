package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/4/17.
 */

public class ChargeAdjustmentEntity {
    @SerializedName("amount")
    @Expose
    String amount;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("type")
    @Expose
    String chargeType;

    public ChargeAdjustmentEntity() {
    }

    public String getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getChargeType() {
        return chargeType;
    }
}
