package com.tokopedia.seller.product.data.source.cloud.model;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductWholesaleServiceModel {
    private String price;
    private String qtyMax;
    private String qtyMin;

    public String getPrice() {
        return price;
    }

    public String getQtyMax() {
        return qtyMax;
    }

    public String getQtyMin() {
        return qtyMin;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQtyMax(String qtyMax) {
        this.qtyMax = qtyMax;
    }

    public void setQtyMin(String qtyMin) {
        this.qtyMin = qtyMin;
    }
}
