package com.tokopedia.transaction.pickuppoint.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.pickuppoint.data.datastore.CartPickupPointDataStore;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 05/01/18.
 */

public class CartPickupPointRepository {
    private final CartPickupPointDataStore dataStore;

    @Inject
    public CartPickupPointRepository(CartPickupPointDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<Response<String>> editPickupPoints(TKPDMapParam<String, String> parameters) {
        return dataStore.editPickupPoints(parameters);
    }

    public Observable<Response<String>> removePickupPoints(TKPDMapParam<String, String> parameters) {
        return dataStore.editPickupPoints(parameters);
    }
}
