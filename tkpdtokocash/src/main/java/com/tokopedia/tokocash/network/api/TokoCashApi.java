package com.tokopedia.tokocash.network.api;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kris on 1/5/17. Tokopedia
 */

public interface TokoCashApi {

    @GET(WalletUrl.Account.PATH_WALLET)
    Observable<Response<TkpdResponse>> getTokoCash();

    @GET(WalletUrl.Account.PATH_CASH_BACK_DOMAIN)
    Observable<Response<TkpdDigitalResponse>> getTokoCashPending(@QueryMap Map<String, String> params);

    @GET(WalletUrl.Account.PATH_REQUEST_OTP_WALLET)
    Observable<Response<TkpdResponse>> requestOtpWallet();

    @GET(WalletUrl.Account.PATH_LINK_WALLET_TO_TOKOCASH)
    Observable<Response<TkpdResponse>> linkedWalletToTokocash(@Query("otp") String otp);

    @GET(WalletUrl.Account.GET_TOKEN_WALLET)
    Observable<Response<TkpdResponse>> getTokenWallet();

    @GET(WalletUrl.Account.GET_TOKEN_WALLET)
    Call<String> getTokenWalletSynchronous();
}
