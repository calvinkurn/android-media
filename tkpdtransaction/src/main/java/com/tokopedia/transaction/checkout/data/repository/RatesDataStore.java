package com.tokopedia.transaction.checkout.data.repository;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.transaction.checkout.data.entity.response.rates.RatesResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class RatesDataStore {
    private final KeroAuthService service;

    @Inject
    public RatesDataStore(KeroAuthService service) {
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
