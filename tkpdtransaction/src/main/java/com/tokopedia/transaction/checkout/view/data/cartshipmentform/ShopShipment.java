package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ShopShipment {

    private int shipId;
    private String shipName;
    private String shipCode;
    private String shipLogo;
    private List<ShipProd> shipProds = new ArrayList<>();
    private boolean isDropshipEnabled;

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipLogo() {
        return shipLogo;
    }

    public void setShipLogo(String shipLogo) {
        this.shipLogo = shipLogo;
    }

    public List<ShipProd> getShipProds() {
        return shipProds;
    }

    public void setShipProds(List<ShipProd> shipProds) {
        this.shipProds = shipProds;
    }

    public boolean isDropshipEnabled() {
        return isDropshipEnabled;
    }

    public void setDropshipEnabled(boolean dropshipEnabled) {
        isDropshipEnabled = dropshipEnabled;
    }
}
