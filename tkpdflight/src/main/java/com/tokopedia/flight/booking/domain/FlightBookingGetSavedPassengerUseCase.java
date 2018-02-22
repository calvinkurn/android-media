package com.tokopedia.flight.booking.domain;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author by furqan on 22/02/18.
 */

public class FlightBookingGetSavedPassengerUseCase extends UseCase<List<FlightBookingPassengerViewModel>> {

    @Override
    public Observable<List<FlightBookingPassengerViewModel>> createObservable(RequestParams requestParams) {
        return null;
    }
}
