package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDatePickerPresenterImpl implements TopAdsDatePickerPresenter {

    private static final int DIFF_START_DAYS = -7;

    private DashboardTopadsInteractor dashboardTopadsInteractor;

    public TopAdsDatePickerPresenterImpl(Context context) {
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
        if (startDate.getTime() != getStartDate().getTime()) {
            return true;
        }
        if (endDate.getTime() != getEndDate().getTime()) {
            return true;
        }
        return false;
    }
}