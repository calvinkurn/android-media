package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageRouteViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealRouteViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.List;

/**
 * Created by alvarisi on 11/16/17.
 */

public interface FlightBookingPassengerContract {

    interface View extends CustomerView {

        FlightBookingPassengerViewModel getCurrentPassengerViewModel();

        void renderSpinnerForAdult();

        void renderSpinnerForChildAndInfant();

        String getReturnTripId();

        String getDepartureId();

        List<FlightBookingLuggageViewModel> getLuggageViewModels();

        List<FlightBookingMealViewModel> getMealViewModels();

        void renderPassengerMeals(List<FlightBookingMealRouteViewModel> flightBookingMealRouteViewModels, List<FlightBookingMealRouteViewModel> selecteds);

        void renderPassengerLuggages(List<FlightBookingLuggageRouteViewModel> flightBookingLuggageRouteViewModels, List<FlightBookingLuggageRouteViewModel> selecteds);
    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();
    }
}
