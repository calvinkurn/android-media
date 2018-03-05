package com.tokopedia.flight.common.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.booking.data.db.model.FlightPassengerDB;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
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

    Observable<FlightAirportDB> getAirportById(String aiport);

    Observable<List<FlightAirportDB>> getAirportList(String query, String idCountry);

    Observable<List<FlightClassEntity>> getFlightClasses();

    Observable<FlightClassEntity> getFlightClassById(int classId);

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

    Observable<FlightCheckoutEntity> checkout(FlightCheckoutRequest request);

    Observable<Boolean> checkVersionAirport(long current_version);

    Observable<List<FlightOrder>> getOrders(Map<String, Object> maps);

    Observable<FlightOrder> getOrder(String id);

    Observable<List<BannerDetail>> getBanners(Map<String, String> params);

    Observable<List<FlightAirportDB>> getPhoneCodeList(String string);

    Observable<FlightAirlineDB> getAirlineById(String airlineId);

    Observable<SendEmailEntity> sendEmail(Map<String, Object> params);

    Observable<List<FlightPassengerDB>> getSavedPassenger();

    Observable<Boolean> updateIsSelected(String passengerId, int isSelected);
}
