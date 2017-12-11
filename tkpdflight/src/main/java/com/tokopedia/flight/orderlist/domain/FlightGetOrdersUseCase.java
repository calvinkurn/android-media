package com.tokopedia.flight.orderlist.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author by alvarisi on 12/6/17.
 */

public class FlightGetOrdersUseCase extends UseCase<List<OrderEntity>> {
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PER_PAGE = "per_page";
    private static final int DEFAULT_PER_PAGE_VALUE = 10;
    private FlightRepository flightRepository;


    public FlightGetOrdersUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<OrderEntity>> createObservable(RequestParams requestParams) {
        return flightRepository.getOrders(requestParams.getParameters());
    }

    public RequestParams createRequestParam(int page) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_PAGE, page);
        requestParams.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE_VALUE);
        return requestParams;
    }
}
