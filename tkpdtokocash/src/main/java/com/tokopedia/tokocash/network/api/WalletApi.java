package com.tokopedia.tokocash.network.api;

import com.tokopedia.tokocash.network.TkpdTokoCashResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface WalletApi {

    String IDENTIFIER = "identifier";

    @GET(WalletUrl.Wallet.GET_HISTORY)
    Observable<Response<TkpdTokoCashResponse>> getHistoryTokocash(@QueryMap HashMap<String, Object> params);

    @POST(WalletUrl.Wallet.POST_COMPLAINT)
    Observable<Response<TkpdTokoCashResponse>> postHelpHistory(@Body Map<String, String> params);

    @GET(WalletUrl.Wallet.GET_QR_INFO)
    Observable<Response<TkpdTokoCashResponse>> getInfoQrTokoCash(@Path(IDENTIFIER) String identifier);

    @POST(WalletUrl.Wallet.POST_QR_PAYMENT)
    Observable<Response<TkpdTokoCashResponse>> postQrPaymentTokoCash(@Body HashMap<String, Object> params);

    @GET(WalletUrl.Wallet.GET_BALANCE)
    Observable<Response<TkpdTokoCashResponse>> getBalanceTokoCash(@QueryMap HashMap<String, Object> params);
}
