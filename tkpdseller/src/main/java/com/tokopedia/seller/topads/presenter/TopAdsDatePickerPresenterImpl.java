package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDatePickerPresenterImpl implements TopAdsDatePickerPresenter {

    private static final int DIFF_START_DAYS = -7;
    private static final String RANGE_DATE_FORMAT = "dd MMM yyyy";
    private static final String RANGE_DATE_FORMAT_WITHOUT_YEAR = "dd MMM";

    private DashboardTopadsInteractor dashboardTopadsInteractor;

    private Context context;

    public TopAdsDatePickerPresenterImpl(Context context) {
        this.context = context;
        this.dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    @Override
    public void resetDate() {
        dashboardTopadsInteractor.resetDate();
    }

    @Override
    public void saveDate(Date startDate, Date endDate) {
        dashboardTopadsInteractor.saveDate(startDate, endDate);
    }

    @Override
    public Date getStartDate() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_YEAR, DIFF_START_DAYS);
        return dashboardTopadsInteractor.getStartDate(startCalendar.getTime());
    }

    @Override
    public Date getEndDate() {
        Calendar endCalendar = Calendar.getInstance();
        return dashboardTopadsInteractor.getEndDate(endCalendar.getTime());
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
}