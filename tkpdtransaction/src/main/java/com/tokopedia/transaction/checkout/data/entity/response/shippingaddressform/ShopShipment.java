package com.tokopedia.transaction.checkout.data.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ShopShipment {

    @SerializedName("ship_id")
    @Expose
    private int shipId;
    @SerializedName("ship_name")
    @Expose
    private String shipName;
    @SerializedName("ship_code")
    @Expose
    private String shipCode;
    @SerializedName("ship_logo")
    @Expose
    private String shipLogo;
    @SerializedName("ship_prods")
    @Expose
    private List<ShipProd> shipProds = new ArrayList<>();
    @SerializedName("is_dropship_enabled")
    @Expose
    private int isDropshipEnabled;

    public int getShipId() {
        return shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public String getShipCode() {
        return shipCode;
    }

    public String getShipLogo() {
        return shipLogo;
    }

    public List<ShipProd> getShipProds() {
        return shipProds;
    }

    public int getIsDropshipEnabled() {
        return isDropshipEnabled;
    }
}
