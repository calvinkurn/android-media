package com.tokopedia.seller.gmsubscribe.data.mapper;

import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.checkout.GMCheckoutServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMSubscribeCheckoutMapper implements Func1<GMCheckoutServiceModel, GMCheckoutDomainModel> {
    @Override
    public GMCheckoutDomainModel call(GMCheckoutServiceModel gmCheckoutServiceModel) {
        return mapServiceToDomain(gmCheckoutServiceModel);
    }

    private GMCheckoutDomainModel mapServiceToDomain(GMCheckoutServiceModel gmCheckoutServiceModel) {
        GMCheckoutDomainModel domainModel = new GMCheckoutDomainModel();
        domainModel.setPaymentUrl(gmCheckoutServiceModel.getPaymentUrl());
        domainModel.setParameter(gmCheckoutServiceModel.getParameter());
        return domainModel;
    }
}
