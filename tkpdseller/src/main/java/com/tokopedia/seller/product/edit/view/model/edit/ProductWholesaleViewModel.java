
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductWholesaleViewModel {

    @SerializedName("min_qty")
    @Expose
    private long minQty;
    @SerializedName("price_value")
    @Expose
    private double priceValue;

    public long getMinQty() {
        return minQty;
    }

    public void setMinQty(long minQty) {
        this.minQty = minQty;
    }

    public double getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(double priceValue) {
        this.priceValue = priceValue;
    }

}
