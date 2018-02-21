package com.tokopedia.transaction.checkout.domain.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ProductShipmentMapping {
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("shipping_ids")
    @Expose
    private String shippingIds;
    @SerializedName("service_ids")
    @Expose
    private List<ServiceId> serviceIds = new ArrayList<>();

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
