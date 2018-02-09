package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
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

        List<FlightBookingAmenityMetaViewModel> getLuggageViewModels();

        List<FlightBookingAmenityMetaViewModel> getMealViewModels();

        void renderPassengerMeals(List<FlightBookingAmenityMetaViewModel> flightBookingMealRouteViewModels, List<FlightBookingAmenityMetaViewModel> selecteds);

        void renderPassengerLuggages(List<FlightBookingAmenityMetaViewModel> flightBookingLuggageRouteViewModels, List<FlightBookingAmenityMetaViewModel> selecteds);

        void hideBirthdayInputView();

        void showBirthdayInputView();

        void renderHeaderTitle(String headerTitle);

        void renderHeaderSubtitle(@StringRes int resId);

        String getPassengerFirstName();

        void showPassengerNameEmptyError(@StringRes int resId);

        String getPassengerTitle();

        void showPassengerTitleEmptyError(@StringRes int resId);

        String getPassengerBirthDate();

        void showPassengerBirthdateEmptyError(int resId);

        void showPassengerChildBirthdateShouldMoreThan2Years(int resId);

        void showPassengerInfantBirthdateShouldNoMoreThan2Years(int resID);

        void showPassengerAdultBirthdateShouldMoreThan12Years(int resID);

        void navigateResultUpdatePassengerData(FlightBookingPassengerViewModel currentPassengerViewModel);

        void showBirthdatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        void showBirthdatePickerDialog(Date selectedDate, Date maxDate);

        void renderBirthdate(String birthdateStr);

        void renderPassengerName(String passengerName, String passengerLastName);

        void renderPassengerTitle(String passengerTitle);

        void navigateToLuggagePicker(List<FlightBookingAmenityViewModel> luggages, FlightBookingAmenityMetaViewModel selected);

        void navigateToMealPicker(List<FlightBookingAmenityViewModel> viewModel, FlightBookingAmenityMetaViewModel selected);

        int getPassengerTitleId();

        void showPassengerFirstNameShouldNoMoreThanMaxError(@StringRes int resId);

        void showPassengerLastNameEmptyError(@StringRes int resId);

        void showPassengerLastNameShouldNoMoreThanMaxError(@StringRes int resId);

        String getPassengerLastName();

        void showPassengerLastNameShouldOneWordError(@StringRes int resId);

        void showPassengerFirstNameShouldAlphabetAndSpaceOnlyError(@StringRes int resId);

        void showPassengerLastNameShouldAlphabetAndSpaceOnlyError(@StringRes int resId);

        void hideKeyboard();

        boolean isAirAsiaAirline();

        String getDepartureDateString();

        void showPassengerLastNameShouldSameWithFirstNameError(int resId);
    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();

        void onSaveButtonClicked();

        void onBirthdateClicked();

        void onBirthdateChange(int year, int month, int date, Date minDate, Date maxDate);

        void onBirthdateChange(int year, int month, int date, Date maxDate);

        void onPassengerLuggageClick(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel);

        void onLuggageDataChange(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel);

        void onDeleteMeal(FlightBookingAmenityMetaViewModel viewModel);

        void onOptionMeal(FlightBookingAmenityMetaViewModel viewModel);

        void onMealDataChange(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel);

    }
}
