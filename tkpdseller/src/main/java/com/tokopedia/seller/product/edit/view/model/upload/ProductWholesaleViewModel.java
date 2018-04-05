package com.tokopedia.seller.product.edit.view.model.upload;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Deprecated
public class ProductWholesaleViewModel {
    private int qtyMin;
    private int qtyMax;
    private double price;

    public int getQtyMin() {
        return qtyMin;
    }

    public int getQtyMax() {
        return qtyMax;
    }

    public double getPrice() {
        return price;
    }

    public void setQtyMin(int qtyMin) {
        this.qtyMin = qtyMin;
    }

    public void setQtyMax(int qtyMax) {
        this.qtyMax = qtyMax;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
