package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ProductShipmentMapping {
    private String shipmentId;
    private String shippingIds;
    private List<ServiceId> serviceIds = new ArrayList<>();

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public void setShippingIds(String shippingIds) {
        this.shippingIds = shippingIds;
    }

    public void setServiceIds(List<ServiceId> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public String getShippingIds() {
        return shippingIds;
    }

    public List<ServiceId> getServiceIds() {
        return serviceIds;
    }
}
