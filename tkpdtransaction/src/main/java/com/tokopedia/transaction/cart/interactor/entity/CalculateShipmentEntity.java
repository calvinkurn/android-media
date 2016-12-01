package com.tokopedia.transaction.cart.interactor.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 11/30/16.
 */

public class CalculateShipmentEntity {
    @SerializedName("shipment")
    @Expose
    private List<ShipmentEntity> shipment = new ArrayList<>();

    public List<ShipmentEntity> getShipment() {
        return shipment;
    }

    public void setShipment(List<ShipmentEntity> shipment) {
        this.shipment = shipment;
    }
}
