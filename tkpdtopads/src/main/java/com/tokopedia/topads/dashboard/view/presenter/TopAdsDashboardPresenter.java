package com.tokopedia.topads.dashboard.view.presenter;

import java.util.Date;

/**
 * Created by Nisie on 5/9/16.
 */
public interface TopAdsDashboardPresenter extends RetrofitPresenter {

    /**
     * Populate summary based on date range
     *
     * @param startDate
     * @param endDate
     */
    void populateSummary(final Date startDate, final Date endDate);

    /**
     * Populate top ads deposit
     */
    void populateDeposit();

    /**
     * Populate shop name and icon
     */
    void populateShopInfo();
}
