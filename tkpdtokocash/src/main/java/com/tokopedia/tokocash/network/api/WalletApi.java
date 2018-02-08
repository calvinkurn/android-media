package com.tokopedia.tokocash.network.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.tokocash.activation.data.entity.PendingCashbackEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.TokoCashHistoryEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.InfoQrEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.QrPaymentEntity;

import java.util.HashMap;

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

    @GET(TkpdBaseURL.TokoCash.PATH_CASH_BACK_DOMAIN)
    Observable<Response<DataResponse<PendingCashbackEntity>>> getTokoCashPending(@QueryMap HashMap<String, String> params);

    @GET(WalletUrl.Wallet.GET_HISTORY)
    Observable<Response<DataResponse<TokoCashHistoryEntity>>> getHistoryTokocash(@QueryMap HashMap<String, Object> params);

    @GET(WalletUrl.Wallet.GET_QR_INFO)
    Observable<Response<DataResponse<InfoQrEntity>>> getInfoQrTokoCash(@Path(IDENTIFIER) String identifier);

    @POST(WalletUrl.Wallet.POST_QR_PAYMENT)
    Observable<Response<DataResponse<QrPaymentEntity>>> postQrPaymentTokoCash(@Body HashMap<String, Object> params);

    @GET(WalletUrl.Wallet.GET_BALANCE)
    Observable<Response<DataResponse<BalanceTokoCashEntity>>> getBalanceTokoCash();
}
