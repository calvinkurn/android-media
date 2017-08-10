package com.tokopedia.ride.common.ride.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.bookingride.data.RideAddressCache;
import com.tokopedia.ride.bookingride.data.RideAddressCacheImpl;
import com.tokopedia.ride.common.ride.data.entity.CancelReasonsResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
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

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 5/31/17.
 */

public class DiskBookingRideDataStore implements BookingRideDataStore {
    private RideAddressCache addressCache;

    public DiskBookingRideDataStore() {
        this.addressCache = new RideAddressCacheImpl();
    }

    @Override
    public Observable<ProductEntity> getProduct(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<List<ProductEntity>> getProducts(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<List<PriceEntity>> getPrices(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<List<TimesEstimateEntity>> getEstimatedTimes(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<FareEstimateEntity> getEstimatedFare(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<RideRequestEntity> createRideRequest(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<String> cancelRequest(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<RideRequestEntity> getRequestDetail(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<ReceiptEntity> getReceipt(TKPDMapParam<String, Object> param) {
        return null;
    }

    @Override
    public Observable<List<PromoEntity>> getPromo(TKPDMapParam<String, Object> param) {
        return null;
    }

    @Override
    public Observable<RideRequestMapEntity> getRideMap(TKPDMapParam<String, Object> parameters) {
        return null;
    }

    @Override
    public Observable<List<RideHistoryEntity>> getHistories(TKPDMapParam<String, Object> parameters) {
        return null;
    }

    @Override
    public Observable<RideHistoryResponse> getHistoriesWithPagination(TKPDMapParam<String, Object> parameters) {
        return null;
    }

    @Override
    public Observable<List<RideHistoryEntity>> getHistory(TKPDMapParam<String, Object> parameters) {
        return null;
    }

    @Override
    public Observable<RideRequestEntity> getCurrentRequest(TKPDMapParam<String, Object> parameters) {
        return null;
    }

    @Override
    public Observable<List<RideAddressEntity>> getAddresses(TKPDMapParam<String, Object> parameters) {
        return addressCache.getCache().onErrorReturn(new Func1<Throwable, List<RideAddressEntity>>() {
            @Override
            public List<RideAddressEntity> call(Throwable throwable) {
                return new ArrayList<>();
            }
        });
    }

    @Override
    public Observable<String> sendRating(String requestId, TKPDMapParam<String, Object> parameters) {
        return null;
    }

    @Override
    public Observable<CancelReasonsResponseEntity> getCancelReasons(TKPDMapParam<String, Object> parameters) {
        return null;
    }

    @Override
    public Observable<String> updateRequest(TKPDMapParam<String, Object> parameters) {
        return null;
    }
}
