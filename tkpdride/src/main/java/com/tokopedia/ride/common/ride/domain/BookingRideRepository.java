package com.tokopedia.ride.common.ride.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.ride.domain.model.Product;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface BookingRideRepository {
    Observable<List<Product>> getProducts(TKPDMapParam<String, Object> productParams);
}