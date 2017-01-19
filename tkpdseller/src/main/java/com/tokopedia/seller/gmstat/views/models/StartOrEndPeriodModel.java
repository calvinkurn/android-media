package com.tokopedia.seller.gmstat.views.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class StartOrEndPeriodModel extends BasePeriodModel {

    private static final Locale locale = new Locale("in","ID");

    public static final int TYPE = 2;
    public static final int YESTERDAY = -1;
    public static final int SEVEN_AGO = -8;
    public static final int SIXTY_DAYS_AGO = -61;
    /**
     * isEndDate 60 hari sebelum hari ini
     */
    public boolean isStartDate,
    /**
     * yesterday
     */
    isEndDate;

    public String textHeader;

    public long startDate = -1, endDate = -1;

    private StartOrEndPeriodModel() {
        super(TYPE);
    }

    public StartOrEndPeriodModel(boolean isStartDate, boolean isEndDate, String textHeader) {
        this();
        this.isStartDate = isStartDate;
        this.isEndDate = isEndDate;
        this.textHeader = textHeader;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public String getStartDate(){
        if(isStartDate) {
            Calendar cal = Calendar.getInstance();
            if(startDate == -1){
                cal.add(Calendar.DATE, SEVEN_AGO);
                startDate = cal.getTimeInMillis();
            }else{
                cal.setTimeInMillis(startDate);
            }
            System.out.println("Yesterday's date = " + cal.getTime());
            DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
            return dateFormat.format(cal.getTime());
        }else
            return "21/01/1992";
    }

    public String getEndDate(){
        if(isEndDate){
            Calendar cal = Calendar.getInstance();
            if(endDate== -1) {
                cal.add(Calendar.DATE, YESTERDAY);
                endDate = cal.getTimeInMillis();
            }else{
                cal.setTimeInMillis(endDate);
            }
            System.out.println("Yesterday's date = " + cal.getTime());
            DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", locale);
            return dateFormat.format(cal.getTime());
        }else{
            return "21/01/1992";
        }
    }
}
