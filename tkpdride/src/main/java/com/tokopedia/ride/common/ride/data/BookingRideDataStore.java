package com.tokopedia.ride.common.ride.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductEntity;
import com.tokopedia.ride.common.ride.data.entity.PromoEntity;
import com.tokopedia.ride.common.ride.data.entity.ReceiptEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;
import com.tokopedia.ride.completetrip.domain.model.Receipt;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface BookingRideDataStore {
    Observable<List<ProductEntity>> getProducts(TKPDMapParam<String, Object> params);

    Observable<List<TimesEstimateEntity>> getEstimatedTimes(TKPDMapParam<String, Object> params);

    Observable<FareEstimateEntity> getEstimatedFare(TKPDMapParam<String, Object> params);

    Observable<RideRequestEntity> createRideRequest(TKPDMapParam<String, Object> params);

    Observable<String> cancelRequest(TKPDMapParam<String, Object> params);

    Observable<RideRequestEntity> getCurrentRequest(TKPDMapParam<String, Object> params);

    Observable<ReceiptEntity> getReceipt(TKPDMapParam<String, Object> param);

    Observable<PromoEntity> getPromo(TKPDMapParam<String, Object> param);
}
