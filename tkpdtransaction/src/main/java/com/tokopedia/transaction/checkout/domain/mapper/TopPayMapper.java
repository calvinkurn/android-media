package com.tokopedia.transaction.checkout.domain.mapper;

import com.tokopedia.transaction.checkout.domain.datamodel.toppay.ThanksTopPayData;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public class TopPayMapper implements ITopPayMapper {

    @Inject
    public TopPayMapper() {
    }

    @Override
    public ThanksTopPayData convertThanksTopPayData(
            com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData
                    thanksTopPayDataResponse) {
        return new ThanksTopPayData.Builder()
                .isSuccess(thanksTopPayDataResponse.getIsSuccess() == 1)
                .build();
    }
}
