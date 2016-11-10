package com.tokopedia.transaction.purchase.model;

import java.util.Calendar;

/**
 * Created by Angga.Prasetiyo on 25/04/2016.
 */
public class AllTxFilter {
    private static final String TAG = AllTxFilter.class.getSimpleName();

    private int dayStart;
    private int monthStart;
    private int yearStart;
    private int dayEnd;
    private int monthEnd;
    private int yearEnd;
    private String filter = "";
    private String query = "";

    public static AllTxFilter instanceFirst(String txFilterID) {
        AllTxFilter data = new AllTxFilter();
        Calendar c = Calendar.getInstance();
        data.setDayEnd(c.get(Calendar.DAY_OF_MONTH));
        data.setMonthEnd(c.get(Calendar.MONTH) + 1);
        data.setYearEnd(c.get(Calendar.YEAR));

        c.add(Calendar.DAY_OF_MONTH, -30);
        data.setDayStart(c.get(Calendar.DAY_OF_MONTH));
        data.setMonthStart(c.get(Calendar.MONTH) + 1);
        data.setYearStart(c.get(Calendar.YEAR));
        data.setFilter(txFilterID);
        data.setQuery("");
        return data;
    }

    public String getDateEnd() {
        return dayEnd + "/" + monthEnd + "/" + yearEnd;
    }

    public String getDateStart() {
        return dayStart + "/" + monthStart + "/" + yearStart;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getDayStart() {
        return dayStart;
    }

    public void setDayStart(int dayStart) {
        this.dayStart = dayStart;
    }

    public int getMonthStart() {
        return monthStart;
    }

    public void setMonthStart(int monthStart) {
        this.monthStart = monthStart;
    }

    public int getYearStart() {
        return yearStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }

    public int getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(int dayEnd) {
        this.dayEnd = dayEnd;
    }

    public int getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(int monthEnd) {
        this.monthEnd = monthEnd;
    }

    public int getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }
}
