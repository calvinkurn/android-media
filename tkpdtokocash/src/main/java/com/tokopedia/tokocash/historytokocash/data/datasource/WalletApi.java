package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface WalletApi {

    @GET(TkpdBaseURL.Wallet.GET_HISTORY)
    Observable<Response<TkpdDigitalResponse>> getHistoryTokocash(@QueryMap TKPDMapParam<String, Object> params);

    @POST(TkpdBaseURL.Wallet.POST_COMPLAINT)
    Observable<Response<TkpdDigitalResponse>> postHelpHistory(@Body Map<String, String> params);

    @POST
    Observable<Response<TkpdDigitalResponse>> withdrawSaldoFromTokocash(@Url String url,
                                                                        @Body Map<String, String> params);

    @GET(TkpdBaseURL.Wallet.GET_OAUTH_INFO_ACCOUNT)
    Observable<Response<TkpdDigitalResponse>> getOAuthInfoAccount();

    @FormUrlEncoded
    @POST(TkpdBaseURL.Wallet.REVOKE_ACCESS_TOKOCASH)
    Observable<Response<String>> revokeAccessAccountTokoCash(@FieldMap Map<String, String> params);
}
