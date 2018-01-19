package com.tokopedia.flight.booking.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 11/13/17.
 */

public class FlightCartRequest {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private CartAttributesRequest cartAttributes;

    private String idEmpotencyKey;

    public FlightCartRequest() {
    }

    public String getType() {
        return type;
    }

    public CartAttributesRequest getCartAttributes() {
        return cartAttributes;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCartAttributes(CartAttributesRequest cartAttributes) {
        this.cartAttributes = cartAttributes;
    }

    public String getIdEmpotencyKey() {
        return idEmpotencyKey;
    }

    public void setIdEmpotencyKey(String idEmpotencyKey) {
        this.idEmpotencyKey = idEmpotencyKey;
    }
}
