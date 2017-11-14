package com.tokopedia.ride.common.ride.data;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.bookingride.data.entity.NearbyRidesEntity;
import com.tokopedia.ride.common.ride.data.entity.CancelReasonsResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.GetPendingEntity;
import com.tokopedia.ride.common.ride.data.entity.PayPendingEntity;
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

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface BookingRideDataStore {
    Observable<ProductEntity> getProduct(TKPDMapParam<String, Object> params);

    Observable<List<ProductEntity>> getProducts(TKPDMapParam<String, Object> params);

    Observable<List<PriceEntity>> getPrices(TKPDMapParam<String, Object> params);

    Observable<List<TimesEstimateEntity>> getEstimatedTimes(TKPDMapParam<String, Object> params);

    Observable<FareEstimateEntity> getEstimatedFare(TKPDMapParam<String, Object> params);

    Observable<RideRequestEntity> createRideRequest(TKPDMapParam<String, Object> params);

    Observable<String> cancelRequest(TKPDMapParam<String, Object> params);

    Observable<RideRequestEntity> getRequestDetail(TKPDMapParam<String, Object> params);

    Observable<ReceiptEntity> getReceipt(TKPDMapParam<String, Object> param);

    Observable<List<PromoEntity>> getPromo(TKPDMapParam<String, Object> param);

    Observable<RideRequestMapEntity> getRideMap(TKPDMapParam<String, Object> parameters);

    Observable<List<RideHistoryEntity>> getHistories(TKPDMapParam<String, Object> parameters);

    Observable<RideHistoryResponse> getHistoriesWithPagination(TKPDMapParam<String, Object> parameters);

    Observable<List<RideHistoryEntity>> getHistory(TKPDMapParam<String, Object> parameters);

    Observable<RideRequestEntity> getCurrentRequest(TKPDMapParam<String, Object> parameters);

    Observable<List<RideAddressEntity>> getAddresses(TKPDMapParam<String, Object> parameters);

    Observable<String> sendRating(String requestId, TKPDMapParam<String, Object> parameters);

    Observable<CancelReasonsResponseEntity> getCancelReasons(TKPDMapParam<String, Object> parameters);

    Observable<UpdateDestinationEntity> updateRequest(TKPDMapParam<String, Object> parameters);

    Observable<String> sendTip(TKPDMapParam<String, Object> parameters);

    Observable<PaymentMethodListEntity> getPaymentMethodList(TKPDMapParam<String, Object> parameters);

    Observable<NearbyRidesEntity> getNearbyCars(TKPDMapParam<String, Object> parameters);

    Observable<String> requestApi(String url, TKPDMapParam<String, Object> parameters);

    Observable<JsonObject> payPendingAmount();

    Observable<GetPendingEntity> getPendingAmount();
}
