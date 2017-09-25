package com.tokopedia.topads.dashboard.view.dialog;

import android.content.Context;
import android.view.Window;
import android.widget.DatePicker;

import com.tokopedia.seller.common.datepicker.view.widget.DatePickerLabelView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nathaniel on 12/20/2016.
 */

public class DatePickerDialog extends android.app.DatePickerDialog {

    public DatePickerDialog(Context context, Calendar calendar, OnDateSetListener callBack) {
        super(context, callBack, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void setMinDate(long timestamp) {
        Calendar minDateCalendar = Calendar.getInstance();
        minDateCalendar.setTimeInMillis(timestamp);
        minDateCalendar.set(Calendar.HOUR_OF_DAY, minDateCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        minDateCalendar.set(Calendar.MINUTE, minDateCalendar.getMinimum(Calendar.MINUTE));
        minDateCalendar.set(Calendar.SECOND, minDateCalendar.getMinimum(Calendar.SECOND));
        minDateCalendar.set(Calendar.MILLISECOND, minDateCalendar.getMinimum(Calendar.MILLISECOND));
        getDatePicker().setMinDate(minDateCalendar.getTimeInMillis());

    }

    public static class OnDateSetListener implements android.app.DatePickerDialog.OnDateSetListener {

        private DatePickerLabelView datePicker;
        private String dateFormat;
        private Date date;

        public OnDateSetListener(DatePickerLabelView datePicker, Date date, String dateFormat) {
            this.datePicker = datePicker;
            this.date = date;
            this.dateFormat = dateFormat;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            date = calendar.getTime();
            this.datePicker.setContent(new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(date));
            onDateUpdated(date);
        }

        public void onDateUpdated(Date date) {

        }
    }
}