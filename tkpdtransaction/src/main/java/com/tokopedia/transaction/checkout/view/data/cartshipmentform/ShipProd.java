package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ShipProd {
    private int shipProdId;
    private String shipProdName;
    private String shipGroupName;
    private int shipGroupId;
    private int additionalFee;
    private int minimumWeight;

    public void setShipProdId(int shipProdId) {
        this.shipProdId = shipProdId;
    }

    public void setShipProdName(String shipProdName) {
        this.shipProdName = shipProdName;
    }

    public void setShipGroupName(String shipGroupName) {
        this.shipGroupName = shipGroupName;
    }

    public void setShipGroupId(int shipGroupId) {
        this.shipGroupId = shipGroupId;
    }

    public void setAdditionalFee(int additionalFee) {
        this.additionalFee = additionalFee;
    }

    public void setMinimumWeight(int minimumWeight) {
        this.minimumWeight = minimumWeight;
    }

    public int getShipProdId() {
        return shipProdId;
    }

    public String getShipProdName() {
        return shipProdName;
    }

    public String getShipGroupName() {
        return shipGroupName;
    }

    public int getShipGroupId() {
        return shipGroupId;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public int getMinimumWeight() {
        return minimumWeight;
    }
}
