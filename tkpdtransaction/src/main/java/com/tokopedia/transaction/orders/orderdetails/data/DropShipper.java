package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DropShipper {

    @SerializedName("name")
    @Expose
    private String dropShipperName;

    @SerializedName("phone")
    @Expose
    private String dropShipperPhone;

    public String getDropShipperName() {
        return dropShipperName;
    }

    public void setDropShipperName(String dropShipperName) {
        this.dropShipperName = dropShipperName;
    }

    public String getDropShipperPhone() {
        return dropShipperPhone;
    }

    public void setDropShipperPhone(String dropShipperPhone) {
        this.dropShipperPhone = dropShipperPhone;
    }
}
