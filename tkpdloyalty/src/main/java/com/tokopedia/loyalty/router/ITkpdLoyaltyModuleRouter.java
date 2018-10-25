package com.tokopedia.loyalty.router;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;

import retrofit2.Converter;


/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ITkpdLoyaltyModuleRouter {

    ChuckInterceptor loyaltyModuleRouterGetCartCheckoutChuckInterceptor();

    FingerprintInterceptor loyaltyModuleRouterGetCartCheckoutFingerPrintInterceptor();

    Converter.Factory loyaltyModuleRouterGetStringResponseConverter();
}
