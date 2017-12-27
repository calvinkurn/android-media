package com.tokopedia.seller.product.edit.data.source.cloud.model;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductWholesaleServiceModel {
    private double price;
    private int qtyMax;
    private int qtyMin;

    public double getPrice() {
        return price;
    }

    public int getQtyMax() {
        return qtyMax;
    }

    public int getQtyMin() {
        return qtyMin;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQtyMax(int qtyMax) {
        this.qtyMax = qtyMax;
    }

    public void setQtyMin(int qtyMin) {
        this.qtyMin = qtyMin;
    }
}
