package com.tokopedia.flight.common.data.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by alvarisi on 10/30/17.
 */

public class DeleteFlightCacheUseCase extends UseCase<Boolean> {
    private FlightRepository flightRepository;
    public static final String PARAM_RETURNING = "returning";

    @Inject
    public DeleteFlightCacheUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        if (requestParams == null || requestParams.getParameters().size() == 0) {
            return flightRepository.deleteFlightCacheSearch();
        } else {
            boolean isReturning = requestParams.getBoolean(PARAM_RETURNING, false);
            return flightRepository.deleteFlightCacheSearch(isReturning);
        }
    }

    public static RequestParams createRequestParam() {
        return RequestParams.EMPTY;
    }

    public static RequestParams createRequestParam(boolean isReturning) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean(PARAM_RETURNING, isReturning);
        return requestParams;
    }
}
