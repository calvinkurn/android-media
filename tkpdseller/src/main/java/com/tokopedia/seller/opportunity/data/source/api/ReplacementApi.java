package com.tokopedia.seller.opportunity.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.constant.OpportunityConstant;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by normansyahputa on 1/10/18.
 */

public interface ReplacementApi {
    @GET(OpportunityConstant.PATH_GET_OPPORTUNITY)
    Observable<Response<TkpdResponse>> getOpportunityList(@QueryMap Map<String, String> params);

    @GET(OpportunityConstant.PATH_GET_CATEGORY)
    Observable<Response<TkpdResponse>> getOpportunityCategory(@QueryMap Map<String, String> param);

    @GET(OpportunityConstant.NEW_PRICE_INFO)
    Observable<Response<TkpdResponse>> getOpportunityPriceInfo(@QueryMap Map<String, String> param);
}
