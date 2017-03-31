package com.tokopedia.ride.common.ride.data.source.cloud;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.ride.data.BookingRideDataStore;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.PromoEntity;
import com.tokopedia.ride.common.ride.data.entity.ReceiptEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateResponseEntity;
import com.tokopedia.ride.common.ride.data.source.api.RideApi;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
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
    public Observable<List<ProductEntity>> getProducts(TKPDMapParam<String, Object> params) {
        return mRideApi.getProducts(params).map(new Func1<ProductResponseEntity, List<ProductEntity>>() {
            @Override
            public List<ProductEntity> call(ProductResponseEntity productResponseEntity) {
                return productResponseEntity.getProductEntities();
            }
        });
    }

    @Override
    public Observable<List<TimesEstimateEntity>> getEstimatedTimes(TKPDMapParam<String, Object> params) {
        return mRideApi.getEstimateds(params).map(new Func1<TimesEstimateResponseEntity, List<TimesEstimateEntity>>() {
            @Override
            public List<TimesEstimateEntity> call(TimesEstimateResponseEntity timesEstimateResponseEntity) {
                return timesEstimateResponseEntity.getTimes();
            }
        });
    }

    @Override
    public Observable<FareEstimateEntity> getEstimatedFare(TKPDMapParam<String, Object> params) {
        return mRideApi.getFareEstimateds(params);
    }

    @Override
    public Observable<RideRequestEntity> createRideRequest(TKPDMapParam<String, Object> params) {
        return mRideApi.createRequestRide(params).doOnNext(new Action1<RideRequestEntity>() {
            @Override
            public void call(RideRequestEntity entity) {
                RideConfiguration rideConfiguration = new RideConfiguration();
                rideConfiguration.setActiveRequest(entity);
            }
        });
    }

    @Override
    public Observable<String> cancelRequest(TKPDMapParam<String, Object> params) {
        return mRideApi.cancelRequest(params);
    }

    @Override
    public Observable<ReceiptEntity> getReceipt(TKPDMapParam<String, Object> param) {
        return mRideApi.getReceipt(param);
    }

    @Override
    public Observable<RideRequestEntity> getCurrentRequest(TKPDMapParam<String, Object> params) {
        return mRideApi.getDetailRequestRide(params);
    }

    @Override
    public Observable<PromoEntity> getPromo(TKPDMapParam<String, Object> param) {
        return mRideApi.getPromo(param);
    }
}
