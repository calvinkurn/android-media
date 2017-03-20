package com.tokopedia.seller.reputation.view.helper;

import android.view.View;

import com.tokopedia.seller.gmstat.views.GMStatHeaderViewHelper;

/**
 * Created by normansyahputa on 3/17/17.
 */

public class ReputationHeaderViewHelper extends GMStatHeaderViewHelper {

    public ReputationHeaderViewHelper(View itemView) {
        super(itemView, true);
        stopLoading();
    }

    public void setData(String startDate, String endDate) {
        calendarRange.setText(startDate + " - " + endDate);
        setImageIcon();
        stopLoading();
    }
}
