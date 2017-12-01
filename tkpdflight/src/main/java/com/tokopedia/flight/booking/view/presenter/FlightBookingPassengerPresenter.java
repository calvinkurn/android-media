package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by alvarisi on 11/16/17.
 *         TODO :this class still
 */

public class FlightBookingPassengerPresenter extends BaseDaggerPresenter<FlightBookingPassengerContract.View> implements FlightBookingPassengerContract.Presenter {

    @Inject
    public FlightBookingPassengerPresenter() {

    }


    @Override
    public void onViewCreated() {
        getView().renderHeaderTitle(getView().getCurrentPassengerViewModel().getHeaderTitle());

        if (isAdultPassenger()) {
            getView().renderHeaderSubtitle(R.string.flight_booking_passenger_adult_subtitle);
            getView().hideBirthdayInputView();
            getView().renderSpinnerForAdult();
        } else {
            getView().renderSpinnerForChildAndInfant();
            getView().showBirthdayInputView();
            if (isChildPassenger()) {
                getView().renderHeaderSubtitle(R.string.flight_booking_passenger_child_subtitle);
            } else {
                getView().renderHeaderSubtitle(R.string.flight_booking_passenger_infant_subtitle);
            }
        }

        if (isAdultPassenger() || isChildPassenger()) {
            if (getView().getLuggageViewModels().size() > 0)
                getView().renderPassengerLuggages(getView().getLuggageViewModels(), getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels());
            if (getView().getMealViewModels().size() > 0)
                getView().renderPassengerMeals(getView().getMealViewModels(), getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerName() != null) {
            getView().renderPassengerName(getView().getCurrentPassengerViewModel().getPassengerName());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerTitle() != null) {
            getView().renderPassengerTitle(getView().getCurrentPassengerViewModel().getPassengerTitle());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerBirthdate() != null) {
            getView().renderBirthdate(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getCurrentPassengerViewModel().getPassengerBirthdate()));
        }
    }

    @Override
    public void onSaveButtonClicked() {
        if (validateFields()) {
            getView().getCurrentPassengerViewModel().setPassengerTitle(getView().getPassengerTitle());
            getView().getCurrentPassengerViewModel().setPassengerName(getView().getPassengerName());
            getView().getCurrentPassengerViewModel().setPassengerBirthdate(
                    FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, FlightDateUtil.DEFAULT_FORMAT, getView().getPassengerBirthDate())
            );
            // TODO : set passenger luggage and meals
            getView().navigateResultUpdatePassengerData(getView().getCurrentPassengerViewModel());
        }
    }

    @Override
    public void onBirthdateClicked() {

        Date maxDate, minDate, selectedDate;

        if (isChildPassenger()) {
            minDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, -12);
            maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, -2);
            selectedDate = maxDate;
        } else {
            minDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, -2);
            maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, -1);
            selectedDate = maxDate;
        }
        if (getView().getPassengerBirthDate().length() > 0) {
            selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getPassengerBirthDate());
        }
        getView().showBirthdatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onBirthdateChange(int year, int month, int date) {
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, date);
        Date newReturnDate = now.getTime();
        String birthdateStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
        getView().renderBirthdate(birthdateStr);
    }

    @Override
    public void onPassengerLuggageClick(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel) {
        FlightBookingAmenityMetaViewModel existingSelected = null;
        for (FlightBookingAmenityMetaViewModel selected : getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels()) {
            if (selected.getKey().equalsIgnoreCase(flightBookingLuggageMetaViewModel.getKey())) {
                existingSelected = selected;
            }
        }
        if (existingSelected == null) {
            existingSelected = new FlightBookingAmenityMetaViewModel();
            existingSelected.setKey(flightBookingLuggageMetaViewModel.getKey());
            existingSelected.setDescription(flightBookingLuggageMetaViewModel.getDescription());
            existingSelected.setAmenities(new ArrayList<FlightBookingAmenityViewModel>());
        }
        getView().navigateToLuggagePicker(flightBookingLuggageMetaViewModel.getAmenities(), existingSelected);
    }

    @Override
    public void onLuggageDataChange(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel) {
        List<FlightBookingAmenityMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels();
        int index = viewModels.indexOf(flightBookingLuggageMetaViewModel);
        if (index != -1) {
            viewModels.set(index, flightBookingLuggageMetaViewModel);
        } else {
            viewModels.add(flightBookingLuggageMetaViewModel);
        }

        getView().renderPassengerLuggages(getView().getLuggageViewModels(), viewModels);
    }

    @Override
    public void onMealDataChange(FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel) {
        List<FlightBookingAmenityMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels();
        int index = viewModels.indexOf(flightBookingAmenityMetaViewModel);
        if (index != -1) {
            viewModels.set(index, flightBookingAmenityMetaViewModel);
        } else {
            viewModels.add(flightBookingAmenityMetaViewModel);
        }

        getView().renderPassengerMeals(getView().getMealViewModels(), viewModels);
    }

    @Override
    public void onDeleteMeal(FlightBookingAmenityMetaViewModel viewModel) {
        List<FlightBookingAmenityMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels();
        int index = viewModels.indexOf(viewModel);
        if (index != -1) {
            viewModels.set(index, viewModel);
        } else {
            viewModels.add(viewModel);
        }

        getView().renderPassengerMeals(getView().getMealViewModels(), viewModels);
    }

    @Override
    public void onOptionMeal(FlightBookingAmenityMetaViewModel viewModel) {
        FlightBookingAmenityMetaViewModel existingSelected = null;
        for (FlightBookingAmenityMetaViewModel viewModel1 : getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels()) {
            if (viewModel1.getKey().equalsIgnoreCase(viewModel.getKey())) {
                existingSelected = viewModel1;
                break;
            }
        }

        if (existingSelected == null) {
            existingSelected = new FlightBookingAmenityMetaViewModel();
            existingSelected.setKey(viewModel.getKey());
            existingSelected.setAmenities(new ArrayList<FlightBookingAmenityViewModel>());
            existingSelected.setDescription(viewModel.getDescription());
        }
        getView().navigateToMealPicker(viewModel.getAmenities(), existingSelected);
    }

    private boolean validateFields() {
        boolean isValid = true;
        Date twoYearsAgo = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, -2);
        if (getView().getPassengerName().isEmpty() || getView().getPassengerName().length() == 0) {
            isValid = false;
            getView().showPassengerNameEmptyError(R.string.flight_booking_passenger_name_empty_error);
        } else if (getView().getPassengerTitle().isEmpty() || getView().getPassengerTitle().length() == 0) {
            isValid = false;
            getView().showPassengerTitleEmptyError(R.string.flight_bookingpassenger_title_error);
        } else if ((isChildPassenger() || isInfantPassenger()) && getView().getPassengerBirthDate().length() == 0) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if (isChildPassenger() &&
                FlightDateUtil.removeTime(FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getPassengerBirthDate()))
                        .compareTo(FlightDateUtil.removeTime(twoYearsAgo)) > 0) {
            isValid = false;
            getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years);
        } else if (isInfantPassenger() && FlightDateUtil.removeTime(FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getPassengerBirthDate()))
                .compareTo(FlightDateUtil.removeTime(twoYearsAgo)) < 0) {
            isValid = false;
            getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
        }
        return isValid;
    }

    private boolean isRoundTrip() {
        return getView().getReturnTripId() != null;
    }

    private boolean isAdultPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.ADULT;
    }

    private boolean isChildPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.CHILDREN;
    }

    private boolean isInfantPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.INFANT;
    }
}
