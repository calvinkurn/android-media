package com.tokopedia.seller.opportunity.viewmodel.opportunitylist;

/**
 * Created by nisie on 3/7/17.
 */
public class OrderShipmentViewModel {

    private String shipmentLogo;
    private String shipmentPackageId;
    private String shipmentId;
    private String shipmentProduct;
    private String shipmentName;
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
