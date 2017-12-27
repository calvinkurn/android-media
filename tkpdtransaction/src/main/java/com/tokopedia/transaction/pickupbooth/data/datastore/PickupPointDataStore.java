package com.tokopedia.transaction.pickupbooth.data.datastore;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.pickupbooth.data.entity.PickupPointResponseEntity;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointDataStore {
    private final KeroAuthService service;

    @Inject
    public PickupPointDataStore(KeroAuthService service) {
        this.service = service;
    }

    public Observable<PickupPointResponseEntity> getPickupPoints(TKPDMapParam<String, String> params) {
        return service.getApi().getPickupStores(params)
                .map(new Func1<Response<String>, PickupPointResponseEntity>() {
                    @Override
                    public PickupPointResponseEntity call(Response<String> stringResponse) {
                        return new Gson().fromJson(stringResponse.body(), PickupPointResponseEntity.class);
                    }
                });
    }

}
