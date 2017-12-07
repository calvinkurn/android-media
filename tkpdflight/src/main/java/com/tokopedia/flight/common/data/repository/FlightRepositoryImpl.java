package com.tokopedia.flight.common.data.repository;

import com.tokopedia.flight.airline.data.FlightAirlineDataListSource;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListBackgroundSource;
import com.tokopedia.flight.booking.data.cloud.FlightCartDataSource;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.data.cloud.FlightClassesDataSource;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.review.data.FlightCheckVoucheCodeDataSource;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.search.data.FlightSearchReturnDataSource;
import com.tokopedia.flight.search.data.FlightSearchSingleDataSource;
import com.tokopedia.flight.search.data.db.FlightMetaDataDBSource;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchMetaParamUtil;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightRepositoryImpl implements FlightRepository {

    private FlightAirportDataListSource flightAirportDataListSource;
    private FlightAirlineDataListSource flightAirlineDataListSource;
    private FlightClassesDataSource flightClassesDataSource;
    private FlightSearchSingleDataSource flightSearchSingleDataListSource;
    private FlightSearchReturnDataSource flightSearchReturnDataListSource;
    private FlightCartDataSource flightCartDataSource;
    private FlightMetaDataDBSource flightMetaDataDBSource;
    private FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource;
    private FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource;

    public FlightRepositoryImpl(FlightAirportDataListSource flightAirportDataListSource,
                                FlightAirlineDataListSource flightAirlineDataListSource,
                                FlightSearchSingleDataSource flightSearchSingleDataListSource,
                                FlightSearchReturnDataSource flightSearchReturnDataListSource,
                                FlightClassesDataSource flightClassesDataSource,
                                FlightCartDataSource flightCartDataSource,
                                FlightMetaDataDBSource flightMetaDataDBSource,
                                FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource,
                                FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource) {
        this.flightAirportDataListSource = flightAirportDataListSource;
        this.flightAirlineDataListSource = flightAirlineDataListSource;
        this.flightSearchSingleDataListSource = flightSearchSingleDataListSource;
        this.flightSearchReturnDataListSource = flightSearchReturnDataListSource;
        this.flightClassesDataSource = flightClassesDataSource;
        this.flightCartDataSource = flightCartDataSource;
        this.flightMetaDataDBSource = flightMetaDataDBSource;
        this.flightAirportDataListBackgroundSource = flightAirportDataListBackgroundSource;
        this.flightCheckVoucheCodeDataSource = flightCheckVoucheCodeDataSource;
    }

    @Override
    public Observable<List<FlightAirportDB>> getAirportList(String query) {
        return flightAirportDataListSource.getAirportList(query);
    }

    @Override
    public Observable<List<FlightAirlineDB>> getAirlineList() {
        return flightAirlineDataListSource.getAirlineList();
    }

    /**
     * will compare between the list and the cache (if not expired)
     * If the cache already has ALL the airline in the list, then it will return as is.
     * Otherwise, it will hit the cloud.
     *
     * Example:
     * List: CA, JT. Cache: AB, AC, CB, JT
     * it will hit the cloud, because it does not have CA in cache
     *
     * List: AB, JT. Cache: AB, AC, CB, JT
     * All in list is in the cache, so, it will not hit cloud
     */
    @Override
    public Observable<List<FlightAirlineDB>> getAirlineList(final List<String> airlineIDFromResult) {
        return flightAirlineDataListSource.isCacheExpired().flatMap(new Func1<Boolean, Observable<List<FlightAirlineDB>>>() {
            @Override
            public Observable<List<FlightAirlineDB>> call(Boolean expired) {
                if (expired) {
                    return flightAirlineDataListSource.getAirlineList();
                } else {
                    return flightAirlineDataListSource.getCacheDataList(null).flatMap(new Func1<List<FlightAirlineDB>, Observable<List<FlightAirlineDB>>>() {
                        @Override
                        public Observable<List<FlightAirlineDB>> call(final List<FlightAirlineDB> flightAirlineDBs) {
                            boolean isAirlineInCache = true;

                            HashMap<String, FlightAirlineDB> dbAirlineMaps = new HashMap<>();
                            for (int i = 0, sizei = flightAirlineDBs.size(); i < sizei; i++) {
                                dbAirlineMaps.put(flightAirlineDBs.get(i).getId(), flightAirlineDBs.get(i));
                            }
                            for (int i = 0, sizei = airlineIDFromResult.size(); i < sizei; i++) {
                                if (!dbAirlineMaps.containsKey(airlineIDFromResult.get(i))) {
                                    isAirlineInCache = false;
                                    break;
                                }
                            }
                            if (isAirlineInCache) {
                                return Observable.just(flightAirlineDBs);
                            } else {
                                return flightAirlineDataListSource.setCacheExpired().flatMap(new Func1<Boolean, Observable<List<FlightAirlineDB>>>() {
                                    @Override
                                    public Observable<List<FlightAirlineDB>> call(Boolean aBoolean) {
                                        return flightAirlineDataListSource.getAirlineList();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public Observable<Boolean> deleteFlightCacheSearch() {
        return flightSearchSingleDataListSource.deleteCache().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                return flightSearchReturnDataListSource.deleteCache();
            }
        });
    }

    @Override
    public Observable<Boolean> deleteFlightCacheSearch(boolean isReturning) {
        if (isReturning) {
            return flightSearchReturnDataListSource.deleteCache();
        } else {
            return flightSearchSingleDataListSource.deleteCache();
        }
    }

    @Override
    public Observable<List<FlightAirlineDB>> getAirlineList(String airlineId) {
        return flightAirlineDataListSource.getAirlineList(airlineId);
    }

    @Override
    public Observable<List<FlightClassEntity>> getFlightClasses() {
        return flightClassesDataSource.getClasses();
    }

    @Override
    public Observable<List<FlightSearchSingleRouteDB>> getFlightSearch(RequestParams requestParams) {
        if (FlightSearchParamUtil.isReturning(requestParams)) {
            return flightSearchReturnDataListSource.getDataList(requestParams);
        } else {
            return flightSearchSingleDataListSource.getDataList(requestParams);
        }
    }

    @Override
    public Observable<List<FlightMetaDataDB>> getFlightMetaData(RequestParams requestParams) {
        return flightMetaDataDBSource.getData(FlightSearchMetaParamUtil.toHashMap(requestParams));
    }

    @Override
    public Observable<Integer> getFlightSearchCount(RequestParams requestParams) {
        if (FlightSearchParamUtil.isReturning(requestParams)) {
            return flightSearchReturnDataListSource.getCacheDataListCount(FlightSearchParamUtil.toHashMap(requestParams));
        } else {
            return flightSearchSingleDataListSource.getCacheDataListCount(FlightSearchParamUtil.toHashMap(requestParams));
        }
    }

    @Override
    public Observable<FlightSearchSingleRouteDB> getFlightSearchById(boolean isReturning, String id) {
        if (isReturning) {
            return flightSearchReturnDataListSource.getSingleFlight(id);
        } else {
            return flightSearchSingleDataListSource.getSingleFlight(id);
        }
    }

    @Override
    public Observable<CartEntity> addCart(FlightCartRequest request, String idEmpotencyKey) {
        return flightCartDataSource.addCart(request, idEmpotencyKey);
    }

    @Override
    public Observable<Boolean> getAirportListBackground() {
        return flightAirportDataListBackgroundSource.getAirportList();
    }

    @Override
    public Observable<AttributesVoucher> checkVoucherCode(HashMap<String, String> paramsAllValueInString) {
        return flightCheckVoucheCodeDataSource.checkVoucherCode(paramsAllValueInString);
    }
}
