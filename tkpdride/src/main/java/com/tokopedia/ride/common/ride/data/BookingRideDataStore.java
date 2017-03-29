package com.tokopedia.ride.common.ride.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface BookingRideDataStore {
    Observable<List<ProductEntity>> getProducts(TKPDMapParam<String, Object> productParams);

    Observable<List<TimesEstimateEntity>> getEstimatedTimes(TKPDMapParam<String, Object> productParams);

    Observable<FareEstimateEntity> getEstimatedFare(TKPDMapParam<String, Object> productParams);

    Observable<RideRequestEntity> createRideRequest(TKPDMapParam<String, Object> productParams);

    Observable<String> cancelRequest(TKPDMapParam<String, Object> productParams);

    Observable<String> getCurrentRequest(TKPDMapParam<String, Object> productParams);
}
