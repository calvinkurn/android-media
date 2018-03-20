package com.tokopedia.flight.common.data.repository;

import com.tokopedia.flight.airline.data.FlightAirlineDataListSource;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListBackgroundSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.db.FlightAirportVersionDBSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.banner.data.source.BannerDataSource;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.booking.data.FlightPassengerFactorySource;
import com.tokopedia.flight.booking.data.cloud.FlightCartDataSource;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.booking.data.db.model.FlightPassengerDb;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.data.cloud.FlightClassesDataSource;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderMapper;
import com.tokopedia.flight.review.data.FlightBookingDataSource;
import com.tokopedia.flight.review.data.FlightCheckVoucheCodeDataSource;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.request.VerifyRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.flight.search.data.FlightSearchReturnDataSource;
import com.tokopedia.flight.search.data.FlightSearchSingleDataSource;
import com.tokopedia.flight.search.data.db.FlightMetaDataDBSource;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchMetaParamUtil;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightRepositoryImpl implements FlightRepository {
    private BannerDataSource bannerDataSource;
    private FlightAirportDataListSource flightAirportDataListSource;
    private FlightAirlineDataListSource flightAirlineDataListSource;
    private FlightClassesDataSource flightClassesDataSource;
    private FlightSearchSingleDataSource flightSearchSingleDataListSource;
    private FlightSearchReturnDataSource flightSearchReturnDataListSource;
    private FlightCartDataSource flightCartDataSource;
    private FlightMetaDataDBSource flightMetaDataDBSource;
    private FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource;
    private FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource;
    private FlightBookingDataSource flightBookingDataSource;
    private FlightAirportVersionDBSource flightAirportVersionDBSource;
    private FlightOrderDataSource flightOrderDataSource;
    private FlightOrderMapper flightOrderMapper;
    private FlightPassengerFactorySource flightPassengerFactorySource;

    public FlightRepositoryImpl(BannerDataSource bannerDataSource,
                                FlightAirportDataListSource flightAirportDataListSource,
                                FlightAirlineDataListSource flightAirlineDataListSource,
                                FlightSearchSingleDataSource flightSearchSingleDataListSource,
                                FlightSearchReturnDataSource flightSearchReturnDataListSource,
                                FlightClassesDataSource flightClassesDataSource,
                                FlightCartDataSource flightCartDataSource,
                                FlightMetaDataDBSource flightMetaDataDBSource,
                                FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource,
                                FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource,
                                FlightBookingDataSource flightBookingDataSource,
                                FlightAirportVersionDBSource flightAirportVersionDBSource,
                                FlightOrderDataSource flightOrderDataSource,
                                FlightOrderMapper flightOrderMapper,
                                FlightPassengerFactorySource flightPassengerFactorySource) {
        this.bannerDataSource = bannerDataSource;
        this.flightAirportDataListSource = flightAirportDataListSource;
        this.flightAirlineDataListSource = flightAirlineDataListSource;
        this.flightSearchSingleDataListSource = flightSearchSingleDataListSource;
        this.flightSearchReturnDataListSource = flightSearchReturnDataListSource;
        this.flightClassesDataSource = flightClassesDataSource;
        this.flightCartDataSource = flightCartDataSource;
        this.flightMetaDataDBSource = flightMetaDataDBSource;
        this.flightAirportDataListBackgroundSource = flightAirportDataListBackgroundSource;
        this.flightCheckVoucheCodeDataSource = flightCheckVoucheCodeDataSource;
        this.flightBookingDataSource = flightBookingDataSource;
        this.flightAirportVersionDBSource = flightAirportVersionDBSource;
        this.flightOrderDataSource = flightOrderDataSource;
        this.flightOrderMapper = flightOrderMapper;
        this.flightPassengerFactorySource = flightPassengerFactorySource;
    }

    @Override
    public Observable<List<FlightAirportDB>> getAirportList(String query) {
        return flightAirportDataListSource.getAirportList(query);
    }

    @Override
    public Observable<FlightAirportDB> getAirportById(final String aiport) {
        return flightAirportDataListSource.getAirport(aiport);
    }

    @Override
    public Observable<FlightAirportDB> getAirportWithParam(Map<String, String> params) {
        return flightAirportDataListSource.getAirport(params);
    }

    @Override
    public Observable<List<FlightAirportDB>> getAirportList(final String query, final String idCountry) {
        if (query != null && query.length() > 0 && idCountry != null && idCountry.length() > 0) {
            return flightAirportDataListSource.getAirportList(query, idCountry);
        } else {
            return flightAirportDataListSource.getAirportCount(query, idCountry)
                    .flatMap(new Func1<Integer, Observable<List<FlightAirportDB>>>() {
                        @Override
                        public Observable<List<FlightAirportDB>> call(Integer airportTotal) {
                            if (airportTotal == 0) {
                                flightAirportDataListSource.deleteCache();
                                flightAirlineDataListSource.setCacheExpired();
                            }
                            return flightAirportDataListSource.getAirportList(query, idCountry);
                        }
                    });
        }
    }

    @Override
    public Observable<List<FlightAirportDB>> getPhoneCodeList(String query) {
        return flightAirportDataListSource.getPhoneCodeList(query);
    }

    @Override
    public Observable<FlightAirlineDB> getAirlineById(final String airlineId) {
        return flightAirlineDataListSource.getAirline(airlineId);
    }

    @Override
    public Observable<SendEmailEntity> sendEmail(Map<String, Object> params) {
        return flightOrderDataSource.sendEmail(params);
    }

    @Override
    public Observable<Boolean> isSearchCacheExpired(boolean isReturn) {
        if (isReturn) {
            return flightSearchReturnDataListSource.isCacheExpired()
                    .zipWith(flightSearchReturnDataListSource.isDataAvailable(),
                            new Func2<Boolean, Boolean, Boolean>() {
                                @Override
                                public Boolean call(Boolean isExpired, Boolean isLocalDataAvailable) {
                                    return isExpired && isLocalDataAvailable;
                                }
                            });
        } else {
            return flightSearchSingleDataListSource.isCacheExpired()
                    .zipWith(flightSearchSingleDataListSource.isDataAvailable(),
                            new Func2<Boolean, Boolean, Boolean>() {
                                @Override
                                public Boolean call(Boolean isExpired, Boolean isLocalDataAvailable) {
                                    return isExpired && isLocalDataAvailable;
                                }
                            });
        }
    }

    @Override
    public Observable<List<FlightAirlineDB>> getAirlineList() {
        return flightAirlineDataListSource.getAirlineList();
    }

    /**
     * will compare between the list and the cache (if not expired)
     * If the cache already has ALL the airline in the list, then it will return as is.
     * Otherwise, it will hit the cloud.
     * <p>
     * Example:
     * List: CA, JT. Cache: AB, AC, CB, JT
     * it will hit the cloud, because it does not have CA in cache
     * <p>
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
        }).onErrorReturn(new Func1<Throwable, List<FlightAirlineDB>>() {
            @Override
            public List<FlightAirlineDB> call(Throwable throwable) {
                return new ArrayList<>();
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
    public Observable<FlightClassEntity> getFlightClassById(final int classId) {
        return flightClassesDataSource.getClasses()
                .flatMap(new Func1<List<FlightClassEntity>, Observable<FlightClassEntity>>() {
                    @Override
                    public Observable<FlightClassEntity> call(final List<FlightClassEntity> flightClassEntities) {
                        return Observable.from(flightClassEntities)
                                .filter(new Func1<FlightClassEntity, Boolean>() {
                                    @Override
                                    public Boolean call(FlightClassEntity flightClassEntity) {
                                        return flightClassEntity.getId() == classId;
                                    }
                                });
                    }
                });
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
    public Observable<Boolean> getAirportListBackground(long versionAirport) {
        return flightAirportDataListBackgroundSource.getAirportList(versionAirport);
    }

    @Override
    public Observable<AttributesVoucher> checkVoucherCode(HashMap<String, String> paramsAllValueInString) {
        return flightCheckVoucheCodeDataSource.checkVoucherCode(paramsAllValueInString);
    }

    @Override
    public Observable<DataResponseVerify> verifyBooking(VerifyRequest verifyRequest) {
        return flightBookingDataSource.verifyBooking(verifyRequest);
    }

    @Override
    public Observable<FlightCheckoutEntity> checkout(FlightCheckoutRequest request) {
        return flightBookingDataSource.checkout(request);
    }

    @Override
    public Observable<Boolean> checkVersionAirport(long versionOnCloud) {
        if (flightAirportVersionDBSource.getVersion() < versionOnCloud) {
            return Observable.just(true);
        } else {
            return Observable.just(false);
        }
    }

    @Override
    public Observable<List<FlightOrder>> getOrders(Map<String, Object> maps) {
        return flightOrderDataSource.getOrders(maps)
                .map(new Func1<List<OrderEntity>, List<FlightOrder>>() {
                    @Override
                    public List<FlightOrder> call(List<OrderEntity> orderEntities) {
                        return flightOrderMapper.transform(orderEntities);
                    }
                });

    }

    @Override
    public Observable<FlightOrder> getOrder(String id) {
        return flightOrderDataSource.getOrder(id)
                .map(new Func1<OrderEntity, FlightOrder>() {
                    @Override
                    public FlightOrder call(OrderEntity orderEntity) {
                        return flightOrderMapper.transform(orderEntity);
                    }
                });
    }

    @Override
    public Observable<List<BannerDetail>> getBanners(Map<String, String> params) {
        return bannerDataSource.getBannerData(params);
    }

    @Override
    public Observable<List<FlightPassengerDb>> getSavedPassenger(String passengerId) {
        return flightPassengerFactorySource.getPassengerList(passengerId);
    }

    @Override
    public Observable<Boolean> updateIsSelected(String passengerId, int isSelected) {
        return flightPassengerFactorySource.updateIsSelected(passengerId, isSelected);
    }

    @Override
    public Observable<Boolean> deleteAllListPassenger() {
        return flightPassengerFactorySource.deleteAllListPassenger();
    }

    @Override
    public Observable<Response<Object>> deletePassenger(DeletePassengerRequest request, String idempotencyKey) {
        return flightPassengerFactorySource.deletePassenger(request, idempotencyKey);
    }

    @Override
    public Observable<List<FlightAirlineDB>> refreshAirlines() {
        return flightAirlineDataListSource.setCacheExpired().flatMap(new Func1<Boolean, Observable<List<FlightAirlineDB>>>() {
            @Override
            public Observable<List<FlightAirlineDB>> call(Boolean aBoolean) {
                return flightAirlineDataListSource.getAirlineList();
            }
        });
    }

    @Override
    public Observable<FlightAirlineDB> getAirlineCacheById(String airlineId) {
        return flightAirlineDataListSource.getCacheAirline(airlineId);
    }
}
