package com.tokopedia.seller.lib.datepicker;

import android.content.Intent;

import com.tokopedia.seller.gmstat.views.GMStatHeaderViewHelper;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;

/**
 * @author normansyahputa on 3/20/17.
 */

public class DatePickerResultListener {

    DatePickerResult datePickerResult;

    public DatePickerResultListener(DatePickerResult datePickerResult) {
        this.datePickerResult = datePickerResult;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the request code is the same
        if (requestCode == GMStatHeaderViewHelper.MOVE_TO_SET_DATE) {
            if (data != null) {
                long sDate = data.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
                long eDate = data.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
                int lastSelection = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1);
                int selectionType = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
                if (sDate != -1 && eDate != -1) {
                    if (datePickerResult != null) {
                        datePickerResult.onDateChoosen(sDate, eDate, lastSelection, selectionType);
                    }
                }
            }
        }
    }

    public interface DatePickerResult {
        void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType);
    }

}
