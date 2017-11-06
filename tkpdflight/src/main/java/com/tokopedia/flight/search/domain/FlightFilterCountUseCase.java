package com.tokopedia.flight.search.domain;

import android.text.TextUtils;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.data.cloud.model.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightFilterCountUseCase extends UseCase<Integer> {
    private final FlightRepository flightRepository;

    @Inject
    public FlightFilterCountUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Integer> createObservable(RequestParams requestParams) {
        return flightRepository.getFlightSearchCount(requestParams);
    }

}
