package com.tokopedia.gm.cashback.data.source;

import com.tokopedia.gm.cashback.data.model.RequestCashbackModel;
import com.tokopedia.seller.common.data.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public class GMCashbackDataSourceCloud {

    private final CashbackApi cashbackApi;

    @Inject
    public GMCashbackDataSourceCloud(CashbackApi cashbackApi) {
        this.cashbackApi = cashbackApi;
    }

    public Observable<Response<DataResponse<String>>> setCashback(RequestCashbackModel cashback) {
        return cashbackApi.setCashback(cashback);
    }
}
