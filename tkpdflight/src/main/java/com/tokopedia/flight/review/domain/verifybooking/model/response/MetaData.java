
package com.tokopedia.flight.review.domain.verifybooking.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaData {

    @SerializedName("cart_id")
    @Expose
    private String cartId;
    @SerializedName("flight_id")
    @Expose
    private String flightId;


    public String getCartId() {
        return cartId;
    }

    public String getFlightId() {
        return flightId;
    }
}
