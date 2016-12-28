package com.tokopedia.seller.topads.network.apiservice.api;

import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.DataRequestSingleAd;
import com.tokopedia.seller.topads.model.data.DataResponseActionAds;
import com.tokopedia.seller.topads.model.data.DataStatistic;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.model.request.AdsActionRequest;
import com.tokopedia.seller.topads.model.response.DataResponse;
import com.tokopedia.seller.topads.model.response.PageDataResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zulfikarrahman on 11/4/16.
 */
public interface TopAdsManagementApi {

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_STATISTIC)
    Observable<Response<DataResponse<DataStatistic>>> getDashboardStatistic(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_DEPOSIT)
    Observable<Response<DataResponse<DataDeposit>>> getDashboardDeposit(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_TOTAL_AD)
    Observable<Response<DataResponse<TotalAd>>> getDashboardTotalAd(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_PRODUCT)
    Observable<Response<PageDataResponse<List<Ad>>>> getDashboardProduct(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_SHOP)
    Observable<Response<DataResponse<Ad>>> getDashboardShop(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_CREDIT)
    Observable<Response<DataResponse<List<DataCredit>>>> getDashboardCredit();

    @PATCH(TopAdsNetworkConstant.PATH_ACTION_BULK_AD)
    Observable<Response<DataResponse<DataResponseActionAds>>> postActionSingleAds(@Body AdsActionRequest<DataRequestSingleAd> body);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_GROUP)
    Observable<Response<PageDataResponse<List<GroupAd>>>> getDashboardGroup(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_SEARCH_PRODUCT)
    Observable<Response<DataResponse<List<Product>>>> getSearchProduct(@QueryMap Map<String, String> params);
}