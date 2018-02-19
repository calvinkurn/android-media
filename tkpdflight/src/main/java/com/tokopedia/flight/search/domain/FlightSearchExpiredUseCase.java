package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 15/02/18.
 */

public class FlightSearchExpiredUseCase extends UseCase<Boolean> {
    private static final String PARAM_IS_RETURNING = "PARAM_IS_RETURNING";

    private final FlightRepository flightRepository;

    @Inject
    public FlightSearchExpiredUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return flightRepository.isSearchCacheExpired(requestParams, PARAM_IS_RETURNING);
    }

    public RequestParams createRequestParams(boolean isReturning) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean(PARAM_IS_RETURNING, isReturning);
        return requestParams;
    }
}
