package com.tokopedia.ride.common.ride.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.TimesEstimate;
import com.tokopedia.ride.completetrip.domain.model.Receipt;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface BookingRideRepository {
    Observable<List<Product>> getProducts(TKPDMapParam<String, Object> productParams);

    Observable<List<TimesEstimate>> getEstimatedTimes(TKPDMapParam<String, Object> productParams);

    Observable<FareEstimate> getEstimatedFare(TKPDMapParam<String, Object> productParams);

    Observable<RideRequest> createRideRequest(TKPDMapParam<String, Object> productParams);

    Observable<String> cancelRequest(TKPDMapParam<String, Object> productParams);

    Observable<RideRequest> getCurrentRequest(TKPDMapParam<String, Object> parameters);

    Observable<Receipt> getReceipt(TKPDMapParam<String, Object> param);

    Observable<Promo> getPromo(TKPDMapParam<String, Object> parameters);
}