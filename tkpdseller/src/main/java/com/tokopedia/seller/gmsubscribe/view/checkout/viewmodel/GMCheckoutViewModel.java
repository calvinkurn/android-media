package com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel;

import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMCheckoutViewModel {
    private String paymentUrl;
    private String parameter;
    private String callbackUrl;
    private Integer paymentId;

    public static GMCheckoutViewModel mapFromDomain(GMCheckoutDomainModel domainModel) {
        GMCheckoutViewModel vieModel = new GMCheckoutViewModel();
        vieModel.setPaymentUrl(domainModel.getPaymentUrl());
        vieModel.setParameter(domainModel.getParameter());
        vieModel.setCallbackUrl(domainModel.getCallbackUrl());
        vieModel.setPaymentId(domainModel.getPaymentId());
        return vieModel;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public String getParameter() {
        return parameter;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
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
