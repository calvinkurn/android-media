package com.tokopedia.flight.booking.domain;

import com.tokopedia.flight.booking.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 05/03/18.
 */

public class FlightBookingDeletePassengerUseCase extends UseCase<Boolean> {
    private static final String PARAM_PASSENGER_ID = "PARAM_PASSENGER_ID";
    private static final String PARAM_IDEMPOTENCY = "PARAM_IDEMPOTENCY";
    private static final String DEFAULT_PARAM = "";
    private FlightRepository flightRepository;

    @Inject
    public FlightBookingDeletePassengerUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(final RequestParams requestParams) {
        return createRequest(requestParams)
                .flatMap(new Func1<DeletePassengerRequest, Observable<Response<Object>>>() {
                    @Override
                    public Observable<Response<Object>> call(DeletePassengerRequest deletePassengerRequest) {
                        return flightRepository.deletePassenger(
                                deletePassengerRequest,
                                requestParams.getString(PARAM_IDEMPOTENCY, DEFAULT_PARAM)
                        );
                    }
                })
                .flatMap(new Func1<Response<Object>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<Object> objectResponse) {
                        return flightRepository.deleteAllListPassenger();
                    }
                });
    }

    private Observable<DeletePassengerRequest> createRequest(RequestParams requestParams) {
        DeletePassengerRequest request = new DeletePassengerRequest();
        request.setId(requestParams.getString(PARAM_PASSENGER_ID, ""));
        return Observable.just(request);
    }

    public RequestParams generateRequest(String passengerId, String idempotencyKey) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, passengerId);
        requestParams.putString(PARAM_IDEMPOTENCY, idempotencyKey);
        return requestParams;
    }
}