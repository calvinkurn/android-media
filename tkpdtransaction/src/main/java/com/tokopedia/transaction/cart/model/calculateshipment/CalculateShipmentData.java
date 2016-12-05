package com.tokopedia.transaction.cart.model.calculateshipment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/3/16.
 */

public class CalculateShipmentData {
    @SerializedName("shipment")
    @Expose
    private List<Shipment> shipment = new ArrayList<>();

    public List<Shipment> getShipment() {
        return shipment;
    }

    public void setShipment(List<Shipment> shipment) {
        this.shipment = shipment;
    }
}
