package com.tokopedia.seller.topads.network.apiservice.api;

import com.tokopedia.seller.topads.model.data.DataRequestSingleAd;
import com.tokopedia.seller.topads.model.request.AdsActionRequest;
import com.tokopedia.seller.topads.model.response.CreditResponse;
import com.tokopedia.seller.topads.model.response.DepositResponse;
import com.tokopedia.seller.topads.model.response.GroupAdResponse;
import com.tokopedia.seller.topads.model.response.ProductResponse;
import com.tokopedia.seller.topads.model.response.ActionAdsResponse;
import com.tokopedia.seller.topads.model.response.SearchProductResponse;
import com.tokopedia.seller.topads.model.response.ShopResponse;
import com.tokopedia.seller.topads.model.response.StatisticResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.QueryMap;
import rx.Observable;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.model.response.TotalAdResponse;

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
    Observable<Response<CreditResponse>> getDashboardCredit();

    @PATCH(TopAdsNetworkConstant.PATH_ACTION_BULK_AD)
    Observable<Response<ActionAdsResponse>> postActionSingleAds(@Body AdsActionRequest<DataRequestSingleAd> body);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_GROUP)
    Observable<Response<GroupAdResponse>> getDashboardGroup(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_SEARCH_PRODUCT)
    Observable<Response<SearchProductResponse>> getSearchProduct(@QueryMap Map<String, String> params);
}