package com.tokopedia.seller.gmstat.utils;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.DatePickerActivity;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.lib.datepicker.model.PeriodRangeModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author normansyahputa on 3/30/17.
 *         This class for starting {@link DatePickerActivity}, currently only use for
 *         {@link com.tokopedia.seller.reputation.view.adapter.SellerReputationAdapter}
 */
public class DateUtilHelper {
    public static final int MOVE_TO_SET_DATE = 1;
    public static final String YYYY_M_MDD = "yyyyMMdd";
    private static final int MAX_DATE_RANGE = 60;
    private static final String MIN_DATE = "25/07/2015";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final Locale locale = new Locale("in", "ID");
    private String[] monthNamesAbrev;
    private long sDate;
    private long eDate;
    private int lastSelection;
    private int selectionType = DatePickerConstant.SELECTION_TYPE_PERIOD_DATE;

    public DateUtilHelper(Context context) {
        monthNamesAbrev = context.getResources().getStringArray(R.array.month_names_abrev);
    }

    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
    }

    public void onClick(Fragment fragment, boolean isForceSelection) {
        Intent intent = new Intent(fragment.getActivity(), DatePickerActivity.class);
        Calendar maxCalendar = getMaxCalendar();

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date minDate = new Date();
        try {
            minDate = dateFormat.parse(MIN_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar minCalendar = getMinCalendar(minDate);

        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, sDate);
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, eDate);

        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, MAX_DATE_RANGE);

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, getPeriodRangeList(fragment.getActivity()));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, lastSelection);
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, selectionType);

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, fragment.getString(R.string.set_date));
        intent.putExtra(DatePickerConstant.EXTRA_FORCE_DISPLAY_SELECTION, isForceSelection);
        fragment.startActivityForResult(intent, MOVE_TO_SET_DATE);
    }

    @NonNull
    private Calendar getMinCalendar(Date minDate) {
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.setTime(minDate);
        minCalendar.set(Calendar.HOUR_OF_DAY, 0);
        minCalendar.set(Calendar.MINUTE, 0);
        minCalendar.set(Calendar.SECOND, 0);
        minCalendar.set(Calendar.MILLISECOND, 0);
        return minCalendar;
    }

    @NonNull
    private Calendar getMaxCalendar() {
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);
        return maxCalendar;
    }

    private ArrayList<PeriodRangeModel> getPeriodRangeList(Context context) {
        ArrayList<PeriodRangeModel> periodRangeList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -1);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), startCalendar.getTimeInMillis(), context.getString(R.string.yesterday)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.seven_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.thirty_days_ago)));
        return periodRangeList;
    }

    public long getsDate() {
        return sDate;
    }

    public void setsDate(long sDate) {
        this.sDate = sDate;
    }

    public long geteDate() {
        return eDate;
    }

    public void seteDate(long eDate) {
        this.eDate = eDate;
    }
}
