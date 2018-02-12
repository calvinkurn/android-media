package com.tokopedia.transaction.checkout.domain.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressRequest {

    @SerializedName("cart_id")
    @Expose
    private int cartId;

    @SerializedName("product_id")
    @Expose
    private int productId;

    @SerializedName("address_id")
    @Expose
    private int addressId;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("qty")
    @Expose
    private int quantity;

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
