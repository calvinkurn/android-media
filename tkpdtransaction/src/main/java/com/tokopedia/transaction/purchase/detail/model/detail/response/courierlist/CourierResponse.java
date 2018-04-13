package com.tokopedia.transaction.purchase.detail.model.detail.response.courierlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class CourierResponse {

    @SerializedName("shipment")
    @Expose
    private List<Shipment> shipment = new ArrayList<Shipment>();

    /**
     *
     * @return
     *     The shipment
     */
    public List<Shipment> getShipment() {
        return shipment;
    }

    /**
     *
     * @param shipment
     *     The shipment
     */
    public void setShipment(List<Shipment> shipment) {
        this.shipment = shipment;
    }


}
