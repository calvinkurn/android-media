
package com.tokopedia.flight.review.domain.verifybooking.model.request;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributesData {

    @SerializedName("cart_items")
    @Expose
    private List<CartItem> cartItems = null;
    @SerializedName("promocode")
    @Expose
    private String promocode;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

}
