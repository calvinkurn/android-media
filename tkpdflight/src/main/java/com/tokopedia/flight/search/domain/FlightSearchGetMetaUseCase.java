package com.tokopedia.flight.search.domain;

import android.text.TextUtils;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.data.cloud.model.response.Meta;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchMetaParamUtil;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightSearchGetMetaUseCase extends UseCase<FlightMetaDataDB> {
    private final FlightRepository flightRepository;

    @Inject
    public FlightSearchGetMetaUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<FlightMetaDataDB> createObservable(RequestParams requestParams) {
        return flightRepository.getFlightMetaData(requestParams).flatMap(new Func1<List<FlightMetaDataDB>, Observable<FlightMetaDataDB>>() {
            @Override
            public Observable<FlightMetaDataDB> call(List<FlightMetaDataDB> flightMetaDataDBs) {
                if (flightMetaDataDBs!=null && flightMetaDataDBs.size() > 0) {
                    return Observable.just(flightMetaDataDBs.get(0));
                } else {
                    return Observable.error(new RuntimeException("FlightMetaDataDB is empty"));
                }
            }
        });
    }

    public static RequestParams generateRequestParams(String departureAirport, String arrivalAirport, String date) {
        return FlightSearchMetaParamUtil.generateRequestParams(departureAirport, arrivalAirport, date);
    }

}
