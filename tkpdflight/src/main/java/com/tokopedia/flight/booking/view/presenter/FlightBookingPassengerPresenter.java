package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightPassengerInfoValidator;
import com.tokopedia.flight.common.util.FlightPassengerTitleType;
import com.tokopedia.flight.passenger.domain.FlightBookingUpdateSelectedPassengerUseCase;
import com.tokopedia.flight.passenger.view.fragment.FlightBookingListPassengerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by alvarisi on 11/16/17.
 */

public class FlightBookingPassengerPresenter extends BaseDaggerPresenter<FlightBookingPassengerContract.View> implements FlightBookingPassengerContract.Presenter {

    private final int MINUS_TWO_YEARS = -2;
    private final int MINUS_TWELVE_YEARS = -12;

    private FlightBookingUpdateSelectedPassengerUseCase flightBookingUpdateSelectedPassengerUseCase;
    private FlightPassengerInfoValidator flightPassengerInfoValidator;

    @Inject
    public FlightBookingPassengerPresenter(FlightBookingUpdateSelectedPassengerUseCase flightBookingUpdateSelectedPassengerUseCase,
                                           FlightPassengerInfoValidator flightPassengerInfoValidator) {
        this.flightBookingUpdateSelectedPassengerUseCase = flightBookingUpdateSelectedPassengerUseCase;
        this.flightPassengerInfoValidator = flightPassengerInfoValidator;
    }


    @Override
    public void onViewCreated() {
        getView().renderHeaderTitle(getView().getCurrentPassengerViewModel().getHeaderTitle());

        if (isAdultPassenger()) {
            getView().renderHeaderSubtitle(R.string.flight_booking_passenger_adult_subtitle);
            getView().renderSpinnerForAdult();
            if (getView().isMandatoryDoB()) {
                getView().showBirthdayInputView();
            } else {
                getView().hideBirthdayInputView();
            }
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

        if (getView().getCurrentPassengerViewModel().getPassengerFirstName() != null) {
            getView().renderPassengerName(getView().getCurrentPassengerViewModel().getPassengerFirstName(),
                    getView().getCurrentPassengerViewModel().getPassengerLastName());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerTitle() != null) {
            getView().renderPassengerTitle(getView().getCurrentPassengerViewModel().getPassengerTitle());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerBirthdate() != null) {
            getView().renderBirthdate(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getCurrentPassengerViewModel().getPassengerBirthdate()));
        }

        if (getView().getCurrentPassengerViewModel().getPassengerId() != null &&
                !getView().getCurrentPassengerViewModel().getPassengerId().equals("")) {
            getView().renderSelectedList(String.format("%s %s",
                    getView().getCurrentPassengerViewModel().getPassengerFirstName(),
                    getView().getCurrentPassengerViewModel().getPassengerLastName()
            ));
        }
    }

