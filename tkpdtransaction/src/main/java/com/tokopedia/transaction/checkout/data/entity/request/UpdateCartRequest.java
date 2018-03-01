package com.tokopedia.transaction.checkout.data.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 20/02/18.
 */

public class UpdateCartRequest {
    @SerializedName("cart_id")
    @Expose
    private int cartId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("notes")
    @Expose
    private String notes;

    private UpdateCartRequest(Builder builder) {
        cartId = builder.cartId;
        quantity = builder.quantity;
        notes = builder.notes;
    }

    public static final class Builder {
        private int cartId;
        private int quantity;
        private String notes;

        public Builder() {
        }

        public Builder cartId(int val) {
            cartId = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder notes(String val) {
            notes = val;
            return this;
        }

        public UpdateCartRequest build() {
            return new UpdateCartRequest(this);
        }
    }
}
