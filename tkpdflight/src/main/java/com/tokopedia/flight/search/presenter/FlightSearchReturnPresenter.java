package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.search.view.FlightSearchReturnView;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by alvarisi on 1/10/18.
 */

public class FlightSearchReturnPresenter extends BaseDaggerPresenter<FlightSearchReturnView> {
    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;

    @Inject
    public FlightSearchReturnPresenter(FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase) {
        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
    }

    public void onFlightSearchSelected(String departureDate,
                                       String returnDate,
                                       String selectedFlightDeparture,
                                       final FlightSearchViewModel returnFlightSearchViewModel) {
        if (departureDate.equalsIgnoreCase(returnDate)) {
            flightBookingGetSingleResultUseCase.execute(flightBookingGetSingleResultUseCase.createRequestParam(false, selectedFlightDeparture),
                    new Subscriber<FlightSearchViewModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (isViewAttached()) {

                            }
                        }

                        @Override
                        public void onNext(FlightSearchViewModel departureFlightSearchViewModel) {
                            if (departureFlightSearchViewModel.getArrivalTimeInt() < returnFlightSearchViewModel.getDepartureTimeInt()) {
                                getView().navigateToCart(returnFlightSearchViewModel);
                            } else {
                                getView().showReturnTimeShouldGreaterThanArrivalDeparture();
                            }
                        }
                    });
        } else {
            getView().navigateToCart(returnFlightSearchViewModel);
        }
    }
}
