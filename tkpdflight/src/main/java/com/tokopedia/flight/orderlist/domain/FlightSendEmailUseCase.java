package com.tokopedia.flight.orderlist.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func4;

/**
 * @author by alvarisi on 12/6/17.
 */

public class FlightSendEmailUseCase extends UseCase<SendEmailEntity> {
    private static final String PARAM_ID = "invoice_id";
    private static final String USER_ID = "user_id";
    private static final String PARAM_EMAIL = "email";
    private static final String DEFAULT_EMPTY_VALUE = "";
    private FlightRepository flightRepository;

    public FlightSendEmailUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<SendEmailEntity> createObservable(RequestParams requestParams) {
        return flightRepository.sendEmail(requestParams.getParameters());

    }

    public RequestParams createRequestParams(String orderId, String userId, String email) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_ID, orderId);
        requestParams.putString(USER_ID, userId);
        requestParams.putString(PARAM_EMAIL, email);
        return requestParams;
    }
}
