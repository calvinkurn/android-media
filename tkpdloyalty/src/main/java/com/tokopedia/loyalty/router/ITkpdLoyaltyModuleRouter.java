package com.tokopedia.loyalty.router;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.router.transactionmodule.sharedata.CouponListResult;

import retrofit2.Converter;
import rx.Observable;


/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ITkpdLoyaltyModuleRouter {

    Observable<CouponListResult> tkpdLoyaltyGetCouponListObservable(String page, String pageSize);

    ChuckInterceptor loyaltyModuleRouterGetCartCheckoutChuckInterceptor();

    FingerprintInterceptor loyaltyModuleRouterGetCartCheckoutFingerPrintInterceptor();

    Converter.Factory loyaltyModuleRouterGetStringResponseConverter();
}
