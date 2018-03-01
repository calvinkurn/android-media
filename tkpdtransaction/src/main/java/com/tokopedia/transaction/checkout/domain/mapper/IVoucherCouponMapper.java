package com.tokopedia.transaction.checkout.domain.mapper;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CouponListResult;
import com.tokopedia.transaction.checkout.data.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.couponlist.CouponDataResponse;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.CouponListData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public interface IVoucherCouponMapper {

    CouponListData convertCouponListData(CouponDataResponse couponDataResponse);

    PromoCodeCartListData convertPromoCodeCartListData(
            CheckPromoCodeCartListDataResponse checkPromoCodeCartListDataResponse
    );

    PromoCodeCartShipmentData convertPromoCodeCartShipmentData(
            CheckPromoCodeFinalDataResponse checkPromoCodeFinalDataResponse
    );

    CouponListResult convertCouponListResult(CouponListData couponListData);

    CheckPromoCodeCartListResult convertCheckPromoCodeCartListResult(
            PromoCodeCartListData promoCodeCartListData
    );

    CheckPromoCodeCartShipmentResult convertCheckPromoCodeCartShipmentResult(
            PromoCodeCartShipmentData promoCodeCartShipmentData
    );
}
