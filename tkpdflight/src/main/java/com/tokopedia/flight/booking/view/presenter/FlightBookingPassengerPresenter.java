package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import javax.inject.Inject;

import rx.functions.Func2;

/**
 * @author by alvarisi on 11/16/17.
 */

public class FlightBookingPassengerPresenter extends BaseDaggerPresenter<FlightBookingPassengerContract.View> implements FlightBookingPassengerContract.Presenter {

    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;

    @Inject
    public FlightBookingPassengerPresenter(FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase) {
        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
    }

    @Override
    public void onViewCreated() {
        if (isAdultPassenger()) {
            getView().renderSpinnerForAdult();
        } else {
            getView().renderSpinnerForChildAndInfant();
        }

        if (isRoundTrip()) {
            flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(false, getView().getDepartureId()))
                    .zipWith(flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(true, getView().getReturnTripId())),
                            new Func2<FlightSearchViewModel, FlightSearchViewModel, Boolean>() {
                                @Override
                                public Boolean call(FlightSearchViewModel flightSearchViewModel, FlightSearchViewModel flightSearchViewModel2) {

                                    for ()
                                        return true;
                                }
                            });
        }

    }

    private boolean isRoundTrip() {
        return getView().getReturnTripId() != null;
    }

    private boolean isAdultPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.ADULT;
    }
}
