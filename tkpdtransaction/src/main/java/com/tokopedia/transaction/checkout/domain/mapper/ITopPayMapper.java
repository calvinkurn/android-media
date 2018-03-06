package com.tokopedia.transaction.checkout.domain.mapper;

import com.tokopedia.transaction.checkout.domain.datamodel.toppay.ThanksTopPayData;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public interface ITopPayMapper {

    ThanksTopPayData convertThanksTopPayData(
            com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData thanksTopPayDataResponse
    );
}
