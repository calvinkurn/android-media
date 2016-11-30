package com.tokopedia.seller.topads.presenter;

import android.content.Intent;

import com.tokopedia.core.inboxmessage.model.ActInboxMessagePass;
import com.tokopedia.core.inboxmessage.model.InboxMessagePass;
import com.tokopedia.core.inboxmessage.model.inboxmessage.InboxMessageItem;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.data.Summary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nisie on 5/9/16.
 */
public interface TopAdsDashboardPresenter {

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
