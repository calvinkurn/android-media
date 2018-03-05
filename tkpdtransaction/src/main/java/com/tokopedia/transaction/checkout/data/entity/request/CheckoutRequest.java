package com.tokopedia.transaction.checkout.data.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class CheckoutRequest {

    @SerializedName("promo_code")
    @Expose
    public String promoCode;
    @SerializedName("is_donation")
    @Expose
    public int isDonation;
    @SerializedName("data")
    @Expose
    public List<DataCheckoutRequest> data = new ArrayList<>();

    private CheckoutRequest(Builder builder) {
        promoCode = builder.promoCode;
        isDonation = builder.isDonation;
        data = builder.data;
    }


    public static final class Builder {
        private String promoCode;
        private int isDonation;
        private List<DataCheckoutRequest> data;

        public Builder() {
        }

        public Builder promoCode(String val) {
            promoCode = val;
            return this;
        }

        public Builder isDonation(int val) {
            isDonation = val;
            return this;
        }

        public Builder data(List<DataCheckoutRequest> val) {
            data = val;
            return this;
        }

        public CheckoutRequest build() {
            return new CheckoutRequest(this);
        }
    }
}
