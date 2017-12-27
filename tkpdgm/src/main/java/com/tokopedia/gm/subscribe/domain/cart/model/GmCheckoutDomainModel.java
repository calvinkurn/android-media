package com.tokopedia.gm.subscribe.domain.cart.model;

/**
 * Created by sebastianuskh on 1/31/17.
 */
public class GmCheckoutDomainModel {
    private String paymentUrl;
    private String parameter;
    private String callbackUrl;
    private Integer paymentId;

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

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
}
