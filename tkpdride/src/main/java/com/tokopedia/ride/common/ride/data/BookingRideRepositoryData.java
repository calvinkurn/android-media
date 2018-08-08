package com.tokopedia.ride.common.ride.data;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.bookingride.data.NearbyRidesDestinationMapper;
import com.tokopedia.ride.bookingride.data.RideAddressCache;
import com.tokopedia.ride.bookingride.data.RideAddressCacheImpl;
import com.tokopedia.ride.bookingride.domain.model.NearbyRides;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.ride.data.entity.CancelReasonsResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.GetPendingEntity;
import com.tokopedia.ride.common.ride.data.entity.PaymentMethodListEntity;
import com.tokopedia.ride.common.ride.data.entity.PriceEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductEntity;
import com.tokopedia.ride.common.ride.data.entity.PromoEntity;
import com.tokopedia.ride.common.ride.data.entity.ReceiptEntity;
import com.tokopedia.ride.common.ride.data.entity.RideAddressEntity;
import com.tokopedia.ride.common.ride.data.entity.RideHistoryEntity;
import com.tokopedia.ride.common.ride.data.entity.RideHistoryResponse;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestMapEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.UpdateDestinationEntity;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.GetPending;
import com.tokopedia.ride.common.ride.domain.model.PayPending;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;
import com.tokopedia.ride.common.ride.domain.model.PriceEstimate;
import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.common.ride.domain.model.Receipt;
import com.tokopedia.ride.common.ride.domain.model.RideAddress;
import com.tokopedia.ride.common.ride.domain.model.RideHistoryWrapper;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.TimePriceEstimate;
import com.tokopedia.ride.common.ride.domain.model.TimesEstimate;
import com.tokopedia.ride.common.ride.domain.model.UpdateDestination;
import com.tokopedia.ride.history.domain.model.RideHistory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

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
    private final TimePriceEstimateEntityMapper timePriceEstimateEntityMapper;
    private final PriceEstimateEntityMapper priceEstimateEntityMapper;
    private final RideHistoryWrapperMapper rideHistoryWrapperMapper;
    private final UpdateDestinationEntityMapper updateDestinationEntityMapper;
    private final PaymentMethodListMapper paymentMethodListMapper;
    private final NearbyRidesDestinationMapper nearbyRidesDestinationMapper;
    private final PayPendingEntityMapper payPendingEntityMapper;
    private final GetPendingEntityMapper getPendingEntityMapper;

    public BookingRideRepositoryData(BookingRideDataStoreFactory bookingRideDataStoreFactory) {
        mBookingRideDataStoreFactory = bookingRideDataStoreFactory;
        mProductEntityMapper = new ProductEntityMapper();
        mTimeEstimateEntityMapper = new TimeEstimateEntityMapper();
        estimateMapper = new FareEstimateMapper();
        rideRequestEntityMapper = new RideRequestEntityMapper();
        receiptEntityMapper = new ReceiptEntityMapper();
        promoEntityMapper = new PromoEntityMapper();
        rideHistoryEntityMapper = new RideHistoryEntityMapper();
        rideAddressEntityMapper = new RideAddressEntityMapper();
        timePriceEstimateEntityMapper = new TimePriceEstimateEntityMapper();
        priceEstimateEntityMapper = new PriceEstimateEntityMapper();
        rideHistoryWrapperMapper = new RideHistoryWrapperMapper();
        updateDestinationEntityMapper = new UpdateDestinationEntityMapper();
        paymentMethodListMapper = new PaymentMethodListMapper();
        nearbyRidesDestinationMapper = new NearbyRidesDestinationMapper();
        payPendingEntityMapper = new PayPendingEntityMapper();
        getPendingEntityMapper = new GetPendingEntityMapper();
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
                }).doOnNext(new Action1<RideRequest>() {
                    @Override
                    public void call(RideRequest rideRequest) {
//                        RideConfiguration configuration = new RideConfiguration();
//                        configuration.setActiveRequest(rideRequest);
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
                }).doOnNext(new Action1<RideRequest>() {
                    @Override
                    public void call(RideRequest rideRequest) {
//                        RideConfiguration configuration = new RideConfiguration();
//                        configuration.setActiveRequest(rideRequest);
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
    public Observable<String> getRideMap(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getRideMap(parameters)
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
    public Observable<RideHistoryWrapper> getHistoriesWithPagination(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getHistoriesWithPagination(parameters)
                .map(new Func1<RideHistoryResponse, RideHistoryWrapper>() {
                    @Override
                    public RideHistoryWrapper call(RideHistoryResponse rideHistoryResponse) {
                        return rideHistoryWrapperMapper.transform(rideHistoryResponse);
                    }
                });
    }

    @Override
    public Observable<RideHistory> getHistory(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getHistory(parameters)
                .map(new Func1<List<RideHistoryEntity>, RideHistory>() {
                    @Override
                    public RideHistory call(List<RideHistoryEntity> rideHistoryEntities) {
                        if (rideHistoryEntities.size() > 0) {
                            return rideHistoryEntityMapper.transform(rideHistoryEntities.get(0));
                        }
                        return null;
                    }
                });
    }

    @Override
    public Observable<List<RideAddress>> getAddresses(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getAddresses(parameters)
                .doOnNext(new Action1<List<RideAddressEntity>>() {
                    @Override
                    public void call(List<RideAddressEntity> entities) {
                        RideAddressCache rideAddressCache = new RideAddressCacheImpl();
                        rideAddressCache.put(entities);
                    }
                })
                .map(new Func1<List<RideAddressEntity>, List<RideAddress>>() {
                    @Override
                    public List<RideAddress> call(List<RideAddressEntity> rideAddressEntities) {
                        return rideAddressEntityMapper.transform(rideAddressEntities);
                    }
                });
    }

    @Override
    public Observable<List<RideAddress>> getAddressesFromCache() {
        return mBookingRideDataStoreFactory.createDiskDataStore()
                .getAddresses(new TKPDMapParam<String, Object>())
                .map(new Func1<List<RideAddressEntity>, List<RideAddress>>() {
                    @Override
                    public List<RideAddress> call(List<RideAddressEntity> entities) {
                        return rideAddressEntityMapper.transform(entities);
                    }
                });
    }

    @Override
    public Observable<String> sendRating(String requestId, TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .sendRating(requestId, parameters);
    }

    @Override
    public Observable<List<String>> getCancelReasons(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getCancelReasons(parameters)
                .map(new Func1<CancelReasonsResponseEntity, List<String>>() {
                    @Override
                    public List<String> call(CancelReasonsResponseEntity cancelReasonsResponseEntity) {
                        return cancelReasonsResponseEntity.getReasons();
                    }
                });
    }

    @Override
    public Observable<List<TimePriceEstimate>> getTimePriceEstimate(TKPDMapParam<String, Object> parameters) {
        return Observable.zip(
                mBookingRideDataStoreFactory.createCloudDataStore()
                        .getEstimatedTimes(parameters),
                mBookingRideDataStoreFactory.createCloudDataStore()
                        .getPrices(parameters),
                new Func2<List<TimesEstimateEntity>, List<PriceEntity>, List<TimePriceEstimate>>() {
                    @Override
                    public List<TimePriceEstimate> call(List<TimesEstimateEntity> timesEstimateEntities, List<PriceEntity> priceEntities) {
                        List<TimePriceEstimate> timePriceEstimates = new ArrayList<>();
                        timePriceEstimates = timePriceEstimateEntityMapper.transform(timesEstimateEntities, priceEntities);/*
                        for (TimesEstimateEntity estimateEntity : timesEstimateEntities){
                            for (PriceEntity priceEntity : priceEntities){
                                if (estimateEntity.getProductId().equalsIgnoreCase(priceEntity.getProductId())){
                                    timePriceEstimates.add(timePriceEstimateEntityMapper.transform(priceEntity, estimateEntity));
                                }
                            }
                        }*/
                        return timePriceEstimates;
                    }
                }
        );
    }

    @Override
    public Observable<List<PriceEstimate>> getPriceEstimate(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getPrices(parameters)
                .map(new Func1<List<PriceEntity>, List<PriceEstimate>>() {
                    @Override
                    public List<PriceEstimate> call(List<PriceEntity> priceEntities) {
                        return priceEstimateEntityMapper.transform(priceEntities);
                    }
                });
    }

    @Override
    public Observable<UpdateDestination> updateRequest(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .updateRequest(parameters)
                .map(new Func1<UpdateDestinationEntity, UpdateDestination>() {
                    @Override
                    public UpdateDestination call(UpdateDestinationEntity updateDestinationEntity) {
                        return updateDestinationEntityMapper.transform(updateDestinationEntity);
                    }
                });
    }

    @Override
    public Observable<String> sendTip(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore().sendTip(parameters);
    }

    @Override
    public Observable<PaymentMethodList> getPaymentMethodList(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getPaymentMethodList(parameters)
                .doOnNext(new Action1<PaymentMethodListEntity>() {
                    @Override
                    public void call(PaymentMethodListEntity paymentMethodListEntity) {
                        PaymentMethodListCache cache = new PaymentMethodListCacheImpl();
                        cache.put(paymentMethodListEntity);
                    }
                })
                .map(new Func1<PaymentMethodListEntity, PaymentMethodList>() {
                    @Override
                    public PaymentMethodList call(PaymentMethodListEntity paymentMethodListEntity) {
                        return paymentMethodListMapper.transform(paymentMethodListEntity);
                    }
                });
    }

    public Observable<NearbyRides> getNearbyCars(TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getNearbyCars(parameters)
                .map(nearbyRidesDestinationMapper);
    }

    @Override
    public Observable<String> requestApi(String url, TKPDMapParam<String, Object> parameters) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .requestApi(url, parameters);
    }

    @Override
    public Observable<PaymentMethodList> getPaymentMethodListFromCache() {
        return mBookingRideDataStoreFactory.createDiskDataStore()
                .getPaymentMethodList(new TKPDMapParam<String, Object>())
                .map(new Func1<PaymentMethodListEntity, PaymentMethodList>() {
                    @Override
                    public PaymentMethodList call(PaymentMethodListEntity paymentMethodListEntity) {
                        return paymentMethodListMapper.transform(paymentMethodListEntity);
                    }
                });
    }

    @Override
    public Observable<PayPending> payPendingAmount() {
        return mBookingRideDataStoreFactory.createCloudDataStore().payPendingAmount()
                .map(new Func1<JsonObject, PayPending>() {
                    @Override
                    public PayPending call(JsonObject payPendingEntity) {
                        return payPendingEntityMapper.transform(payPendingEntity);
                    }
                });
    }

    @Override
    public Observable<GetPending> getPendingAmount() {
        return mBookingRideDataStoreFactory.createCloudDataStore().getPendingAmount().map(new Func1<GetPendingEntity, GetPending>() {
            @Override
            public GetPending call(GetPendingEntity getPendingEntity) {
                return getPendingEntityMapper.transform(getPendingEntity);
            }
        });
    }
}
