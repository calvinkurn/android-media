package com.tokopedia.transaction.checkout.data.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class ProductDataCheckoutRequest {

    @SerializedName("product_id")
    @Expose
    public int productId;

    private ProductDataCheckoutRequest(Builder builder) {
        productId = builder.productId;
    }

    public static final class Builder {
        private int productId;

        public Builder() {
        }

        public Builder productId(int val) {
            productId = val;
            return this;
        }

        public ProductDataCheckoutRequest build() {
            return new ProductDataCheckoutRequest(this);
        }
    }
}
