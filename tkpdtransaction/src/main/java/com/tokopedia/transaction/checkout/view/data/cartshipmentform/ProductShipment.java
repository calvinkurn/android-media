package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ProductShipment {
    private String shipmentId;
    private List<String> serviceId = new ArrayList<>();

    public String getShipmentId() {
        return shipmentId;
    }

    public List<String> getServiceId() {
        return serviceId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public void setServiceId(List<String> serviceId) {
        this.serviceId = serviceId;
    }
}
