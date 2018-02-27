package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.domain.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transaction.checkout.domain.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transaction.checkout.domain.response.couponlist.CouponDataResponse;
import com.tokopedia.transaction.checkout.view.data.voucher.CouponListData;
import com.tokopedia.transaction.checkout.view.data.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.view.data.voucher.PromoCodeCartShipmentData;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public interface IVoucherCouponMapper {

    CouponListData convertCouponListData(CouponDataResponse couponDataResponse);

    PromoCodeCartListData convertPromoCodeCartListData(CheckPromoCodeCartListDataResponse checkPromoCodeCartListDataResponse);

    PromoCodeCartShipmentData convertPromoCodeCartShipmentData(CheckPromoCodeFinalDataResponse checkPromoCodeFinalDataResponse);
}
