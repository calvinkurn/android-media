package com.tokopedia.transaction.checkout.domain.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class SetShippingAddressRequest {

    @SerializedName("cart_id")
    @Expose
    private int cartId;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("qty")
    @Expose
    private int qty;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("address_id")
    @Expose
    private int addressId;

    private SetShippingAddressRequest(Builder builder) {
        cartId = builder.cartId;
        productId = builder.productId;
        qty = builder.qty;
        note = builder.note;
        addressId = builder.addressId;
    }


    public static final class Builder {
        private int cartId;
        private int productId;
        private int qty;
        private String note;
        private int addressId;

        public Builder() {
        }

        public Builder cartId(int val) {
            cartId = val;
            return this;
        }

        public Builder productId(int val) {
            productId = val;
            return this;
        }

        public Builder qty(int val) {
            qty = val;
            return this;
        }

        public Builder note(String val) {
            note = val;
            return this;
        }

        public Builder addressId(int val) {
            addressId = val;
            return this;
        }

        public SetShippingAddressRequest build() {
            return new SetShippingAddressRequest(this);
        }
    }
}
