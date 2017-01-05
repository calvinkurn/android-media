package com.tokopedia.seller.topads.presenter;

import java.util.Date;

/**
 * Created by Nisie on 5/9/16.
 */
public interface TopAdsDatePickerPresenter {

    void resetDate();

    void saveDate(Date startDate, Date endDate);

    Date getStartDate();

    Date getEndDate();

    boolean isDateUpdated(Date startDate, Date endDate);
}
