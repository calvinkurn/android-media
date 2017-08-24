
package com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderShipment {

    @SerializedName("shipment_logo")
    @Expose
    private String shipmentLogo;
    @SerializedName("shipment_package_id")
    @Expose
    private String shipmentPackageId;
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("shipment_product")
    @Expose
    private String shipmentProduct;
    @SerializedName("shipment_name")
    @Expose
    private String shipmentName;
    @SerializedName("same_day")
    @Expose
    private int sameDay;

    public String getShipmentLogo() {
        return shipmentLogo;
    }

    public void setShipmentLogo(String shipmentLogo) {
        this.shipmentLogo = shipmentLogo;
    }

    public String getShipmentPackageId() {
        return shipmentPackageId;
    }

    public void setShipmentPackageId(String shipmentPackageId) {
        this.shipmentPackageId = shipmentPackageId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentProduct() {
        return shipmentProduct;
    }

    public void setShipmentProduct(String shipmentProduct) {
        this.shipmentProduct = shipmentProduct;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public int getSameDay() {
        return sameDay;
    }

    public void setSameDay(int sameDay) {
        this.sameDay = sameDay;
    }

}
