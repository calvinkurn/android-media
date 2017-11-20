package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageRouteViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealRouteViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.ArrayList;
import java.util.List;

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
            flightBookingGetSingleResultUseCase
                    .createObservable(flightBookingGetSingleResultUseCase.createRequestParam(false, getView().getDepartureId()))
                    .zipWith(flightBookingGetSingleResultUseCase.createObservable(
                            flightBookingGetSingleResultUseCase.createRequestParam(true, getView().getReturnTripId())),
                            new Func2<FlightSearchViewModel, FlightSearchViewModel, Boolean>() {
                                @Override
                                public Boolean call(FlightSearchViewModel departureViewModel, FlightSearchViewModel returnViewModel) {
                                    if (getView().getMealViewModels() != null && getView().getMealViewModels().size() > 0) {
                                        List<FlightBookingMealRouteViewModel> flightBookingMealRouteViewModels = new ArrayList<>();
                                        for (Route route : departureViewModel.getRouteList()) {
                                            List<FlightBookingMealViewModel> flightBookingMealViewModels = new ArrayList<>();
                                            for (FlightBookingMealViewModel mealViewModel : getView().getMealViewModels()) {
                                                if (mealViewModel.getId().contains(route.getDepartureAirport()) &&
                                                        mealViewModel.getId().contains(route.getArrivalAirport())) {
                                                    flightBookingMealViewModels.add(mealViewModel);
                                                }
                                            }

                                            if (flightBookingMealViewModels.size() > 0) {
                                                FlightBookingMealRouteViewModel flightBookingMealRouteViewModel = new FlightBookingMealRouteViewModel();
                                                flightBookingMealRouteViewModel.setRoute(route);
                                                flightBookingMealRouteViewModel.setMealViewModels(flightBookingMealViewModels);
                                                flightBookingMealRouteViewModels.add(flightBookingMealRouteViewModel);
                                            }
                                        }
                                        for (Route route : returnViewModel.getRouteList()) {
                                            List<FlightBookingMealViewModel> flightBookingMealViewModels = new ArrayList<>();
                                            for (FlightBookingMealViewModel mealViewModel : getView().getMealViewModels()) {
                                                if (mealViewModel.getId().contains(route.getDepartureAirport()) &&
                                                        mealViewModel.getId().contains(route.getArrivalAirport())) {
                                                    flightBookingMealViewModels.add(mealViewModel);
                                                }
                                                if (flightBookingMealRouteViewModels.size() > 0) {
                                                    FlightBookingMealRouteViewModel flightBookingMealRouteViewModel = new FlightBookingMealRouteViewModel();
                                                    flightBookingMealRouteViewModel.setRoute(route);
                                                    flightBookingMealRouteViewModel.setMealViewModels(flightBookingMealViewModels);
                                                    flightBookingMealRouteViewModels.add(flightBookingMealRouteViewModel);
                                                }
                                            }
                                        }
                                        if (flightBookingMealRouteViewModels.size() > 0) {
                                            getView().renderPassengerMeals(flightBookingMealRouteViewModels,
                                                    getView().getCurrentPassengerViewModel().getFlightBookingMealRouteViewModels());
                                        }
                                    }

                                    if (getView().getLuggageViewModels() != null && getView().getLuggageViewModels().size() > 0) {
                                        List<FlightBookingLuggageRouteViewModel> flightBookingLuggageRouteViewModels = new ArrayList<>();
                                        for (Route route : departureViewModel.getRouteList()) {
                                            List<FlightBookingLuggageViewModel> flightBookingLuggageViewModels = new ArrayList<>();
                                            for (FlightBookingLuggageViewModel flightBookingLuggageViewModel : getView().getLuggageViewModels()) {
                                                if (flightBookingLuggageViewModel.getId().contains(route.getArrivalAirport()) &&
                                                        flightBookingLuggageViewModel.getId().contains(route.getDepartureAirport())) {
                                                    flightBookingLuggageViewModels.add(flightBookingLuggageViewModel);
                                                }
                                            }
                                            if (flightBookingLuggageViewModels.size() > 0) {
                                                FlightBookingLuggageRouteViewModel flightBookingLuggageRouteViewModel = new FlightBookingLuggageRouteViewModel();
                                                flightBookingLuggageRouteViewModel.setRoute(route);
                                                flightBookingLuggageRouteViewModel.setLuggage(flightBookingLuggageViewModels);
                                                flightBookingLuggageRouteViewModels.add(flightBookingLuggageRouteViewModel);
                                            }
                                        }
                                        for (Route route : returnViewModel.getRouteList()) {
                                            List<FlightBookingLuggageViewModel> flightBookingLuggageViewModels = new ArrayList<>();
                                            for (FlightBookingLuggageViewModel flightBookingLuggageViewModel : getView().getLuggageViewModels()) {
                                                if (flightBookingLuggageViewModel.getId().contains(route.getArrivalAirport()) &&
                                                        flightBookingLuggageViewModel.getId().contains(route.getDepartureAirport())) {
                                                    flightBookingLuggageViewModels.add(flightBookingLuggageViewModel);
                                                }
                                            }
                                            if (flightBookingLuggageViewModels.size() > 0) {
                                                FlightBookingLuggageRouteViewModel flightBookingLuggageRouteViewModel = new FlightBookingLuggageRouteViewModel();
                                                flightBookingLuggageRouteViewModel.setRoute(route);
                                                flightBookingLuggageRouteViewModel.setLuggage(flightBookingLuggageViewModels);
                                                flightBookingLuggageRouteViewModels.add(flightBookingLuggageRouteViewModel);
                                            }
                                        }

                                        if (flightBookingLuggageRouteViewModels.size() > 0) {
                                            getView().renderPassengerLuggages(flightBookingLuggageRouteViewModels,
                                                    getView().getCurrentPassengerViewModel().getFlightBookingLuggageRouteViewModels());
                                        }
                                    }
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
