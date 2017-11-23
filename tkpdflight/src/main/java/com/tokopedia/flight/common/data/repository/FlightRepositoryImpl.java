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

    public FlightRepositoryImpl(FlightAirportDataListSource flightAirportDataListSource,
                                FlightAirlineDataListSource flightAirlineDataListSource,
                                FlightSearchSingleDataSource flightSearchSingleDataListSource,
                                FlightSearchReturnDataSource flightSearchReturnDataListSource,
                                FlightClassesDataSource flightClassesDataSource,
                                FlightCartDataSource flightCartDataSource,
                                FlightMetaDataDBSource flightMetaDataDBSource,
                                FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource) {
        this.flightAirportDataListSource = flightAirportDataListSource;
        this.flightAirlineDataListSource = flightAirlineDataListSource;
        this.flightSearchSingleDataListSource = flightSearchSingleDataListSource;
        this.flightSearchReturnDataListSource = flightSearchReturnDataListSource;
        this.flightClassesDataSource = flightClassesDataSource;
        this.flightCartDataSource = flightCartDataSource;
        this.flightMetaDataDBSource = flightMetaDataDBSource;
        this.flightAirportDataListBackgroundSource = flightAirportDataListBackgroundSource;
    }

    @Override
    public Observable<List<FlightAirportDB>> getAirportList(String query) {
        return flightAirportDataListSource.getAirportList(query);
    }


//    @Override
//    public Observable<List<FlightAirportDB>> getAirportList(final List<String> airportIDFromResult) {
//        return flightAirportDataListSource.getCacheDataList(null).flatMap(new Func1<List<FlightAirportDB>, Observable<List<FlightAirportDB>>>() {
//            @Override
//            public Observable<List<FlightAirportDB>> call(final List<FlightAirportDB> flightAirportDBs) {
//                boolean isAirportInCache = true;
//
//                HashMap<String, FlightAirportDB> dbAirportMaps = new HashMap<>();
//                for (int i = 0, sizei = flightAirportDBs.size(); i < sizei; i++) {
//                    dbAirportMaps.put(flightAirportDBs.get(i).getAirportId(), flightAirportDBs.get(i));
//                }
//                for (int i = 0, sizei = airportIDFromResult.size(); i < sizei; i++) {
//                    if (!dbAirportMaps.containsKey(airportIDFromResult.get(i))) {
//                        isAirportInCache = false;
//                        break;
//                    }
//                }
//                if (isAirportInCache) {
//                    return Observable.just(flightAirportDBs);
//                } else {
//                    return flightAirportDataListSource.setCacheExpired().flatMap(new Func1<Boolean, Observable<List<FlightAirportDB>>>() {
//                        @Override
//                        public Observable<List<FlightAirportDB>> call(Boolean aBoolean) {
//                            return flightAirportDataListSource.getAirportList("");
//                        }
//                    });
//                }
//            }
//        });
//    }

    @Override
    public Observable<List<FlightAirlineDB>> getAirlineList() {
        return flightAirlineDataListSource.getAirlineList();
    }

    @Override
    public Observable<List<FlightAirlineDB>> getAirlineList(final List<String> airlineIDFromResult) {
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
}
