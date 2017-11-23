package com.tokopedia.flight.airport.data.source;

import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListCloudSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 11/21/17.
 */

public class FlightAirportDataListBackgroundSource {

    private final FlightAirportDataListDBSource flightAirportDataListDBSource;
    private final FlightAirportDataListCloudSource flightAirportDataListCloudSource;

    @Inject
    public FlightAirportDataListBackgroundSource(FlightAirportDataListDBSource flightAirportDataListDBSource,
                                                 FlightAirportDataListCloudSource flightAirportDataListCloudSource) {
        this.flightAirportDataListDBSource = flightAirportDataListDBSource;
        this.flightAirportDataListCloudSource = flightAirportDataListCloudSource;
    }

    public Observable<Boolean> getAirportList() {
        return flightAirportDataListCloudSource.getData(new HashMap<String, Object>())
                .flatMap(new Func1<List<FlightAirportCountry>, Observable<Boolean>>() {
                             @Override
                             public Observable<Boolean> call(List<FlightAirportCountry> flightAirportCountries) {
                                 if (flightAirportCountries == null) {
                                     return Observable.just(false);
                                 }
                                 return flightAirportDataListDBSource.insertAll(flightAirportCountries);
                             }
                         }
                );
    }
}
