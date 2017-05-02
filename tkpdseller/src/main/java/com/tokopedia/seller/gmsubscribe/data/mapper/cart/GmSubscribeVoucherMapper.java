package com.tokopedia.seller.gmsubscribe.data.mapper.cart;

import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.voucher.GmVoucherServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.exception.GmVoucherCheckException;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmVoucherCheckDomainModel;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/3/17.
 */
public class GmSubscribeVoucherMapper implements Func1<GmVoucherServiceModel, GmVoucherCheckDomainModel> {

    @Inject
    public GmSubscribeVoucherMapper() {
    }

    @Override
    public GmVoucherCheckDomainModel call(GmVoucherServiceModel serviceModel) {
        return mapServiceToDomain(serviceModel);
    }

    private GmVoucherCheckDomainModel mapServiceToDomain(GmVoucherServiceModel gmVoucherServiceModel) {
        GmVoucherCheckDomainModel domainModel = new GmVoucherCheckDomainModel();
        if (gmVoucherServiceModel.getData().getSuccess()) {
            domainModel.setMessage(gmVoucherServiceModel.getData().getMessageSuccess());
        } else {
            throw new GmVoucherCheckException(gmVoucherServiceModel.getData().getAttributes().getDetail());
        }
        return domainModel;
    }
}
