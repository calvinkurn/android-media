package com.tokopedia.transaction.pickupbooth.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.pickupbooth.data.datastore.PickupPointDataStore;
import com.tokopedia.transaction.pickupbooth.data.entity.PickupPointResponseEntity;
import com.tokopedia.transaction.pickupbooth.domain.mapper.PickupPointEntityMapper;
import com.tokopedia.transaction.pickupbooth.domain.model.PickupPointResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointRepository {
    private final PickupPointDataStore dataStore;
    private final PickupPointEntityMapper entityMapper;

    @Inject
    public PickupPointRepository(PickupPointDataStore dataStore, PickupPointEntityMapper entityMapper) {
        this.dataStore = dataStore;
        this.entityMapper = entityMapper;
    }

    public Observable<PickupPointResponse> getPickupPoints(TKPDMapParam<String, String> parameters) {
        return dataStore.getPickupPoints(parameters).map(new Func1<PickupPointResponseEntity, PickupPointResponse>() {
            @Override
            public PickupPointResponse call(PickupPointResponseEntity addressResponseEntity) {
                return entityMapper.transform(addressResponseEntity);
            }
        });
    }
}
