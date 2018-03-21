package com.tokopedia.flight.passenger.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerAttributesRequest;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerRequest;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 20/03/18.
 */

public class FlightPassengerUpdateDataUseCase extends UseCase<Boolean> {

    private static final String PARAM_PASSENGER_TITLE = "PARAM_PASSENGER_TITLE";
    private static final String PARAM_PASSENGER_FIRST_NAME = "PARAM_PASSENGER_FIRST_NAME";
    private static final String PARAM_PASSENGER_LAST_NAME = "PARAM_PASSENGER_LAST_NAME";
    private static final String PARAM_PASSENGER_BIRTHDATE = "PARAM_PASSENGER_BIRTHDATE";
    private static final String PARAM_IDEMPOTENCY = "PARAM_IDEMPOTENCY";
    private static final int DEFAULT_INT_VALUE = 0;
    private static final String DEFAULT_STRING_VALUE = "";

    private FlightRepository flightRepository;

    @Inject
    public FlightPassengerUpdateDataUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(final RequestParams requestParams) {
        return createRequest(requestParams)
                .flatMap(new Func1<UpdatePassengerRequest, Observable<Response<Object>>>() {
                    @Override
                    public Observable<Response<Object>> call(UpdatePassengerRequest request) {
                        return flightRepository.updatePassengerListData(request,
                                requestParams.getString(PARAM_IDEMPOTENCY, DEFAULT_STRING_VALUE)
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

    public Observable<UpdatePassengerRequest> createRequest(RequestParams requestParams) {
        UpdatePassengerAttributesRequest attributesRequest = new UpdatePassengerAttributesRequest();
        attributesRequest.setTitle(requestParams.getInt(PARAM_PASSENGER_TITLE, DEFAULT_INT_VALUE));
        attributesRequest.setFirstName(requestParams.getString(PARAM_PASSENGER_FIRST_NAME, DEFAULT_STRING_VALUE));
        attributesRequest.setLastName(requestParams.getString(PARAM_PASSENGER_LAST_NAME, DEFAULT_STRING_VALUE));
        attributesRequest.setDob(requestParams.getString(PARAM_PASSENGER_BIRTHDATE, DEFAULT_STRING_VALUE));
        attributesRequest.setPassportCountry("");
        attributesRequest.setPassportExpiry("");
        attributesRequest.setPassportNumber("");

        UpdatePassengerRequest updatePassengerRequest = new UpdatePassengerRequest(attributesRequest);
        return Observable.just(updatePassengerRequest);
    }

    public RequestParams generateRequestParams(int title,
                                               String firstName,
                                               String lastName,
                                               String birthdate,
                                               String idempotencyKey) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_PASSENGER_TITLE, title);
        requestParams.putString(PARAM_PASSENGER_FIRST_NAME, firstName);
        requestParams.putString(PARAM_PASSENGER_LAST_NAME, lastName);
        requestParams.putString(PARAM_PASSENGER_BIRTHDATE, birthdate);
        requestParams.putString(PARAM_IDEMPOTENCY, idempotencyKey);

        return requestParams;
    }
}
