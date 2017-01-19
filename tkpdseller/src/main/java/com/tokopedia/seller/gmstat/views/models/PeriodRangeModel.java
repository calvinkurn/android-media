package com.tokopedia.seller.gmstat.views.models;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.tokopedia.seller.gmstat.views.models.StartOrEndPeriodModel.YESTERDAY;

/**
 * Created by normansyahputa on 1/18/17.
 */

@Parcel
public class PeriodRangeModel extends BasePeriodModel {

    public static final int TYPE = 1;
    public boolean isChecked;
    public String headerText;
    String formatText = "%s - %s";
    public boolean isRange;
    public int range;
    public long startDate = -1, endDate = -1;

    private static final Locale locale = new Locale("in","ID");

    public PeriodRangeModel() {
        super(TYPE);
    }

    public PeriodRangeModel(boolean isRange, int range){
        this();
        this.isRange = isRange;
        this.range = range;
    }

    public String getDescription(){
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
        if(isRange) {
            Calendar sDate = calculateCalendar(YESTERDAY);
            endDate = sDate.getTimeInMillis();
            String yesterday = dateFormat.format(sDate.getTime());

            Calendar eDate = calculateCalendar(-range);
            startDate = eDate.getTimeInMillis();
            String startDate = dateFormat.format(eDate.getTime());

            return headerText = String.format(formatText, startDate, yesterday);
        }else{
            Calendar sDate = calculateCalendar(-range);
            startDate = sDate.getTimeInMillis();
            endDate = sDate.getTimeInMillis();
            return dateFormat.format(sDate.getTime());
        }
    }

    private Calendar calculateCalendar(int daysAgo){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysAgo);
        return cal;
    }
}
