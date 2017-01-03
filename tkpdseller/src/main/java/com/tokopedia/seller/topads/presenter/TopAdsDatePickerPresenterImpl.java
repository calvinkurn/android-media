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

    private DashboardTopadsInteractor dashboardTopadsInteractor;

    private Date startDate;
    private Date endDate;

    public TopAdsDatePickerPresenterImpl(Context context) {
        this.dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_YEAR, -7);
        startDate = startCalendar.getTime();
        endDate = endCalendar.getTime();
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
        startCalendar.add(Calendar.DAY_OF_YEAR, -7);
        return dashboardTopadsInteractor.getStartDate(startDate);
    }

    @Override
    public Date getEndDate() {
        Calendar endCalendar = Calendar.getInstance();
        return dashboardTopadsInteractor.getEndDate(endDate);
    }
}