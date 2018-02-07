package com.tokopedia.ride.common.ride.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.bookingride.domain.model.NearbyRides;
import com.tokopedia.ride.bookingride.domain.model.Promo;
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

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface BookingRideRepository {
    Observable<Product> getProduct(TKPDMapParam<String, Object> params);

    Observable<List<Product>> getProducts(TKPDMapParam<String, Object> params);

    Observable<List<TimesEstimate>> getEstimatedTimes(TKPDMapParam<String, Object> params);

    Observable<FareEstimate> getEstimatedFare(TKPDMapParam<String, Object> params);

    Observable<RideRequest> createRequest(TKPDMapParam<String, Object> params);

    Observable<String> cancelRequest(TKPDMapParam<String, Object> params);

    Observable<RideRequest> getRequestDetail(TKPDMapParam<String, Object> parameters);

    Observable<Receipt> getReceipt(TKPDMapParam<String, Object> param);

    Observable<List<Promo>> getPromo(TKPDMapParam<String, Object> parameters);

    Observable<String> getRideMap(TKPDMapParam<String, Object> parameters);

    Observable<List<RideHistory>> getHistories(TKPDMapParam<String, Object> parameters);

    Observable<RideHistoryWrapper> getHistoriesWithPagination(TKPDMapParam<String, Object> parameters);

    Observable<RideHistory> getHistory(TKPDMapParam<String, Object> parameters);

    Observable<RideRequest> getCurrentRequest(TKPDMapParam<String, Object> parameters);

    Observable<List<RideAddress>> getAddresses(TKPDMapParam<String, Object> parameters);

    Observable<List<RideAddress>> getAddressesFromCache();

    Observable<String> sendRating(String requestId, TKPDMapParam<String, Object> parameters);

    Observable<List<String>> getCancelReasons(TKPDMapParam<String, Object> parameters);

    Observable<List<TimePriceEstimate>> getTimePriceEstimate(TKPDMapParam<String, Object> parameters);

    Observable<List<PriceEstimate>> getPriceEstimate(TKPDMapParam<String, Object> parameters);

    Observable<UpdateDestination> updateRequest(TKPDMapParam<String, Object> parameters);

    Observable<String> sendTip(TKPDMapParam<String, Object> parameters);

    Observable<PaymentMethodList> getPaymentMethodList(TKPDMapParam<String, Object> parameters);

    Observable<NearbyRides> getNearbyCars(TKPDMapParam<String, Object> parameters);

    Observable<String> requestApi(String url, TKPDMapParam<String, Object> parameters);

    Observable<PaymentMethodList> getPaymentMethodListFromCache();

    Observable<PayPending> payPendingAmount();

    Observable<GetPending> getPendingAmount();
}