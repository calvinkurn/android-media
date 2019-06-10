package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntityPackage {

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("display_name")
    @Expose
    private String displayName;

    @SerializedName("city")
    private String city;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "[package:{" +" "+
                "address=" + address +" "+
                "displayName=" + displayName
                + "}]";
    }

}
