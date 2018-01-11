package com.tokopedia.transaction.pickuppoint.data.datastore;

import com.tokopedia.core.network.apiservices.transaction.TXCartActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 05/01/18.
 */

public class CartPickupPointDataStore {
    private final TXCartActService service;

    @Inject
    public CartPickupPointDataStore(TXCartActService service) {
        this.service = service;
    }

    public Observable<Response<TkpdResponse>> editPickupPoints(TKPDMapParam<String, String> params) {
        return service.getApi().editPickupPoint(params);
    }

    public Observable<Response<TkpdResponse>> removePickupPoints(TKPDMapParam<String, String> params) {
        return service.getApi().removePickupPoint(params);
    }
}
