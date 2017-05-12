package com.tokopedia.ride.common.ride.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductEntity;
import com.tokopedia.ride.common.ride.data.entity.PromoEntity;
import com.tokopedia.ride.common.ride.data.entity.ReceiptEntity;
import com.tokopedia.ride.common.ride.data.entity.RideAddressEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestMapEntity;
import com.tokopedia.ride.common.ride.data.entity.RideHistoryEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.ApplyPromo;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.common.ride.domain.model.RideAddress;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.TimesEstimate;
import com.tokopedia.ride.completetrip.domain.model.Receipt;
import com.tokopedia.ride.history.domain.model.RideHistory;

import java.util.List;

import rx.Observable;
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
    private final ReceiptEntityMapper receiptEntityMapper;
    private final PromoEntityMapper promoEntityMapper;
    private final RideHistoryEntityMapper rideHistoryEntityMapper;
    private final RideAddressEntityMapper rideAddressEntityMapper;


    public BookingRideRepositoryData(BookingRideDataStoreFactory bookingRideDataStoreFactory,
                                     ProductEntityMapper productEntityMapper,
                                     TimeEstimateEntityMapper timeEstimateEntityMapper) {
        mBookingRideDataStoreFactory = bookingRideDataStoreFactory;
        mProductEntityMapper = productEntityMapper;
        mTimeEstimateEntityMapper = timeEstimateEntityMapper;
        estimateMapper = new FareEstimateMapper();
        rideRequestEntityMapper = new RideRequestEntityMapper();
        receiptEntityMapper = new ReceiptEntityMapper();
        promoEntityMapper = new PromoEntityMapper();
        rideHistoryEntityMapper = new RideHistoryEntityMapper();
        rideAddressEntityMapper = new RideAddressEntityMapper();
    }

    @Override
    public Observable<Product> getProduct(TKPDMapParam<String, Object> params) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getProduct(params)
                .map(new Func1<ProductEntity, Product>() {
                    @Override
                    public Product call(ProductEntity productEntity) {
                        return mProductEntityMapper.transform(productEntity);
                    }
                });
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
    public Observable<RideRequest> createRequest(TKPDMapParam<String, Object> params) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .createRideRequest(params)
                .map(new Func1<RideRequestEntity, RideRequest>() {
                    @Override
                    public RideRequest call(RideRequestEntity rideRequestEntity) {
                        return rideRequestEntityMapper.transform(rideRequestEntity);
                    }
                });
    }

    @Override
    public Observable<String> cancelRequest(TKPDMapParam<String, Object> params) {
        return mBookingRideDataStoreFactory.createCloudDataStore().cancelRequest(params);
    }

    @Override
    public Observable<RideRequest> getRequestDetail(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory
                .createCloudDataStore()
                .getRequestDetail(parameters)
                .map(new Func1<RideRequestEntity, RideRequest>() {
                    @Override
                    public RideRequest call(RideRequestEntity entity) {
                        return rideRequestEntityMapper.transform(entity);
                    }
                });
    }

    @Override
    public Observable<RideRequest> getCurrentRequest(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory
                .createCloudDataStore()
                .getCurrentRequest(parameters)
                .map(new Func1<RideRequestEntity, RideRequest>() {
                    @Override
                    public RideRequest call(RideRequestEntity rideRequestEntity) {
                        return rideRequestEntityMapper.transform(rideRequestEntity);
                    }
                });
    }

    @Override
    public Observable<Receipt> getReceipt(TKPDMapParam<String, Object> param) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getReceipt(param)
                .map(new Func1<ReceiptEntity, Receipt>() {
                    @Override
                    public Receipt call(ReceiptEntity entity) {
                        return receiptEntityMapper.transform(entity);
                    }
                });
    }

    @Override
    public Observable<List<Promo>> getPromo(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getPromo(parameters)
                .map(new Func1<List<PromoEntity>, List<Promo>>() {
                    @Override
                    public List<Promo> call(List<PromoEntity> promoEntities) {
                        return promoEntityMapper.transform(promoEntities);
                    }
                });
    }

    @Override
    public Observable<String> getRideMap(String requestId, TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getRideMap(requestId, parameters)
                .map(new Func1<RideRequestMapEntity, String>() {
                    @Override
                    public String call(RideRequestMapEntity rideRequestMapEntity) {
                        return rideRequestMapEntity.getHref();
                    }
                });
    }

    @Override
    public Observable<List<RideHistory>> getHistories(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getHistories(parameters)
                .map(new Func1<List<RideHistoryEntity>, List<RideHistory>>() {
                    @Override
                    public List<RideHistory> call(List<RideHistoryEntity> rideTransactionEntities) {
                        return rideHistoryEntityMapper.transform(rideTransactionEntities);
                    }
                });
    }

    @Override
    public Observable<RideHistory> getHistory(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getHistory(parameters)
                .map(new Func1<RideHistoryEntity, RideHistory>() {
                    @Override
                    public RideHistory call(RideHistoryEntity rideHistoryEntity) {
                        return rideHistoryEntityMapper.transform(rideHistoryEntity);
                    }
                });
    }

    @Override
    public Observable<ApplyPromo> applyPromo(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .applyPromo(parameters)
                .map(new Func1<PromoEntity, ApplyPromo>() {
                    @Override
                    public ApplyPromo call(PromoEntity promoEntity) {
                        return new ApplyPromo();
                    }
                });
    }

    @Override
    public Observable<List<RideAddress>> getAddresses(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getAddresses(parameters)
                .map(new Func1<List<RideAddressEntity>, List<RideAddress>>() {
                    @Override
                    public List<RideAddress> call(List<RideAddressEntity> rideAddressEntities) {
                        return rideAddressEntityMapper.transform(rideAddressEntities);
                    }
                });
    }
}
