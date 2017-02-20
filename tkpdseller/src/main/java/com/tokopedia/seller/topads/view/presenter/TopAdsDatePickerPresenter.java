package com.tokopedia.seller.topads.view.presenter;

import android.app.Activity;
import android.content.Intent;

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

    void saveSelectionDatePicker(int selectionDatePickerType, int selectionDatePeriodIndex);

    String getRangeDateFormat(Date startDate, Date endDate);

    Intent getDatePickerIntent(Activity activity, Date startDate, Date endDate);
}
