package com.tokopedia.flight.cancellation.domain;

import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.domain.mapper.FlightCancellationViewModelMapper;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightCancellationGetCancelablePassengerUseCase extends UseCase<List<FlightCancellationViewModel>> {

    private static final String PARAM_INVOICE_ID = "invoice_id";

    private FlightRepository flightRepository;
    private FlightCancellationViewModelMapper flightCancellationViewModelMapper;

    @Inject
    public FlightCancellationGetCancelablePassengerUseCase(FlightRepository flightRepository,
                                                           FlightCancellationViewModelMapper flightCancellationViewModelMapper) {
        this.flightRepository = flightRepository;
        this.flightCancellationViewModelMapper = flightCancellationViewModelMapper;
    }

    @Override
    public Observable<List<FlightCancellationViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getCancelablePassenger(
                requestParams.getString(PARAM_INVOICE_ID, ""))
                .flatMap(new Func1<List<Passenger>, Observable<List<FlightCancellationViewModel>>>() {
                    @Override
                    public Observable<List<FlightCancellationViewModel>> call(List<Passenger> passengers) {
                        return Observable.just(flightCancellationViewModelMapper.transform(passengers));
                    }
                });
    }

    public RequestParams generateRequestParams(String invoiceId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_INVOICE_ID, invoiceId);
        return requestParams;
    }

}
