package com.tokopedia.seller.topads.lib.datepicker;

import java.util.Calendar;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class DatePickerRules {
    public long minSDate;
    public long maxEDate;
    public long sDate = -1;
    public long eDate = -1;
    DatePickerRulesListener datePickerRulesListener;
    private long maxLimit;
    private long minLimit;
    private int rangeLimit;

    public DatePickerRules(long maxEDate, long minSDate, int rangeLimit) {
        this.maxLimit = maxEDate;
        this.minLimit = minSDate;
        this.rangeLimit = rangeLimit;
        this.maxEDate = maxEDate;
        this.minSDate = minSDate;
    }

    public void setDatePickerRulesListener(DatePickerRulesListener datePickerRulesListener) {
        this.datePickerRulesListener = datePickerRulesListener;
    }

    private Calendar getInstance() {
        return Calendar.getInstance();
    }

    public long getsDate() {
        return sDate;
    }

    public void setsDate(long sDate) {
        this.sDate = sDate;
        Calendar endDateCalendar = getInstance();
        endDateCalendar.setTimeInMillis(sDate);
        endDateCalendar.add(Calendar.DATE, rangeLimit);
        if (eDate > endDateCalendar.getTimeInMillis()) {
            eDate = endDateCalendar.getTimeInMillis();
        }
        if (eDate > maxLimit) {
            eDate = maxLimit;
        }
        if (eDate < sDate) {
            eDate = sDate;
        }
        if (datePickerRulesListener != null) {
            datePickerRulesListener.successDate(sDate, eDate);
        }
    }

    public long geteDate() {
        return eDate;
    }

    public void seteDate(long endDate) {
        this.eDate = endDate;
        Calendar startDateCalendar = getInstance();
        startDateCalendar.setTimeInMillis(endDate);
        startDateCalendar.add(Calendar.DATE, -rangeLimit);
        if (sDate < startDateCalendar.getTimeInMillis()) {
            sDate = startDateCalendar.getTimeInMillis();
        }
        if (sDate < minLimit) {
            sDate = minLimit;
        }
        if (sDate > endDate) {
            sDate = endDate;
        }
        if (datePickerRulesListener != null) {
            datePickerRulesListener.successDate(sDate, endDate);
        }
    }

    public interface DatePickerRulesListener {

        void successDate(long sDate, long eDate);

    }
}