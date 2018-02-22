package com.tokopedia.tkpdtrain.homepage.presentation.presenter;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.common.util.KAIDateUtil;
import com.tokopedia.tkpdtrain.homepage.presentation.listener.ITrainHomepageView;
import com.tokopedia.tkpdtrain.homepage.presentation.model.KAIHomepageViewModel;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rizky on 21/02/18.
 */

public class TrainHomepagePresenter implements ITrainHomepagePresenter {

    private final int DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL = 2;
    private final int MAX_BOOKING_DAYS_FROM_TODAY = 100;

    private ITrainHomepageView trainHomepageView;
    private KAIHomepageViewModel kaiHomepageViewModel;

    public TrainHomepagePresenter(KAIHomepageViewModel kaiHomepageViewModel) {
        this.kaiHomepageViewModel = kaiHomepageViewModel;
    }

    @Override
    public void takeView(ITrainHomepageView trainHomepageView) {
        this.trainHomepageView = trainHomepageView;
    }

    @Override
    public void singleTrip() {
        kaiHomepageViewModel.setOneWay(true);
        trainHomepageView.renderSingleTripView(kaiHomepageViewModel);
    }

    @Override
    public void roundTrip() {
        kaiHomepageViewModel.setOneWay(false);
        trainHomepageView.renderRoundTripView(kaiHomepageViewModel);
    }

    @Override
    public void onDepartureDateButtonClicked() {
        Date minDate = KAIDateUtil.getCurrentDate();
        Date maxDate = KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);
        Date selectedDate = KAIDateUtil.stringToDate(kaiHomepageViewModel.getDepartureDate());
        trainHomepageView.showDepartureDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onReturnDateButtonClicked() {
        Date selectedDate = KAIDateUtil.stringToDate(kaiHomepageViewModel.getReturnDate());
        Date minDate = KAIDateUtil.stringToDate(kaiHomepageViewModel.getDepartureDate());
        Date maxDate = KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);
        trainHomepageView.showReturnDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onDepartureDateChange(int year, int month, int dayOfMonth) {
        Calendar now = KAIDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);

        Date limitDate = KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);
        Date newDepartureDate = now.getTime();

        if (newDepartureDate.after(limitDate)) {
            trainHomepageView.showDepartureDateMax100Days(R.string.kai_homepage_departure_max_100_days_from_today_error);
        } else if (newDepartureDate.before(KAIDateUtil.getCurrentDate())) {
            trainHomepageView.showDepartureDateShouldAtLeastToday(R.string.kai_homepage_departure_should_atleast_today_error);
        } else {
            String newDepartureDateStr = KAIDateUtil.dateToString(newDepartureDate, KAIDateUtil.DEFAULT_FORMAT);
            kaiHomepageViewModel.setDepartureDate(newDepartureDateStr);
            String newDepartureDateFmtStr = KAIDateUtil.dateToString(newDepartureDate, KAIDateUtil.DEFAULT_VIEW_FORMAT);
            kaiHomepageViewModel.setDepartureDateFmt(newDepartureDateFmtStr);
            if (!kaiHomepageViewModel.isOneWay()) {
                Date currentReturnDate = KAIDateUtil.stringToDate(kaiHomepageViewModel.getReturnDate());
                if (currentReturnDate.compareTo(newDepartureDate) < 0) {
                    Date reAssignReturnDate = KAIDateUtil.addDate(newDepartureDate, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL);
                    kaiHomepageViewModel.setReturnDate(KAIDateUtil.dateToString(reAssignReturnDate, KAIDateUtil.DEFAULT_FORMAT));
                    kaiHomepageViewModel.setReturnDateFmt(KAIDateUtil.dateToString(reAssignReturnDate, KAIDateUtil.DEFAULT_VIEW_FORMAT));
                }
                trainHomepageView.renderRoundTripView(kaiHomepageViewModel);
            } else {
                Date reAssignReturnDate = KAIDateUtil.addDate(newDepartureDate, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL);
                kaiHomepageViewModel.setReturnDate(KAIDateUtil.dateToString(reAssignReturnDate, KAIDateUtil.DEFAULT_FORMAT));
                kaiHomepageViewModel.setReturnDateFmt(KAIDateUtil.dateToString(reAssignReturnDate, KAIDateUtil.DEFAULT_VIEW_FORMAT));
                trainHomepageView.renderSingleTripView(kaiHomepageViewModel);
            }
        }
    }

    @Override
    public void onReturnDateChange(int year, int month, int dayOfMonth) {
        Calendar now = KAIDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);

        Date newReturnDate = now.getTime();

        Date departureDate = KAIDateUtil.stringToDate(kaiHomepageViewModel.getDepartureDate());
        Date limitDate = KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);

        if (newReturnDate.after(limitDate)) {
            trainHomepageView.showReturnDateMax100Days(R.string.kai_homepage_return_max_100_days_from_today_error);
        } else if (newReturnDate.before(departureDate)) {
            trainHomepageView.showReturnDateShouldGreaterOrEqual(R.string.kai_homepage_return_should_greater_equal_error);
        } else {
            String newReturnDateStr = KAIDateUtil.dateToString(newReturnDate, KAIDateUtil.DEFAULT_FORMAT);
            kaiHomepageViewModel.setReturnDate(newReturnDateStr);
            String newReturnDateFmtStr = KAIDateUtil.dateToString(newReturnDate, KAIDateUtil.DEFAULT_VIEW_FORMAT);
            kaiHomepageViewModel.setReturnDateFmt(newReturnDateFmtStr);
            trainHomepageView.renderRoundTripView(kaiHomepageViewModel);
        }
    }

}
