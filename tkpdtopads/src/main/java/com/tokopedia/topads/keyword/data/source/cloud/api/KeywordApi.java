package com.tokopedia.topads.keyword.data.source.cloud.api;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.EditTopAdsKeywordDetailDataModel;
import com.tokopedia.topads.keyword.data.model.TopAdsKeywordEditDetailInputDataModel;
import com.tokopedia.topads.keyword.data.model.cloud.KeywordAddResponseDatum;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.data.model.cloud.request.keywordadd.AddKeywordRequest;

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
 * Created by Test on 5/18/2017.
 */

public interface KeywordApi {

    @GET(TopAdsNetworkConstant.GET_DASHBOARD_KEYWORD)
    Observable<PageDataResponse<List<Datum>>> getDashboardKeyword(@QueryMap Map<String, String> param);

    @Headers({TopAdsNetworkConstant.CONTENT_TYPE_APPLICATION_JSON})
    @POST(TopAdsNetworkConstant.ADD_KEYOWRD)
    Observable<PageDataResponse<List<KeywordAddResponseDatum>>> addKeyword(@Body AddKeywordRequest addKeywordRequest);

    @Headers({TopAdsNetworkConstant.CONTENT_TYPE_APPLICATION_JSON})
    @PATCH(TopAdsNetworkConstant.PATH_EDIT_KEYWORD_DETAIL)
    Observable<Response<DataResponse<List<EditTopAdsKeywordDetailDataModel>>>> editTopAdsKeywordDetail(@Body DataRequest<List<TopAdsKeywordEditDetailInputDataModel>> dataModel);

    @Headers({TopAdsNetworkConstant.CONTENT_TYPE_APPLICATION_JSON})
    @PATCH(TopAdsNetworkConstant.PATH_BULK_KEYWORD_DETAIL)
    Observable<Response<PageDataResponse<DataBulkKeyword>>> actionBulkKeyword(@Body DataRequest<DataBulkKeyword> dataModel);
}