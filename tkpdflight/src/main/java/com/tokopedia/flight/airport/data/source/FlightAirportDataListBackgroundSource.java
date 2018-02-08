package com.tokopedia.flight.airport.data.source;

import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListCloudSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource;
import com.tokopedia.flight.airport.data.source.db.FlightAirportVersionDBSource;

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
    private FlightAirportVersionDBSource flightAirportVersionDBSource;

    @Inject
    public FlightAirportDataListBackgroundSource(FlightAirportDataListDBSource flightAirportDataListDBSource,
                                                 FlightAirportDataListCloudSource flightAirportDataListCloudSource,
                                                 FlightAirportVersionDBSource flightAirportVersionDBSource) {
        this.flightAirportDataListDBSource = flightAirportDataListDBSource;
        this.flightAirportDataListCloudSource = flightAirportDataListCloudSource;
        this.flightAirportVersionDBSource = flightAirportVersionDBSource;
    }

    public Observable<Boolean> getAirportList(final long versionAirport) {
        return flightAirportDataListCloudSource.getData(new HashMap<String, Object>())
                .flatMap(new Func1<List<FlightAirportCountry>, Observable<Boolean>>() {
                             @Override
                             public Observable<Boolean> call(List<FlightAirportCountry> flightAirportCountries) {
                                 if (flightAirportCountries == null) {
                                     return Observable.just(false);
                                 }
                                 flightAirportVersionDBSource.updateVersion(versionAirport);
                                 return updateDatabaseList(flightAirportCountries);
                             }
                         }
                );
    }

    private Observable<Boolean> updateDatabaseList(final List<FlightAirportCountry> flightAirportCountries) {
        return flightAirportDataListDBSource.updateAndInsert(flightAirportCountries);
    }
}
