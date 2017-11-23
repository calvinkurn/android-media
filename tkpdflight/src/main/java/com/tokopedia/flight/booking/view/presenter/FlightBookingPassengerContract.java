package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.Date;
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

        List<FlightBookingLuggageMetaViewModel> getLuggageViewModels();

        List<FlightBookingMealMetaViewModel> getMealViewModels();

        void renderPassengerMeals(List<FlightBookingMealMetaViewModel> flightBookingMealRouteViewModels, List<FlightBookingMealMetaViewModel> selecteds);

        void renderPassengerLuggages(List<FlightBookingLuggageMetaViewModel> flightBookingLuggageRouteViewModels, List<FlightBookingLuggageMetaViewModel> selecteds);

        void hideBirthdayInputView();

        void showBirthdayInputView();

        void renderHeaderTitle(String headerTitle);

        void renderHeaderSubtitle(@StringRes int resId);

        String getPassengerName();

        void showPassengerNameEmptyError(@StringRes int resId);

        String getPassengerTitle();

        void showPassengerTitleEmptyError(@StringRes int resId);

        String getPassengerBirthDate();

        void showPassengerBirthdateEmptyError(int resId);

        void showPassengerChildBirthdateShouldMoreThan2Years(int resId);

        void showPassengerInfantBirthdateShouldNoMoreThan2Years(int resID);

        void navigateResultUpdatePassengerData(FlightBookingPassengerViewModel currentPassengerViewModel);

        void showBirthdatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        void renderBirthdate(String birthdateStr);

        void renderPassengerName(String passengerName);

        void renderPassengerTitle(String passengerTitle);

        void navigateToLuggagePicker(List<FlightBookingLuggageViewModel> luggages, FlightBookingLuggageMetaViewModel selected);

        void navigateToMealPicker(List<FlightBookingMealViewModel> viewModel, FlightBookingMealMetaViewModel selected);
    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();

        void onSaveButtonClicked();

        void onBirthdateClicked();

        void onBirthdateChange(int year, int month, int date);

        void onPassengerLuggageClick(FlightBookingLuggageMetaViewModel flightBookingLuggageMetaViewModel);

        void onLuggageDataChange(FlightBookingLuggageMetaViewModel flightBookingLuggageMetaViewModel);

        void onDeleteMeal(FlightBookingMealMetaViewModel viewModel);

        void onOptionMeal(FlightBookingMealMetaViewModel viewModel);

        void onMealDataChange(FlightBookingMealMetaViewModel flightBookingLuggageMetaViewModel);

    }
}
