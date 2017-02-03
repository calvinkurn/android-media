package com.tokopedia.seller.gmsubscribe.data.mapper.cart;

import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.voucher.GMVoucherServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.exception.GMVoucherCheckException;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMVoucherCheckDomainModel;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMSubscribeVoucherMapper implements Func1<GMVoucherServiceModel, GMVoucherCheckDomainModel> {
    @Override
    public GMVoucherCheckDomainModel call(GMVoucherServiceModel serviceModel) {
        return mapServiceToDomain(serviceModel);
    }

    private GMVoucherCheckDomainModel mapServiceToDomain(GMVoucherServiceModel gmVoucherServiceModel) {
        GMVoucherCheckDomainModel domainModel = new GMVoucherCheckDomainModel();
        if(gmVoucherServiceModel.getData().getSuccess()) {
            domainModel.setMessage(gmVoucherServiceModel.getData().getMessageSuccess());
        } else {
            throw new GMVoucherCheckException(gmVoucherServiceModel.getData().getAttributes().getDetail());
        }
        return domainModel;
    }
}
