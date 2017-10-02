package com.tokopedia.gm.cashback.data.source;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.gm.cashback.data.model.RequestCashbackModel;
import com.tokopedia.seller.common.data.response.DataResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public interface CashbackApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.GoldMerchant.SET_CASHBACK_PRODUCTS)
    Observable<Response<DataResponse<String>>> setCashback(@Body RequestCashbackModel cashback);
}
