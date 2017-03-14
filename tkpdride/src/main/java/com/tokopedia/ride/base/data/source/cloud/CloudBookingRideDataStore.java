package com.tokopedia.ride.base.data.source.cloud;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.base.data.BookingRideDataStore;
import com.tokopedia.ride.base.data.entity.ProductEntity;
import com.tokopedia.ride.base.data.source.api.UberApi;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public class CloudBookingRideDataStore implements BookingRideDataStore {
    private final UberApi mUberApi;
    public CloudBookingRideDataStore(UberApi uberApi) {
        mUberApi = uberApi;
    }

    @Override
    public Observable<List<ProductEntity>> getProducts(TKPDMapParam<String, Object> productParams) {
        return mUberApi.getProducts(productParams);
    }
}
