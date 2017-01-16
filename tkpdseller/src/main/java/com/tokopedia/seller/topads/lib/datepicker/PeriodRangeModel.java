package com.tokopedia.seller.topads.lib.datepicker;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Nathaniel on 1/16/2017.
 */

@Parcel
public class PeriodRangeModel extends SetDateFragment.BasePeriodModel {
    private static final Locale locale = new Locale("in", "ID");
    public static final int TYPE = 1;
    public boolean isChecked;
    String headerText;
    String formatText = "%s - %s";
    boolean isRange;
    int range;
    long startDate = -1, endDate = -1;

    public PeriodRangeModel() {
        super(TYPE);
    }

    public PeriodRangeModel(boolean isRange, int range) {
        this();
        this.isRange = isRange;
        this.range = range;
    }

    public String getDescription() {
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
        if (isRange) {
            Calendar sDate = calculateCalendar(StartOrEndPeriodModel.YESTERDAY);
            endDate = sDate.getTimeInMillis();
            String yesterday = dateFormat.format(sDate.getTime());

            Calendar eDate = calculateCalendar(-range);
            startDate = eDate.getTimeInMillis();
            String startDate = dateFormat.format(eDate.getTime());

            return headerText = String.format(formatText, startDate, yesterday);
        } else {
            Calendar sDate = calculateCalendar(-range);
            startDate = sDate.getTimeInMillis();
            endDate = sDate.getTimeInMillis();
            return dateFormat.format(sDate.getTime());
        }
    }

    private Calendar calculateCalendar(int daysAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysAgo);
        return cal;
    }
}