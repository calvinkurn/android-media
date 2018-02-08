package com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author normansyahputa on 4/25/17.
 */

public class WholesalePrice {

    @SerializedName("wholesale_min")
    @Expose
    private Integer wholesaleMin;
    @SerializedName("wholesale_price")
    @Expose
    private String wholesalePrice;
    @SerializedName("wholesale_max")
    @Expose
    private Integer wholesaleMax;

    public Integer getWholesaleMin() {
        return wholesaleMin;
    }

    public void setWholesaleMin(Integer wholesaleMin) {
        this.wholesaleMin = wholesaleMin;
    }

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public Integer getWholesaleMax() {
        return wholesaleMax;
    }

    public void setWholesaleMax(Integer wholesaleMax) {
        this.wholesaleMax = wholesaleMax;
    }

}
