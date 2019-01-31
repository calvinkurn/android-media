package com.tokopedia.inbox.rescenter.shipping.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/3/16.
 */
public class ResCenterKurir {

    @SerializedName("shippingList")
    @Expose
    private List<Kurir> list;

    public List<Kurir> getList() {
        return list;
    }

    public void setList(List<Kurir> list) {
        this.list = list;
    }

    public static class Kurir {

        @SerializedName("id")
        @Expose
        private String shipmentId;
        @SerializedName("name")
        @Expose
        private String shipmentName;

        public String getShipmentId() {
            return shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
        }

        public String getShipmentName() {
            return shipmentName;
        }

        public void setShipmentName(String shipmentName) {
            this.shipmentName = shipmentName;
        }
    }
}
