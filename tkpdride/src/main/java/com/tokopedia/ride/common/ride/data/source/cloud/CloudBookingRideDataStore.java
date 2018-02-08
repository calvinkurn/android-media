package com.tokopedia.ride.common.ride.data.source.cloud;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.bookingride.data.entity.NearbyRidesEntity;
import com.tokopedia.ride.common.ride.data.BookingRideDataStore;
import com.tokopedia.ride.common.ride.data.entity.CancelReasonsResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.GetPendingEntity;
import com.tokopedia.ride.common.ride.data.entity.PaymentMethodListEntity;
import com.tokopedia.ride.common.ride.data.entity.PriceEntity;
import com.tokopedia.ride.common.ride.data.entity.PriceResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.PromoEntity;
import com.tokopedia.ride.common.ride.data.entity.ReceiptEntity;
import com.tokopedia.ride.common.ride.data.entity.RideAddressEntity;
import com.tokopedia.ride.common.ride.data.entity.RideHistoryEntity;
import com.tokopedia.ride.common.ride.data.entity.RideHistoryResponse;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestMapEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.UpdateDestinationEntity;
import com.tokopedia.ride.common.ride.data.source.api.RideApi;

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
    public Observable<ProductEntity> getProduct(TKPDMapParam<String, Object> params) {
        return mRideApi.getProduct(params);
    }

    @Override
    public Observable<List<PriceEntity>> getPrices(TKPDMapParam<String, Object> params) {
        return mRideApi.getPrices(params)
                .map(new Func1<PriceResponseEntity, List<PriceEntity>>() {
                    @Override
                    public List<PriceEntity> call(PriceResponseEntity priceResponseEntity) {
                        return priceResponseEntity.getPrices();
                    }
                });
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
        return mRideApi.createRequestRide(params);
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
    public Observable<RideRequestEntity> getRequestDetail(TKPDMapParam<String, Object> params) {
        return mRideApi.getDetailRequestRide(params);
    }

    @Override
    public Observable<List<PromoEntity>> getPromo(TKPDMapParam<String, Object> param) {
        return mRideApi.getPromo(param);
    }

    @Override
    public Observable<RideRequestMapEntity> getRideMap(TKPDMapParam<String, Object> parameters) {
        return mRideApi.getRideMap(parameters);
    }

    @Override
    public Observable<List<RideHistoryEntity>> getHistories(TKPDMapParam<String, Object> parameters) {
        return mRideApi.getHistories(parameters);
    }

    @Override
    public Observable<RideHistoryResponse> getHistoriesWithPagination(TKPDMapParam<String, Object> parameters) {
        return mRideApi.getHistoriesWithPagination(parameters);
    }

    @Override
    public Observable<List<RideHistoryEntity>> getHistory(TKPDMapParam<String, Object> parameters) {
        return mRideApi.getHistory(parameters);
    }

    @Override
    public Observable<RideRequestEntity> getCurrentRequest(TKPDMapParam<String, Object> parameters) {
        return mRideApi.getCurrentRequest(parameters);
    }

    @Override
    public Observable<List<RideAddressEntity>> getAddresses(TKPDMapParam<String, Object> parameters) {
        return mRideApi.getAddresses(parameters);
    }

    @Override
    public Observable<String> sendRating(String requestId, TKPDMapParam<String, Object> parameters) {
        return mRideApi.sendRating(requestId, parameters);
    }

    @Override
    public Observable<CancelReasonsResponseEntity> getCancelReasons(TKPDMapParam<String, Object> parameters) {
        return mRideApi.cancelReasons(parameters);
    }

    @Override
    public Observable<UpdateDestinationEntity> updateRequest(TKPDMapParam<String, Object> parameters) {
        return mRideApi.updateRequest(parameters);
    }

    @Override
    public Observable<String> sendTip(TKPDMapParam<String, Object> parameters) {
        return mRideApi.sendTip(parameters);
    }

    @Override
    public Observable<PaymentMethodListEntity> getPaymentMethodList(TKPDMapParam<String, Object> parameters) {
        return mRideApi.getPaymentMethodList(parameters);
    }

    @Override
    public Observable<NearbyRidesEntity> getNearbyCars(TKPDMapParam<String, Object> parameters) {
        return mRideApi.getNearbyCars(parameters);
    }

    @Override
    public Observable<String> requestApi(String url, TKPDMapParam<String, Object> parameters) {
        return mRideApi.requestApi(url, parameters);
    }

    @Override
    public Observable<JsonObject> payPendingAmount() {
        return mRideApi.payPendingAmount();
    }

    @Override
    public Observable<GetPendingEntity> getPendingAmount() {
        return mRideApi.getPendingAmount();
    }
}
