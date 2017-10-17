package com.tokopedia.gm.cashback.data.source;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.gm.cashback.data.model.RequestCashbackModel;
import com.tokopedia.gm.cashback.data.model.RequestGetCashbackModel;
import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.seller.common.data.response.DataResponse;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public interface CashbackApi {

    @POST(TkpdBaseURL.GoldMerchant.SET_CASHBACK_PRODUCTS)
    Observable<Response<DataResponse<String>>> setCashback(@Body RequestCashbackModel cashback);

    @POST(TkpdBaseURL.GoldMerchant.GET_CASHBACK_PRODUCTS)
    Observable<Response<DataResponse<List<DataCashbackModel>>>> getCashbackList(@Body RequestGetCashbackModel requestGetCashbackModel);
}
