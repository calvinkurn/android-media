package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

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

    @Inject
    public FlightPassengerUpdatePresenter() {
    }

    @Override
    public void onViewCreated() {
        renderView();
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

        if (getView().getCurrentPassengerViewModel().getPassengerBirthdate() != null &&
                getView().getCurrentPassengerViewModel().getPassengerBirthdate().length() > 0) {
            selectedDate = FlightDateUtil.stringToDate(
                    getView().getCurrentPassengerViewModel().getPassengerBirthdate());
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
    public void onBirthdateChanged(int year, int month, int dayOfMonth, Date minDate, Date maxDate) {

    }

    @Override
    public void onBirthdateChanged(int year, int month, int dayOfMonth, Date maxDate) {

    }

    private void renderView() {

        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getView().getCurrentPassengerViewModel();
        flightBookingPassengerViewModel.setPassengerTitle(
                getSalutationById(flightBookingPassengerViewModel.getPassengerTitleId()));

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

    private String getSalutationById(int salutationId) {
        switch (salutationId) {
            case TUAN:
                return getView().getString(R.string.mister);
            case NYONYA:
                return getView().getString(R.string.misiz);
            case NONA:
                return getView().getString(R.string.miss);
        }

        return "";
    }

}
