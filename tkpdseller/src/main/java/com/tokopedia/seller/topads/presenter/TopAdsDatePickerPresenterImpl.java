package com.tokopedia.seller.topads.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsDatePickerInteractorImpl;
import com.tokopedia.seller.lib.datepicker.DatePickerActivity;
import com.tokopedia.seller.lib.datepicker.model.PeriodRangeModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDatePickerPresenterImpl implements TopAdsDatePickerPresenter {

    private static final String RANGE_DATE_FORMAT = "dd MMM yyyy";
    private static final String RANGE_DATE_FORMAT_WITHOUT_YEAR = "dd MMM";

    private TopAdsDatePickerInteractor topAdsDatePickerInteractor;

    private Context context;

    public TopAdsDatePickerPresenterImpl(Context context) {
        this.context = context;
        this.topAdsDatePickerInteractor = new TopAdsDatePickerInteractorImpl(context);
    }

    @Override
    public void resetDate() {
        topAdsDatePickerInteractor.resetDate();
    }

    @Override
    public void saveDate(Date startDate, Date endDate) {
        topAdsDatePickerInteractor.saveDate(startDate, endDate);
    }

    @Override
    public Date getStartDate() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_YEAR, -DatePickerConstant.DIFF_ONE_WEEK);
        return topAdsDatePickerInteractor.getStartDate(startCalendar.getTime());
    }

    @Override
    public Date getEndDate() {
        Calendar endCalendar = Calendar.getInstance();
        return topAdsDatePickerInteractor.getEndDate(endCalendar.getTime());
    }

    @Override
    public boolean isDateUpdated(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return true;
        }
        String dateText = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate);
        String dateTextCache = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getStartDate());
        if (!dateText.equalsIgnoreCase(dateTextCache)) {
            return true;
        }
        dateText = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate);
        dateTextCache = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getEndDate());
        if (!dateText.equalsIgnoreCase(dateTextCache)) {
            return true;
        }
        return false;
    }

    @Override
    public void saveSelectionDatePicker(int selectionDatePickerType, int selectionDatePeriodIndex) {
        topAdsDatePickerInteractor.saveSelectionDatePicker(selectionDatePickerType, selectionDatePeriodIndex);
    }

    @Override
    public String getRangeDateFormat(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        String startDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(startDate);
        String endDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(endDate);
        if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
            startDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT_WITHOUT_YEAR, Locale.ENGLISH).format(startDate);
        }
        return context.getString(R.string.top_ads_range_date_text, startDateFormatted, endDateFormatted);
    }

    @Override
    public Intent getDatePickerIntent(Activity activity, Date startDate, Date endDate) {
        Intent intent = new Intent(activity, DatePickerActivity.class);
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR_OF_DAY, 23);
        todayCalendar.set(Calendar.MINUTE, 59);
        todayCalendar.set(Calendar.SECOND, 59);

        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.YEAR, -1);
        maxCalendar.set(Calendar.HOUR_OF_DAY, 0);
        maxCalendar.set(Calendar.MINUTE, 0);
        maxCalendar.set(Calendar.SECOND, 0);
        maxCalendar.set(Calendar.MILLISECOND, 0);

        intent.putExtra(DatePickerActivity.CUSTOM_START_DATE, startDate.getTime());
        intent.putExtra(DatePickerActivity.CUSTOM_END_DATE, endDate.getTime());

        intent.putExtra(DatePickerActivity.MIN_START_DATE, maxCalendar.getTimeInMillis());
        intent.putExtra(DatePickerActivity.MAX_END_DATE, todayCalendar.getTimeInMillis());
        intent.putExtra(DatePickerActivity.MAX_DATE_RANGE, TopAdsConstant.MAX_DATE_RANGE);

        intent.putExtra(DatePickerActivity.DATE_PERIOD_LIST, getPeriodRangeList());
        intent.putExtra(DatePickerActivity.SELECTION_PERIOD, topAdsDatePickerInteractor.getLastSelectionDatePickerIndex());
        intent.putExtra(DatePickerActivity.SELECTION_TYPE, topAdsDatePickerInteractor.getLastSelectionDatePickerType());

        intent.putExtra(DatePickerActivity.PAGE_TITLE, activity.getString(R.string.title_date_picker));
        return intent;
    }

    private ArrayList<PeriodRangeModel> getPeriodRangeList() {
        ArrayList<PeriodRangeModel> periodRangeList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        periodRangeList.add(new PeriodRangeModel(endCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.label_today)));
        startCalendar.add(Calendar.DATE, -1);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), startCalendar.getTimeInMillis(), context.getString(R.string.yesterday)));
        startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.seven_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.thirty_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DATE, 1);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.label_this_month)));
        return periodRangeList;
    }
}