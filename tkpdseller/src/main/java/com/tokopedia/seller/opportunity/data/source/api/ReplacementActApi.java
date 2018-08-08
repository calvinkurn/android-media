package com.tokopedia.seller.opportunity.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.opportunity.data.constant.OpportunityConstant;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by normansyahputa on 1/10/18.
 */

public interface ReplacementActApi {
    @FormUrlEncoded
    @POST(OpportunityConstant.PATH_ACCEPT_REPLACEMENT)
    Observable<Response<TkpdResponse>> acceptReplacement(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(OpportunityConstant.PATH_CANCEL_REPLACEMENT)
    Observable<Response<TkpdResponse>> cancelReplacement(@FieldMap Map<String, String> params);
}
