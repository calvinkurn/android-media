package com.tokopedia.transaction.checkout.data.repository;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.data.entity.response.ratesV2.RatesResponse;
import com.tokopedia.transaction.checkout.data.service.RatesService;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 22/03/18.
 */

public class RatesDataStoreV2 {
    private final RatesService service;

    @Inject
    public RatesDataStoreV2(RatesService service) {
        this.service = service;
    }

    public Observable<RatesResponse> getRates(TKPDMapParam<String, String> params) {
        return service.getApi().calculateShippingRate(params)
                .map(new Func1<Response<String>, RatesResponse>() {
                    @Override
                    public RatesResponse call(Response<String> stringResponse) {
                        return new Gson().fromJson(stringResponse.body(), RatesResponse.class);
                    }
                });
    }
}
