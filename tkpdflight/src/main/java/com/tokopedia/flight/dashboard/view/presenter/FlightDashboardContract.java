package com.tokopedia.flight.dashboard.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;

import java.util.Date;

/**
 * Created by alvarisi on 10/30/17.
 */

public interface FlightDashboardContract {
    interface View extends CustomerView {

        void renderSingleTripView();

        void renderRoundTripView();

        FlightDashboardViewModel getCurrentDashboardViewModel();

        void showDepartureDatePickerDialog(Date selectedDate, Date minDate);

        void setDashBoardViewModel(FlightDashboardViewModel viewModel);

        void showReturnDatePickerDialog(Date selectedDate, Date minDate);

        CharSequence getString(@StringRes int resID);

        void showDepartureEmptyErrorMessage(@StringRes int resId);

        void showArrivalEmptyErrorMessage(@StringRes int resId);

        void showArrivalAndDestinationAreSameError(@StringRes int resID);

        void showDepartureDateShouldAtLeastToday(@StringRes int resID);

        void showArrivalDateShouldGreaterOrEqual(@StringRes int resId);

        void showPassengerAtLeastOneAdult(@StringRes int resId);

        void showFlightClassPassengerIsEmpty(@StringRes int resId);

        void navigateToSearchPage(FlightDashboardViewModel currentDashboardViewModel);

    }

    interface Presenter extends CustomerPresenter<View> {

        void onSingleTripChecked();

        void onRoundTripChecked();

        void initialize();

        void onReverseAirportButtonClicked();

        void onDepartureDateButtonClicked();

        void onDepartureDateChange(int year, int month, int dayOfMonth);

        void onReturnDateButtonClicked();

        void onReturnDateChange(int year, int month, int dayOfMonth);

        void onFlightClassesChange(FlightClassViewModel viewModel);

        void onFlightPassengerChange(FlightPassengerViewModel passengerViewModel);

        void onDepartureAirportChange(FlightAirportDB departureAirport);

        void onArrivalAirportChange(FlightAirportDB arrivalAirport);

        void onSearchTicketButtonClicked();

        void onDestroyView();
    }
}
