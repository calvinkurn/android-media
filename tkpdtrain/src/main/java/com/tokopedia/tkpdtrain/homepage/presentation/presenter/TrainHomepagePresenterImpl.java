package com.tokopedia.tkpdtrain.homepage.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.common.util.TrainDateUtil;
import com.tokopedia.tkpdtrain.homepage.presentation.listener.TrainHomepageView;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainHomepageViewModel;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainPassengerViewModel;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * @author by Rizky on 21/02/18.
 */

public class TrainHomepagePresenterImpl extends BaseDaggerPresenter<TrainHomepageView> implements TrainHomepagePresenter {

    private final int DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL = 2;
    private final int MAX_BOOKING_DAYS_FROM_TODAY = 100;


    @Inject
    public TrainHomepagePresenterImpl() {
    }

    @Override
    public void singleTrip() {
        getView().getHomepageViewModel().setOneWay(true);
        getView().renderSingleTripView(getView().getHomepageViewModel());
    }

    @Override
    public void roundTrip() {
        getView().getHomepageViewModel().setOneWay(false);
        getView().renderRoundTripView(getView().getHomepageViewModel());
    }

    @Override
    public void onDepartureDateButtonClicked() {
        Date minDate = TrainDateUtil.getCurrentDate();
        Date maxDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);
        Date selectedDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getDepartureDate());
        getView().showDepartureDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onReturnDateButtonClicked() {
        Date selectedDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getReturnDate());
        Date minDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getDepartureDate());
        Date maxDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);
        getView().showReturnDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onDepartureDateChange(int year, int month, int dayOfMonth) {
        Calendar now = TrainDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);

        Date limitDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);
        Date newDepartureDate = now.getTime();

        if (newDepartureDate.after(limitDate)) {
            getView().showDepartureDateMax100Days(R.string.kai_homepage_departure_max_100_days_from_today_error);
        } else if (newDepartureDate.before(TrainDateUtil.getCurrentDate())) {
            getView().showDepartureDateShouldAtLeastToday(R.string.kai_homepage_departure_should_atleast_today_error);
        } else {
            String newDepartureDateStr = TrainDateUtil.dateToString(newDepartureDate, TrainDateUtil.DEFAULT_FORMAT);
            getView().getHomepageViewModel().setDepartureDate(newDepartureDateStr);
            String newDepartureDateFmtStr = TrainDateUtil.dateToString(newDepartureDate, TrainDateUtil.DEFAULT_VIEW_FORMAT);
            getView().getHomepageViewModel().setDepartureDateFmt(newDepartureDateFmtStr);
            if (!getView().getHomepageViewModel().isOneWay()) {
                Date currentReturnDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getReturnDate());
                if (currentReturnDate.compareTo(newDepartureDate) < 0) {
                    Date reAssignReturnDate = TrainDateUtil.addDate(newDepartureDate, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL);
                    getView().getHomepageViewModel().setReturnDate(TrainDateUtil.dateToString(reAssignReturnDate, TrainDateUtil.DEFAULT_FORMAT));
                    getView().getHomepageViewModel().setReturnDateFmt(TrainDateUtil.dateToString(reAssignReturnDate, TrainDateUtil.DEFAULT_VIEW_FORMAT));
                }
                getView().renderRoundTripView(getView().getHomepageViewModel());
            } else {
                Date reAssignReturnDate = TrainDateUtil.addDate(newDepartureDate, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL);
                getView().getHomepageViewModel().setReturnDate(TrainDateUtil.dateToString(reAssignReturnDate, TrainDateUtil.DEFAULT_FORMAT));
                getView().getHomepageViewModel().setReturnDateFmt(TrainDateUtil.dateToString(reAssignReturnDate, TrainDateUtil.DEFAULT_VIEW_FORMAT));
                getView().renderSingleTripView(getView().getHomepageViewModel());
            }
        }
    }

    @Override
    public void onReturnDateChange(int year, int month, int dayOfMonth) {
        Calendar now = TrainDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);

        Date newReturnDate = now.getTime();

        Date departureDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getDepartureDate());
        Date limitDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);

        if (newReturnDate.after(limitDate)) {
            getView().showReturnDateMax100Days(R.string.kai_homepage_return_max_100_days_from_today_error);
        } else if (newReturnDate.before(departureDate)) {
            getView().showReturnDateShouldGreaterOrEqual(R.string.kai_homepage_return_should_greater_equal_error);
        } else {
            String newReturnDateStr = TrainDateUtil.dateToString(newReturnDate, TrainDateUtil.DEFAULT_FORMAT);
            getView().getHomepageViewModel().setReturnDate(newReturnDateStr);
            String newReturnDateFmtStr = TrainDateUtil.dateToString(newReturnDate, TrainDateUtil.DEFAULT_VIEW_FORMAT);
            getView().getHomepageViewModel().setReturnDateFmt(newReturnDateFmtStr);
            getView().renderRoundTripView(getView().getHomepageViewModel());
        }
    }

    @Override
    public void initialize() {
        setupViewModel();
        singleTrip();
    }

    private void setupViewModel() {
        Date departureDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 1); // departure date = today + 1
        String departureDateString = TrainDateUtil.dateToString(departureDate, TrainDateUtil.DEFAULT_FORMAT);
        String departureDateFmtString = TrainDateUtil.dateToString(departureDate, TrainDateUtil.DEFAULT_VIEW_FORMAT);

        Date returnDate = TrainDateUtil.addTimeToSpesificDate(departureDate, Calendar.DATE, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL); // return date = departure date + 2
        String returnDateString = TrainDateUtil.dateToString(returnDate, TrainDateUtil.DEFAULT_FORMAT);
        String returnDateFmtString = TrainDateUtil.dateToString(returnDate, TrainDateUtil.DEFAULT_VIEW_FORMAT);

        TrainPassengerViewModel passData = new TrainPassengerViewModel.Builder()
                .setAdult(1)
                .build();
        String passengerFmt = buildPassengerTextFormatted(passData);

        TrainHomepageViewModel viewModel = new TrainHomepageViewModel.Builder()
                .setKAIPassengerViewModel(passData)
                .setIsOneWay(true)
                .setDepartureDate(departureDateString)
                .setReturnDate(returnDateString)
                .setDepartureDateFmt(departureDateFmtString)
                .setReturnDateFmt(returnDateFmtString)
                .setPassengerFmt(passengerFmt)
                .build();

        getView().setHomepageViewModel(viewModel);
    }


    @NonNull
    private String buildPassengerTextFormatted(TrainPassengerViewModel passData) {
        String passengerFmt = "";
        if (passData.getAdult() > 0) {
            passengerFmt = passData.getAdult() + " " + getView().getActivity().getString(R.string.kai_homepage_adult_passenger);
            if (passData.getInfant() > 0) {
                passengerFmt += ", " + passData.getInfant() + " " + getView().getActivity().getString(R.string.kai_homepage_adult_infant);
            }
        }
        return passengerFmt;
    }

}
