package com.tokopedia.transaction.checkout.view.data;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShippingFeeBannerModel {

    private boolean visible;
    private String shipmentFeeDiscount;

    public String getShipmentFeeDiscount() {
        return shipmentFeeDiscount;
    }

    public void setShipmentFeeDiscount(String shipmentFeeDiscount) {
        this.shipmentFeeDiscount = shipmentFeeDiscount;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
