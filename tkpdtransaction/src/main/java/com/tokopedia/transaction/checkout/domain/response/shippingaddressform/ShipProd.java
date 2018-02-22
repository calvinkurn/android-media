package com.tokopedia.transaction.checkout.domain.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ShipProd {
    @SerializedName("ship_prod_id")
    @Expose
    private int shipProdId;
    @SerializedName("ship_prod_name")
    @Expose
    private String shipProdName;
    @SerializedName("ship_group_name")
    @Expose
    private String shipGroupName;
    @SerializedName("ship_group_id")
    @Expose
    private int shipGroupId;
    @SerializedName("additional_fee")
    @Expose
    private int additionalFee;
    @SerializedName("minimum_weight")
    @Expose
    private int minimumWeight;

    public int getShipProdId() {
        return shipProdId;
    }

    public String getShipProdName() {
        return shipProdName;
    }

    public String getShipGroupName() {
        return shipGroupName;
    }

    public int getShipGroupId() {
        return shipGroupId;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public int getMinimumWeight() {
        return minimumWeight;
    }
}
