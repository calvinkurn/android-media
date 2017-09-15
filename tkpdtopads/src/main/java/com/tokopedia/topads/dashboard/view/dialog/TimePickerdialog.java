package com.tokopedia.topads.dashboard.view.dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import com.tokopedia.seller.common.datepicker.view.widget.DatePickerLabelView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nathaniel on 12/20/2016.
 */

public class TimePickerdialog extends TimePickerDialog {

    public TimePickerdialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
    }

    public TimePickerdialog(Context context, Calendar calendar, boolean is24HourView, OnTimeSetListener listener) {
        super(context, listener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HourView);
    }

    public static class OnTimeSetListener implements TimePickerDialog.OnTimeSetListener {

        private DatePickerLabelView datePicker;
        private String dateFormat;
        private Date date;

        public OnTimeSetListener(DatePickerLabelView datePicker, Date date, String dateFormat) {
            this.datePicker = datePicker;
            this.date = date;
            this.dateFormat = dateFormat;
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);
            date = calendar.getTime();
            datePicker.setContent(new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(date));
            onDateUpdated(date);
        }

        public void onDateUpdated(Date date) {

        }

    }
}