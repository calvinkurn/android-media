package com.tokopedia.transaction.checkout.domain.mapper;

import com.tokopedia.transaction.checkout.data.entity.response.checkout.CheckoutDataResponse;
import com.tokopedia.transaction.checkout.domain.datamodel.cartcheckout.CheckoutData;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public interface ICheckoutMapper {

    CheckoutData convertCheckoutData(CheckoutDataResponse checkoutDataResponse);
}
