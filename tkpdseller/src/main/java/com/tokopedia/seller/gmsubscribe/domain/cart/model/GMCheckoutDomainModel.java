package com.tokopedia.seller.gmsubscribe.domain.cart.model;

/**
 * Created by sebastianuskh on 1/31/17.
 */
public class GMCheckoutDomainModel {
    private String paymentUrl;
    private String parameter;
    private String callbackUrl;
    private Integer paymentId;

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public String getParameter() {
        return parameter;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public Integer getPaymentId() {
        return paymentId;
    }
}
