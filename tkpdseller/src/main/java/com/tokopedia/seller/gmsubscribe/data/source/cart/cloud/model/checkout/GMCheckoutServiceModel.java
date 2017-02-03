package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMCheckoutServiceModel {

    @SerializedName("paymentURL")
    @Expose
    private String paymentUrl;

    @SerializedName("parameter1")
    @Expose
    private String parameter;

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
