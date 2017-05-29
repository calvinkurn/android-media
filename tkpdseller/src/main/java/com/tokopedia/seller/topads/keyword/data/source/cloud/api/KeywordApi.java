package com.tokopedia.seller.topads.keyword.data.source.cloud.api;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.model.request.DataRequest;
import com.tokopedia.seller.topads.data.model.response.DataResponse;
import com.tokopedia.seller.topads.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.TopAdsKeywordEditDetailInputDataModel;
import com.tokopedia.seller.topads.keyword.data.model.cloud.KeywordAddResponseDatum;
import com.tokopedia.seller.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.seller.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.seller.topads.keyword.data.model.cloud.request.keywordadd.AddKeywordRequest;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Test on 5/18/2017.
 */

public interface KeywordApi {

    @GET("/v1.1/dashboard/keywords")
    Observable<PageDataResponse<List<Datum>>> getDashboardKeyword(@QueryMap Map<String, String> param);

    @FormUrlEncoded
    @POST("/v2/promo/keyword")
    Observable<PageDataResponse<List<KeywordAddResponseDatum>>> addKeyword(@Body AddKeywordRequest addKeywordRequest);

    @PATCH(TopAdsNetworkConstant.PATH_EDIT_KEYWORD_DETAIL)
    Observable<Response<DataResponse<List<EditTopAdsKeywordDetailDomainModel>>>> editTopAdsKeywordDetail(@Body DataRequest<List<TopAdsKeywordEditDetailInputDataModel>> dataModel);
}