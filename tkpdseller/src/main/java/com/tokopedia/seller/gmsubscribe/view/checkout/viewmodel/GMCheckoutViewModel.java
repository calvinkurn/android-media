package com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel;

import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMCheckoutViewModel {
    private String paymentUrl;
    private String parameter;

    public static GMCheckoutViewModel mapFromDomain(GMCheckoutDomainModel domainModel) {
        GMCheckoutViewModel vieModel = new GMCheckoutViewModel();
        vieModel.setPaymentUrl(domainModel.getPaymentUrl());
        vieModel.setParameter(domainModel.getParameter());
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
}
