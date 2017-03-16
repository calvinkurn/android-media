package com.tokopedia.ride.base.data.source.cloud;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.base.data.BookingRideDataStore;
import com.tokopedia.ride.base.data.entity.ProductEntity;
import com.tokopedia.ride.base.data.entity.ProductResponseEntity;
import com.tokopedia.ride.base.data.source.api.RideApi;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 3/14/17.
 */

public class CloudBookingRideDataStore implements BookingRideDataStore {
    private final RideApi mRideApi;
    public CloudBookingRideDataStore(RideApi rideApi) {
        mRideApi = rideApi;
    }

    @Override
    public Observable<List<ProductEntity>> getProducts(TKPDMapParam<String, Object> productParams) {
        return mRideApi.getProducts(productParams).map(new Func1<ProductResponseEntity, List<ProductEntity>>() {
            @Override
            public List<ProductEntity> call(ProductResponseEntity productResponseEntity) {
                return productResponseEntity.getProductEntities();
            }
        });
    }
}
