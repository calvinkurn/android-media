package com.tokopedia.seller.product.edit.data.source.db.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductWholesaleDraftModel {
    @SerializedName("product_wholesale_price")
    private double price;

    @SerializedName("product_qty_max")
    private int qtyMax;

    @SerializedName("product_qty_min")
    private int qtyMin;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQtyMax() {
        return qtyMax;
    }

    public void setQtyMax(int qtyMax) {
        this.qtyMax = qtyMax;
    }

    public int getQtyMin() {
        return qtyMin;
    }

    public void setQtyMin(int qtyMin) {
        this.qtyMin = qtyMin;
    }
}
