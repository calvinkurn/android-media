
package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GMCheckoutServiceModel {

    @SerializedName("paymentURL")
    @Expose
    private String paymentURL;
    @SerializedName("parameter1")
    @Expose
    private String parameter1;
    @SerializedName("callbackurl")
    @Expose
    private String callbackurl;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;

    public String getPaymentURL() {
        return paymentURL;
    }

    public void setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

}
