package com.tokopedia.flight.passenger.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightPassengerInfoValidator;
import com.tokopedia.flight.passenger.domain.FlightPassengerUpdateDataUseCase;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.ADULT;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.CHILDREN;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.INFANT;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NONA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NYONYA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.TUAN;

/**
 * @author by furqan on 12/03/18.
 */

public class FlightPassengerUpdatePresenter extends BaseDaggerPresenter<FlightPassengerUpdateContract.View>
        implements FlightPassengerUpdateContract.Presenter {

    private static final int MINUS_TWELVE = -12;
    private static final int MINUS_TWO = -2;
    private static final int MINUS_ONE = -1;
    private static final int PLUS_ONE = 1;

    private FlightPassengerInfoValidator flightPassengerInfoValidator;
    private FlightPassengerUpdateDataUseCase flightPassengerUpdateDataUseCase;

    @Inject
    public FlightPassengerUpdatePresenter(FlightPassengerInfoValidator flightPassengerInfoValidator,
                                          FlightPassengerUpdateDataUseCase flightPassengerUpdateDataUseCase) {
        this.flightPassengerInfoValidator = flightPassengerInfoValidator;
        this.flightPassengerUpdateDataUseCase = flightPassengerUpdateDataUseCase;
    }

    @Override
    public void onViewCreated() {
        renderView();
    }

    @Override
    public void onSaveButtonClicked() {

        if (validateFields()) {
            updatePassengerData();
        }

    }

    @Override
    public void onBirthdateClicked() {

        Date maxDate, minDate = null, selectedDate;
        Date departureDate = FlightDateUtil.stringToDate(getView().getDepartureDate());

        if (isChildPassenger()) {
            minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, MINUS_TWELVE);
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, PLUS_ONE);
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, MINUS_TWO);
        } else if (isAdultPassenger()) {
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, MINUS_TWELVE);
        } else {
            minDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, MINUS_TWO);
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, PLUS_ONE);
            maxDate = FlightDateUtil.addTimeToSpesificDate(departureDate, Calendar.DATE, MINUS_ONE);
        }

        if (flightPassengerInfoValidator.validateBirthdateNotEmpty(getView().getPassengerBirthdate())) {
            selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT,
                    getView().getPassengerBirthdate());
        } else {
            selectedDate = maxDate;
        }

        if (minDate != null) {
            getView().showBirthdatePickerDialog(selectedDate, minDate, maxDate);
        } else {
            getView().showBirthdatePickerDialog(selectedDate, maxDate);
        }
    }

    @Override
    public void onBirthdateChanged(int year, int month, int date, Date minDate, Date maxDate) {
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, date);
        Date newReturnDate = now.getTime();

        //max Date + 1 hari, karena pengecekan pakai before
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, +1);

        if (flightPassengerInfoValidator.validateDateNotBetween(minDate, maxDate, newReturnDate)) {
            if (isChildPassenger()) {
                getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years);
            } else if (isInfantPassenger()) {
                getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
            }
        } else {
            String birthdateStr = FlightDateUtil.dateToString(newReturnDate,
                    FlightDateUtil.DEFAULT_VIEW_FORMAT);
            getView().renderPassengerBirthdate(birthdateStr);
        }
    }

    @Override
    public void onBirthdateChanged(int year, int month, int date, Date maxDate) {
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
            String birthdateStr = FlightDateUtil.dateToString(newReturnDate,
                    FlightDateUtil.DEFAULT_VIEW_FORMAT);
            getView().renderPassengerBirthdate(birthdateStr);
        }
    }

    private void updatePassengerData() {
        flightPassengerUpdateDataUseCase.execute(
                flightPassengerUpdateDataUseCase.generateRequestParams(
                        getView().getCurrentPassengerViewModel().getPassengerId(),
                        getTitleId(),
                        getView().getPassengerFirstName(),
                        getView().getPassengerLastName(),
                        FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT,
                                FlightDateUtil.FORMAT_DATE_API,
                                getView().getPassengerBirthdate()),
                        getView().getRequestId()
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
                        if (aBoolean) {
                            getView().onSuccessUpdatePassengerData();
                        }
                    }
                }
        );
    }

    private void renderView() {

        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getView().getCurrentPassengerViewModel();
        flightBookingPassengerViewModel.setPassengerTitle(flightBookingPassengerViewModel.getPassengerTitle());

        if (isAdultPassenger()) {
            getView().renderSpinnerForAdult();
            getView().renderPassengerType(
                    getView().getString(R.string.flightbooking_price_adult_label));
        } else {
            getView().renderSpinnerForChildAndInfant();

            if (isChildPassenger()) {
                getView().renderPassengerType(
                        getView().getString(R.string.flightbooking_price_child_label));
            } else if (isInfantPassenger()) {
                getView().renderPassengerType(
                        getView().getString(R.string.flightbooking_price_infant_label));
            }
        }

        if (flightBookingPassengerViewModel.getPassengerBirthdate() != null) {
            getView().renderPassengerBirthdate(
                    FlightDateUtil.dateToString(
                            FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT,
                                    flightBookingPassengerViewModel.getPassengerBirthdate()),
                            FlightDateUtil.DEFAULT_VIEW_FORMAT
                    )
            );
        }

        getView().renderSelectedTitle(flightBookingPassengerViewModel.getPassengerTitle());
        getView().renderPassengerName(flightBookingPassengerViewModel.getPassengerFirstName(),
                flightBookingPassengerViewModel.getPassengerLastName());

    }

    private boolean isAdultPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == ADULT;
    }

    private boolean isChildPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == CHILDREN;
    }

    private boolean isInfantPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == INFANT;
    }

    private int getTitleId() {
        switch (getView().getPassengerTitlePosition()) {
            case 0:
                return TUAN;
            case 1:
                if (isAdultPassenger()) {
                    return NYONYA;
                } else if (isChildPassenger() || isInfantPassenger()) {
                    return NONA;
                }
            case 2:
                return NONA;
            default:
                return TUAN;
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        Date twelveYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(getView().getDepartureDate()),
                Calendar.YEAR, MINUS_TWELVE
        );
        Date twoYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(getView().getDepartureDate()),
                Calendar.YEAR, MINUS_TWO
        );

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
                !flightPassengerInfoValidator.validateBirthdateNotEmpty(getView().getPassengerBirthdate())) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if ((isAdultPassenger()) && !flightPassengerInfoValidator.validateBirthdateNotEmpty(
                getView().getPassengerBirthdate())) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if (isAdultPassenger() && flightPassengerInfoValidator.validateBirthdateNotEmpty(
                getView().getPassengerBirthdate()) &&
                flightPassengerInfoValidator.validateDateMoreThan(getView().getPassengerBirthdate(), twelveYearsAgo)) {
            isValid = false;
            getView().showPassengerAdultBirthdateShouldMoreThan12Years(R.string.flight_booking_passenger_birthdate_adult_shoud_more_than_twelve_years);
        } else if (isChildPassenger() && flightPassengerInfoValidator.validateDateMoreThan(
                getView().getPassengerBirthdate(), twoYearsAgo)) {
            isValid = false;
            getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years);
        } else if (isInfantPassenger() && flightPassengerInfoValidator.validateDateLessThan(
                getView().getPassengerBirthdate(), twoYearsAgo)) {
            isValid = false;
            getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
        }

        return isValid;
    }

}
