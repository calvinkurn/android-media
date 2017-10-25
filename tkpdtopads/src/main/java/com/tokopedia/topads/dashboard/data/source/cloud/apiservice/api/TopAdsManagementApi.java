package com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.TopAdsProductDetailDataSourceModel;
import com.tokopedia.topads.dashboard.data.model.request.CreateGroupRequest;
import com.tokopedia.topads.dashboard.data.model.request.EditGroupRequest;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.DataResponseCreateGroup;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.data.DataCredit;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.data.model.data.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.Product;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
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

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_SHOP)
    Observable<Response<DataResponse<ShopAd>>> getShopAd(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_CREDIT)
    Observable<Response<DataResponse<List<DataCredit>>>> getDashboardCredit();

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_PRODUCT)
    Observable<Response<PageDataResponse<List<ProductAd>>>> getProductAd(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_GROUP)
    Observable<Response<PageDataResponse<List<GroupAd>>>> getGroupAd(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_SEARCH_PRODUCT)
    Observable<Response<DataResponse<List<Product>>>> searchProductAd(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_SEARCH_GROUP)
    Observable<Response<DataResponse<List<GroupAd>>>> searchGroupAd(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DETAIL_PRODUCT_AD)
    Observable<Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>>> getDetailProduct(@QueryMap Map<String, String> params);

    @Headers({"Content-Type: application/json"})
    @PATCH(TopAdsNetworkConstant.PATH_BULK_ACTION_PRODUCT_AD)
    Observable<Response<DataResponse<ProductAdBulkAction>>> bulkActionProductAd(@Body DataRequest<ProductAdBulkAction> body);

    @Headers({"Content-Type: application/json"})
    @PATCH(TopAdsNetworkConstant.PATH_BULK_ACTION_GROUP_AD)
    Observable<Response<DataResponse<GroupAdBulkAction>>> bulkActionGroupAd(@Body DataRequest<GroupAdBulkAction> body);

    @Headers({"Content-Type: application/json"})
    @PATCH(TopAdsNetworkConstant.PATH_DETAIL_PRODUCT_AD)
    Observable<Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>>> editProductAd(@Body DataRequest<List<TopAdsProductDetailDataSourceModel>> body);

    @POST(TopAdsNetworkConstant.PATH_DETAIL_PRODUCT_AD)
    Observable<Response<DataResponse<List<TopAdsProductDetailDataSourceModel>>>> createProductAd(@Body DataRequest<List<TopAdsProductDetailDataSourceModel>> body);

    @Headers({"Content-Type: application/json"})
    @POST(TopAdsNetworkConstant.PATH_CREATE_GROUP_AD)
    Observable<Response<DataResponse<DataResponseCreateGroup>>> createGroupAd(@Body DataRequest<CreateGroupRequest> body);

    @GET(TopAdsNetworkConstant.PATH_CREATE_GROUP_AD)
    Observable<Response<DataResponse<DataResponseCreateGroup>>> getDetailGroup(@QueryMap Map<String, String> params);

    @Headers({"Content-Type: application/json"})
    @PATCH(TopAdsNetworkConstant.PATH_CREATE_GROUP_AD)
    Observable<Response<DataResponse<DataResponseCreateGroup>>> editGroupAd(@Body DataRequest<EditGroupRequest> body);

    @Headers({"Content-Type: application/json"})
    @POST(TopAdsNetworkConstant.GET_SUGGESTION)
    Observable<Response<String>> getSuggestion(@Body DataRequest<GetSuggestionBody> body);

}