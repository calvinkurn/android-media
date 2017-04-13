package com.tokopedia.seller.product.data.source.db.model;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductWholesaleDraftModel {
    private String price;
    private String qtyMax;
    private String qtyMin;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQtyMax() {
        return qtyMax;
    }

    public void setQtyMax(String qtyMax) {
        this.qtyMax = qtyMax;
    }

    public String getQtyMin() {
        return qtyMin;
    }

    public void setQtyMin(String qtyMin) {
        this.qtyMin = qtyMin;
    }
}
