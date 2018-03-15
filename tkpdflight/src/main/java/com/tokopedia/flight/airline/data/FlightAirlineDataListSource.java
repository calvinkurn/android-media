package com.tokopedia.flight.airline.data;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.flight.airline.data.cache.FlightAirlineDataCacheSource;
import com.tokopedia.flight.airline.data.cloud.FlightAirlineDataListCloudSource;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airline.util.FlightAirlineParamUtil;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightAirlineDataListSource extends DataListSource<AirlineData, FlightAirlineDB> {
    private static final String DEFAULT_EMPTY_VALUE = "";
    private FlightAirlineDataListDBSource flightAirlineDataListDBSource;
    private FlightAirlineDataListCloudSource flightAirlineDataListCloudSource;

    @Inject
    public FlightAirlineDataListSource(FlightAirlineDataCacheSource flightAirlineDataListCacheSource,
                                       FlightAirlineDataListDBSource flightAirlineDataListDBSource,
                                       FlightAirlineDataListCloudSource flightAirlineDataListCloudSource) {
        super(flightAirlineDataListCacheSource, flightAirlineDataListDBSource, flightAirlineDataListCloudSource);
        this.flightAirlineDataListDBSource = flightAirlineDataListDBSource;
        this.flightAirlineDataListCloudSource = flightAirlineDataListCloudSource;
    }

    public static HashMap<String, Object> generateGetParam(String idToSearch) {
        return FlightAirlineParamUtil.generateMap(idToSearch);
    }

    public Observable<List<FlightAirlineDB>> getAirlineList(final String idToSearch) {
        final HashMap<String, Object> map = generateGetParam(idToSearch);
        return getDataList(map);
    }

    public Observable<List<FlightAirlineDB>> getAirlineList() {
        return getDataList(null);
    }

    public Observable<FlightAirlineDB> getAirline(final String airlineId) {
        return flightAirlineDataListDBSource.getAirline(airlineId).map(new Func1<FlightAirlineDB, FlightAirlineDB>() {
            @Override
            public FlightAirlineDB call(FlightAirlineDB flightAirlineDB) {
                if (flightAirlineDB == null){
                    return new FlightAirlineDB(
                            airlineId,
                            DEFAULT_EMPTY_VALUE,
                            DEFAULT_EMPTY_VALUE,
                            DEFAULT_EMPTY_VALUE,
                            0
                    );
                }
                return flightAirlineDB;
            }
        });
        /*return flightAirlineDataListDBSource.isDataAvailable().flatMap(new Func1<Boolean, Observable<FlightAirlineDB>>() {
            @Override
            public Observable<FlightAirlineDB> call(Boolean aBoolean) {
                if (aBoolean) {
                    return flightAirlineDataListDBSource.getAirline(airlineId)
                            .flatMap(new Func1<FlightAirlineDB, Observable<FlightAirlineDB>>() {
                                @Override
                                public Observable<FlightAirlineDB> call(FlightAirlineDB flightAirlineDB) {
                                    if (flightAirlineDB == null)
                                        return getFlightAirlineFromCloud(airlineId);
                                    else
                                        return Observable.just(flightAirlineDB);
                                }
                            });
                } else {
                    return getFlightAirlineFromCloud(airlineId);
                }
            }
        });*/
    }

    private Observable<FlightAirlineDB> getFlightAirlineFromCloud(final String airlineId) {
        return flightAirlineDataListCloudSource.getAirline(airlineId)
                .flatMap(new Func1<AirlineData, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(AirlineData airlineData) {
                        return flightAirlineDataListDBSource.insert(airlineData);
                    }
                }).flatMap(new Func1<Boolean, Observable<FlightAirlineDB>>() {
                    @Override
                    public Observable<FlightAirlineDB> call(Boolean aBoolean) {
                        return flightAirlineDataListDBSource.getAirline(airlineId);
                    }
                }).onErrorReturn(new Func1<Throwable, FlightAirlineDB>() {
                    @Override
                    public FlightAirlineDB call(Throwable throwable) {
                        return new FlightAirlineDB(
                                airlineId,
                                DEFAULT_EMPTY_VALUE,
                                DEFAULT_EMPTY_VALUE,
                                DEFAULT_EMPTY_VALUE,
                                0
                        );
                    }
                });
    }
}
