package com.tokopedia.flight.orderlist.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by alvarisi on 12/6/17.
 */

public class FlightGetOrderUseCase extends UseCase<OrderEntity> {
    private static final String PARAM_ID = "id";
    private static final String DEFAULT_EMPTY_VALUE = "";
    private FlightRepository flightRepository;

    public FlightGetOrderUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<OrderEntity> createObservable(RequestParams requestParams) {
        return flightRepository.getOrder(requestParams.getString(PARAM_ID, DEFAULT_EMPTY_VALUE));
    }
}
