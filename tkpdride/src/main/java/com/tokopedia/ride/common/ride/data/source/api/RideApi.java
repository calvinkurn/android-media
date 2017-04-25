package com.tokopedia.ride.common.ride.data.source.api;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.PromoEntity;
import com.tokopedia.ride.common.ride.data.entity.ReceiptEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestEntity;
import com.tokopedia.ride.common.ride.data.entity.RideRequestMapEntity;
import com.tokopedia.ride.common.ride.data.entity.RideHistoryEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateResponseEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alvarisi on 3/16/17.
 */

public interface RideApi {
    @GET(RideUrl.PRODUCTS)
    Observable<ProductResponseEntity> getProducts(@QueryMap Map<String, Object> param);

    @GET(RideUrl.ESTIMATED_TIME)
    Observable<TimesEstimateResponseEntity> getEstimateds(@QueryMap Map<String, Object> param);

    @GET(RideUrl.ESTIMATED_FARE)
    Observable<FareEstimateEntity> getFareEstimateds(@QueryMap Map<String, Object> param);

    @POST(RideUrl.REQUEST_CREATE)
    @FormUrlEncoded
    Observable<RideRequestEntity> createRequestRide(@FieldMap Map<String, Object> param);

    @POST(RideUrl.REQUEST_CANCEL)
    @FormUrlEncoded
    Observable<String> cancelRequest(@FieldMap Map<String, Object> param);

    @POST(RideUrl.REQUEST_DETAIL)
    @FormUrlEncoded
    Observable<RideRequestEntity> getDetailRequestRide(@FieldMap Map<String, Object> param);

    @GET(RideUrl.RECEIPT_DETAIL)
    Observable<ReceiptEntity> getReceipt(@QueryMap TKPDMapParam<String, Object> param);

    @GET(RideUrl.PROMO)
    Observable<PromoEntity> getPromo(TKPDMapParam<String, Object> param);

    @GET(RideUrl.REQUEST_MAP)
    Observable<RideRequestMapEntity> getRideMap(@Path("request_id") String requestId, @QueryMap TKPDMapParam<String, Object> parameters);

    @GET(RideUrl.TRANSACTIONS_ALL)
    Observable<List<RideHistoryEntity>> getHistories(@QueryMap TKPDMapParam<String, Object> parameters);

    @GET(RideUrl.TRANSACTION)
    Observable<RideHistoryEntity> getHistory(@QueryMap TKPDMapParam<String, Object> parameters);

    @GET(RideUrl.REQUEST_CURRENT)
    Observable<RideRequestEntity> getCurrentRequest(@QueryMap TKPDMapParam<String, Object> parameters);

    @POST(RideUrl.PROMO_APPLY)
    @FormUrlEncoded
    Observable<PromoEntity> applyPromo(TKPDMapParam<String, Object> parameters);
}