    @Override
    public void onSaveButtonClicked() {
        getView().hideKeyboard();
        if (validateFields(getView().getDepartureDateString())) {
            getView().getCurrentPassengerViewModel().setPassengerTitle(getView().getPassengerTitle());
            getView().getCurrentPassengerViewModel().setPassengerTitleId(getPassengerTitleId());
            getView().getCurrentPassengerViewModel().setPassengerFirstName(getView().getPassengerFirstName());
            getView().getCurrentPassengerViewModel().setPassengerBirthdate(
                    FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, FlightDateUtil.DEFAULT_FORMAT, getView().getPassengerBirthDate())
            );
            getView().getCurrentPassengerViewModel().setPassengerLastName(getView().getPassengerLastName());
            getView().navigateResultUpdatePassengerData(getView().getCurrentPassengerViewModel());
        }
    }

    @Override
    public void onBirthdateClicked() {

        Date maxDate, minDate = null, selectedDate;
        Date departureDate = FlightDateUtil.stringToDate(getView().getDepartureDateString());

        if (isChildPassenger()) {
            // minDate = 12 tahun + 1 hari
            minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -12);
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, +1);
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -2);
            selectedDate = maxDate;
        } else if (isAdultPassenger()) {
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -12);
            selectedDate = maxDate;
        } else {
            // minDate = 2 tahun + 1 hari
            minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -2);
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, +1);
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.DATE, -1);
            selectedDate = maxDate;
        }
        if (flightPassengerInfoValidator.validateBirthdateNotEmpty(getView().getPassengerBirthDate())) {
            selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getPassengerBirthDate());
        }

        if (minDate != null) {
            getView().showBirthdatePickerDialog(selectedDate, minDate, maxDate);
        } else {
            getView().showBirthdatePickerDialog(selectedDate, maxDate);
        }
    }

    @Override
    public void onBirthdateChange(int year, int month, int date, Date minDate, Date maxDate) {
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, date);
        Date newReturnDate = now.getTime();

        //max Date + 1 hari, karena pengecekan pakai before
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, +1);

        if (flightPassengerInfoValidator.validateDateNotBetween(minDate, maxDate, newReturnDate)) {
            if (isChildPassenger()) {
                getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_between_twelve_to_two_years);
            } else if (isInfantPassenger()) {
                getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
            }
        } else {
            String birthdateStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
            getView().renderBirthdate(birthdateStr);
        }

        getView().hideKeyboard();
    }

    @Override
    public void onBirthdateChange(int year, int month, int date, Date maxDate) {
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, date);
        Date newReturnDate = now.getTime();

        //max Date + 1 hari, karena pengecekan pakai before
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, +1);

        if (flightPassengerInfoValidator.validateDateExceedMaxDate(maxDate, newReturnDate)) {
            getView().showPassengerAdultBirthdateShouldMoreThan12Years(R.string.flight_booking_passenger_birthdate_adult_shoud_more_than_twelve_years);
        } else {
            String birthdateStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
            getView().renderBirthdate(birthdateStr);
        }
        getView().hideKeyboard();
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
            existingSelected.setJourneyId(flightBookingLuggageMetaViewModel.getJourneyId());
            existingSelected.setArrivalId(flightBookingLuggageMetaViewModel.getArrivalId());
            existingSelected.setDepartureId(flightBookingLuggageMetaViewModel.getDepartureId());
            existingSelected.setDescription(flightBookingLuggageMetaViewModel.getDescription());
            existingSelected.setAmenities(new ArrayList<FlightBookingAmenityViewModel>());
        }
        getView().navigateToLuggagePicker(flightBookingLuggageMetaViewModel.getAmenities(), existingSelected);
    }

    @Override
    public void onLuggageDataChange(FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel) {
        List<FlightBookingAmenityMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels();
        int index = viewModels.indexOf(flightBookingLuggageMetaViewModel);

        if (flightBookingLuggageMetaViewModel.getAmenities().size() != 0) {
            if (index != -1) {
                viewModels.set(index, flightBookingLuggageMetaViewModel);
            } else {
                viewModels.add(flightBookingLuggageMetaViewModel);
            }
        } else {
            if (index != -1) {
                viewModels.remove(index);
            }
        }

        getView().renderPassengerLuggages(getView().getLuggageViewModels(), viewModels);
    }

    @Override
    public void onMealDataChange(FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel) {
        List<FlightBookingAmenityMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels();
        int index = viewModels.indexOf(flightBookingAmenityMetaViewModel);

        if (flightBookingAmenityMetaViewModel.getAmenities().size() != 0) {
            if (index != -1) {
                viewModels.set(index, flightBookingAmenityMetaViewModel);
            } else {
                viewModels.add(flightBookingAmenityMetaViewModel);
            }
        } else {
            if (index != -1) {
                viewModels.remove(index);
            }
        }

        getView().renderPassengerMeals(getView().getMealViewModels(), viewModels);
    }

    @Override
    public void onSavedPassengerClicked() {
        FlightBookingPassengerViewModel existingSelected = getView().getCurrentPassengerViewModel();

        if (existingSelected == null) {
            existingSelected = new FlightBookingPassengerViewModel();
        }

        getView().navigateToSavedPassengerPicker(existingSelected);
    }

    @Override
    public void onUnselectPassengerList(String passengerId) {
        flightBookingUpdateSelectedPassengerUseCase.execute(
                flightBookingUpdateSelectedPassengerUseCase.createRequestParams(
                        passengerId,
                        FlightBookingListPassengerFragment.IS_NOT_SELECTING
                ),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getView().canGoBack();
                    }
                }
        );
    }

    @Override
    public void onNewPassengerChoosed() {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getView().getCurrentPassengerViewModel();
        flightBookingPassengerViewModel.setPassengerId("");
        flightBookingPassengerViewModel.setPassengerFirstName("");
        flightBookingPassengerViewModel.setPassengerLastName("");
        flightBookingPassengerViewModel.setPassengerBirthdate("");
        flightBookingPassengerViewModel.setPassengerTitle("");

        getView().renderSelectedList(getView().getString(R.string.flight_booking_passenger_saved_secondary_hint));
        getView().renderPassengerName("", "");
        getView().renderBirthdate("");
    }

    @Override
    public void onChangeFromSavedPassenger(FlightBookingPassengerViewModel selectedPassenger) {
        getView().renderSelectedList(String.format("%s %s",
                selectedPassenger.getPassengerFirstName(),
                selectedPassenger.getPassengerLastName()));

        FlightBookingPassengerViewModel currentPassengerViewModel = getView().getCurrentPassengerViewModel();
        currentPassengerViewModel.setPassengerId(selectedPassenger.getPassengerId());
        currentPassengerViewModel.setPassengerFirstName(selectedPassenger.getPassengerFirstName());
        currentPassengerViewModel.setPassengerLastName(selectedPassenger.getPassengerLastName());
        currentPassengerViewModel.setPassengerTitle(selectedPassenger.getPassengerTitle());
        currentPassengerViewModel.setPassengerTitleId(selectedPassenger.getPassengerTitleId());
        if (flightPassengerInfoValidator.validateBirthdateNotEmpty(selectedPassenger.getPassengerBirthdate()) &&
                (isChildPassenger() || isInfantPassenger() || getView().isMandatoryDoB())) {
            currentPassengerViewModel.setPassengerBirthdate(selectedPassenger.getPassengerBirthdate());
        }

        getView().setCurrentPassengerViewModel(currentPassengerViewModel);
        onViewCreated();
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
            existingSelected.setJourneyId(viewModel.getJourneyId());
            existingSelected.setArrivalId(viewModel.getArrivalId());
            existingSelected.setDepartureId(viewModel.getDepartureId());
            existingSelected.setAmenities(new ArrayList<FlightBookingAmenityViewModel>());
            existingSelected.setDescription(viewModel.getDescription());
        }
        getView().navigateToMealPicker(viewModel.getAmenities(), existingSelected);
    }

    private boolean validateFields(String departureDateString) {
        boolean isValid = true;
        Date twelveYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(departureDateString), Calendar.YEAR, MINUS_TWELVE_YEARS);
        Date twoYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(departureDateString), Calendar.YEAR, MINUS_TWO_YEARS);
        if (flightPassengerInfoValidator.validateNameIsEmpty(getView().getPassengerFirstName())) {
            isValid = false;
            getView().showPassengerNameEmptyError(R.string.flight_booking_passenger_first_name_empty_error);
        } else if (flightPassengerInfoValidator.validateNameIsNotAlphabetAndSpaceOnly(getView().getPassengerFirstName())) {
            isValid = false;
            getView().showPassengerFirstNameShouldAlphabetAndSpaceOnlyError(R.string.flight_booking_passenger_first_name_alpha_space_error);
        } else if (flightPassengerInfoValidator.validateNameIsMoreThanMaxLength(
                getView().getPassengerFirstName(), getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerFirstNameShouldNoMoreThanMaxError(R.string.flight_booking_passenger_first_last_name_max_error);
        } else if (flightPassengerInfoValidator.validateNameIsEmpty(getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerLastNameShouldSameWithFirstNameError(R.string.flight_booking_passenger_last_name_should_same_error);
        } else if (flightPassengerInfoValidator.validateLastNameIsLessThanMinLength(getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerLastNameEmptyError(R.string.flight_booking_passenger_last_name_empty_error);
        } else if (flightPassengerInfoValidator.validateLastNameIsNotSingleWord(getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerLastNameShouldOneWordError(R.string.flight_booking_passenger_last_name_single_word_error);
        } else if (flightPassengerInfoValidator.validateNameIsNotAlphabetAndSpaceOnly(getView().getPassengerLastName())) {
            isValid = false;
            getView().showPassengerLastNameShouldAlphabetAndSpaceOnlyError(R.string.flight_booking_passenger_last_name_alpha_space_error);
        } else if (flightPassengerInfoValidator.validateTitleIsEmpty(getView().getPassengerTitle())) {
            isValid = false;
            getView().showPassengerTitleEmptyError(R.string.flight_bookingpassenger_title_error);
        } else if ((isChildPassenger() || isInfantPassenger()) &&
                !flightPassengerInfoValidator.validateBirthdateNotEmpty(getView().getPassengerBirthDate())) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if ((isAdultPassenger()) && !flightPassengerInfoValidator.validateBirthdateNotEmpty(
                getView().getPassengerBirthDate()) && getView().isMandatoryDoB()) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if (isAdultPassenger() && flightPassengerInfoValidator.validateBirthdateNotEmpty(
                getView().getPassengerBirthDate()) && getView().isMandatoryDoB() &&
                flightPassengerInfoValidator.validateDateMoreThan(getView().getPassengerBirthDate(), twelveYearsAgo)) {
            isValid = false;
            getView().showPassengerAdultBirthdateShouldMoreThan12Years(R.string.flight_booking_passenger_birthdate_adult_shoud_more_than_twelve_years);
        } else if (isChildPassenger() && flightPassengerInfoValidator.validateDateMoreThan(
                getView().getPassengerBirthDate(), twoYearsAgo)) {
            isValid = false;
            getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years);
        } else if (isInfantPassenger() && flightPassengerInfoValidator.validateDateLessThan(
                getView().getPassengerBirthDate(), twoYearsAgo)) {
            isValid = false;
            getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
        }
        return isValid;
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

    private int getPassengerTitleId() {
        switch (getView().getTitleSpinnerPosition()) {
            case 0:
                return FlightPassengerTitleType.TUAN;
            case 1:
                if (isChildPassenger() || isInfantPassenger()) {
                    return FlightPassengerTitleType.NONA;
                } else {
                    return FlightPassengerTitleType.NYONYA;
                }
            case 2:
                return FlightPassengerTitleType.NONA;
            default:
                return 0;
        }
    }
}
