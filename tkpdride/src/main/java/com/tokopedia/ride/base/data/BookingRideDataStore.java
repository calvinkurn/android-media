package com.tokopedia.ride.base.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.base.data.entity.ProductEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface BookingRideDataStore {
    Observable<List<ProductEntity>> getProducts(TKPDMapParam<String, Object> productParams);
}
