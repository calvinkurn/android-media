package com.tokopedia.flight.review.domain.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 12/20/17.
 */

public class FlightCheckoutAttributes {
    @SerializedName("cart_items")
    @Expose
    private List<FlightCheckoutItem> items;
    @SerializedName("promocode")
    @Expose
    private String promocode;

    public List<FlightCheckoutItem> getItems() {
        return items;
    }

    public void setItems(List<FlightCheckoutItem> items) {
        this.items = items;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }
}
