package com.tokopedia.loyalty.router;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CouponListResult;

import rx.Observable;


/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ITkpdLoyaltyModuleRouter {

    Observable<CheckPromoCodeCartListResult> tkpdLoyaltyGetCheckPromoCodeCartListResultObservable(
            String promoCode
    );

    Observable<CheckPromoCodeCartShipmentResult> tkpdLoyaltyGetCheckPromoCodeCartShipmentResultObservable(
            CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest
    );

    Observable<CouponListResult> tkpdLoyaltyGetCouponListObservable(String page, String pageSize);

}
