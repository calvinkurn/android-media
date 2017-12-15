package com.tokopedia.flight.common.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.domain.verifybooking.model.request.VerifyRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightRepository {
    Observable<List<FlightAirportDB>> getAirportList(String query);

    Observable<List<FlightClassEntity>> getFlightClasses();

    Observable<List<FlightAirlineDB>> getAirlineList();

    Observable<List<FlightAirlineDB>> getAirlineList(List<String> distinctSearchResultList);

    Observable<List<FlightAirlineDB>> getAirlineList(String airlineId);

    Observable<Boolean> deleteFlightCacheSearch();

    Observable<Boolean> deleteFlightCacheSearch(boolean isReturning);

    Observable<List<FlightSearchSingleRouteDB>> getFlightSearch(RequestParams requestParams);

    Observable<List<FlightMetaDataDB>> getFlightMetaData(RequestParams requestParams);

    Observable<Integer> getFlightSearchCount(RequestParams requestParams);

    Observable<FlightSearchSingleRouteDB> getFlightSearchById(boolean isReturning, String id);

    Observable<CartEntity> addCart(FlightCartRequest request, String idEmpotencyKey);

    Observable<Boolean> getAirportListBackground(long versionAirport);

    Observable<AttributesVoucher> checkVoucherCode(HashMap<String, String> paramsAllValueInString);

    Observable<DataResponseVerify> verifyBooking(VerifyRequest verifyRequest);

    Observable<Boolean> checkVersionAirport(long current_version);

    Observable<List<FlightOrder>> getOrders(Map<String, Object> maps);

    Observable<FlightOrder> getOrder(String id);
}
