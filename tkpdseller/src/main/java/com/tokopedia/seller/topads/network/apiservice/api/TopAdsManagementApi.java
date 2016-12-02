package com.tokopedia.seller.topads.network.apiservice.api;

import com.tokopedia.seller.topads.model.exchange.CreditResponse;
import com.tokopedia.seller.topads.model.exchange.DepositResponse;
import com.tokopedia.seller.topads.model.exchange.ProductResponse;
import com.tokopedia.seller.topads.model.exchange.ShopResponse;
import com.tokopedia.seller.topads.model.exchange.StatisticResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.model.exchange.TotalAdResponse;

/**
 * Created by zulfikarrahman on 11/4/16.
 */
public interface TopAdsManagementApi {

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_STATISTIC)
    Observable<Response<StatisticResponse>> getDashboardStatistic(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_DEPOSIT)
    Observable<Response<DepositResponse>> getDashboardDeposit(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_TOTAL_AD)
    Observable<Response<TotalAdResponse>> getDashboardTotalAd(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_PRODUCT)
    Observable<Response<ProductResponse>> getDashboardProduct(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_SHOP)
    Observable<Response<ShopResponse>> getDashboardShop(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_CREDIT)
    Observable<Response<CreditResponse>> getDashboardCredit(@QueryMap Map<String, String> params);
}
