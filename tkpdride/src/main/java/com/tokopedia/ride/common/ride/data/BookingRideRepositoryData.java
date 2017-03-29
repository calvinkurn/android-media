package com.tokopedia.ride.common.ride.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.TimesEstimate;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by alvarisi on 3/14/17.
 */

public class BookingRideRepositoryData implements BookingRideRepository {
    private final BookingRideDataStoreFactory mBookingRideDataStoreFactory;
    private final ProductEntityMapper mProductEntityMapper;
    private final TimeEstimateEntityMapper mTimeEstimateEntityMapper;
    private final FareEstimateMapper estimateMapper;
    private final RideRequestEntityMapper rideRequestEntityMapper;

    public BookingRideRepositoryData(BookingRideDataStoreFactory bookingRideDataStoreFactory,
                                     ProductEntityMapper productEntityMapper,
                                     TimeEstimateEntityMapper timeEstimateEntityMapper) {
        mBookingRideDataStoreFactory = bookingRideDataStoreFactory;
        mProductEntityMapper = productEntityMapper;
        mTimeEstimateEntityMapper = timeEstimateEntityMapper;
        estimateMapper = new FareEstimateMapper();
        rideRequestEntityMapper = new RideRequestEntityMapper();
    }

    @Override
    public Observable<List<Product>> getProducts(TKPDMapParam<String, Object> params) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getProducts(params).map(new Func1<List<ProductEntity>, List<Product>>() {
                    @Override
                    public List<Product> call(List<ProductEntity> entities) {
                        return mProductEntityMapper.transform(entities);
                    }
                });
    }

    @Override
    public Observable<List<TimesEstimate>> getEstimatedTimes(TKPDMapParam<String, Object> params) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getEstimatedTimes(params).map(new Func1<List<TimesEstimateEntity>, List<TimesEstimate>>() {
                    @Override
                    public List<TimesEstimate> call(List<TimesEstimateEntity> timesEstimateEntities) {
                        return mTimeEstimateEntityMapper.transform(timesEstimateEntities);
                    }
                });
    }

    @Override
    public Observable<FareEstimate> getEstimatedFare(TKPDMapParam<String, Object> params) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getEstimatedFare(params)
                .map(new Func1<FareEstimateEntity, FareEstimate>() {
                    @Override
                    public FareEstimate call(FareEstimateEntity fareEstimateEntity) {
                        return estimateMapper.transform(fareEstimateEntity);
                    }
                });
    }

    @Override
    public Observable<RideRequest> createRideRequest(TKPDMapParam<String, Object> params) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .createRideRequest(params)
                .map(new Func1<RideRequestEntity, RideRequest>() {
                    @Override
                    public RideRequest call(RideRequestEntity rideRequestEntity) {
                        return rideRequestEntityMapper.transform(rideRequestEntity);
                    }
                }).doOnNext(new Action1<RideRequest>() {
                    @Override
                    public void call(RideRequest rideRequest) {

                    }
                });
    }

    @Override
    public Observable<String> cancelRequest(TKPDMapParam<String, Object> params) {
        return mBookingRideDataStoreFactory.createCloudDataStore().cancelRequest(params);
    }

    @Override
    public Observable<String> getCurrentRequest(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore().getCurrentRequest(parameters);
    }
}
