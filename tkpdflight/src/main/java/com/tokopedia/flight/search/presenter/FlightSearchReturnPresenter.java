package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.CommonUtils;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.view.FlightSearchReturnView;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.Date;

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
//                            if (departureFlightSearchViewModel.getArrivalTimeInt() < returnFlightSearchViewModel.getDepartureTimeInt()) {
                        if (isValidReturnJourney(departureFlightSearchViewModel, returnFlightSearchViewModel)) {
                            getView().navigateToCart(returnFlightSearchViewModel);
                        } else {
                            getView().showReturnTimeShouldGreaterThanArrivalDeparture();
                        }
                    }
                });
    }

    private boolean isValidReturnJourney(FlightSearchViewModel departureViewModel, FlightSearchViewModel returnViewModel) {
        if (departureViewModel.getRouteList() != null && returnViewModel.getRouteList() != null) {
            if (departureViewModel.getRouteList().size() > 0 && returnViewModel.getRouteList().size() > 0) {
                Route lastDepartureRoute = departureViewModel.getRouteList().get(departureViewModel.getRouteList().size() - 1);
                Route firstReturnRoute = returnViewModel.getRouteList().get(0);
                Date departureArrivalTime = FlightDateUtil.stringToDate(FlightDateUtil.FORMAT_DATE_API, lastDepartureRoute.getArrivalTimestamp());
                Date returnDepartureTime = FlightDateUtil.stringToDate(FlightDateUtil.FORMAT_DATE_API, firstReturnRoute.getDepartureTimestamp());
                long different = returnDepartureTime.getTime() - departureArrivalTime.getTime();
                if (different >= 0) {
                    long hours = different / 3600000;
                    CommonUtils.dumper("diff" + hours);
                    return hours >= 6;
                } else {
                    return false;
                }

            }
        }
        return true;
    }
}
