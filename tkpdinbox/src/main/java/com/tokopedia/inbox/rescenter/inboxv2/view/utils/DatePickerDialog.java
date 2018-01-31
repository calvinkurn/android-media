package com.tokopedia.inbox.rescenter.inboxv2.view.utils;

import android.content.Context;
import android.view.Window;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yfsx on 31/01/18.
 */

public class DatePickerDialog extends android.app.DatePickerDialog {

    public DatePickerDialog(Context context, Calendar calendar, OnDateSetListener callBack) {
        super(context, callBack, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public static class OnDateSetListener implements android.app.DatePickerDialog.OnDateSetListener {

        private String dateFormat;
        private Date date;

        public OnDateSetListener(Date date, String dateFormat) {
            this.date = date;
            this.dateFormat = dateFormat;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            date = calendar.getTime();
            onDateUpdated(date);
        }

        public void onDateUpdated(Date date) {

        }
    }
}
