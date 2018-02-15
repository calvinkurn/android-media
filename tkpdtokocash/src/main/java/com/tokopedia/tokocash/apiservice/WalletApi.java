package com.tokopedia.tokocash.apiservice;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface WalletApi {

    String IDENTIFIER = "identifier";

    @GET(TkpdBaseURL.Wallet.GET_HISTORY)
    Observable<Response<TkpdDigitalResponse>> getHistoryTokocash(@QueryMap HashMap<String, Object> params);

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

    @GET(TkpdBaseURL.Wallet.GET_QR_INFO)
    Observable<Response<TkpdDigitalResponse>> getInfoQrTokoCash(@Path(IDENTIFIER) String identifier);

    @POST(TkpdBaseURL.Wallet.POST_QR_PAYMENT)
    Observable<Response<TkpdDigitalResponse>> postQrPaymentTokoCash(@Body HashMap<String, Object> params);

    @GET(TkpdBaseURL.Wallet.GET_BALANCE)
    Observable<Response<DataResponse<BalanceTokoCashEntity>>> getBalanceTokoCash();
}
