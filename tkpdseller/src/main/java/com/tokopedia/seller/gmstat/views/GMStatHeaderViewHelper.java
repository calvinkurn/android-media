package com.tokopedia.seller.gmstat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.library.LoaderTextView;
import com.tokopedia.seller.lib.datepicker.DatePickerActivity;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.lib.datepicker.model.PeriodRangeModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils.getDateWithYear;

/**
 * Created by normansyahputa on 11/21/16.
 */

public class GMStatHeaderViewHelper {

    private static final int MAX_DATE_RANGE = 60;
    private static final String MIN_DATE = "25/07/2015";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    static final int MOVE_TO_SET_DATE = 1;
    private static final Locale locale = new Locale("in", "ID");
    private String[] monthNamesAbrev;
    private LoaderTextView calendarRange;
    private LoaderImageView calendarArrowIcon;
    private LoaderImageView calendarIcon;
    private int gredyColor;
    private int greenColor;
    private View itemView;
    private boolean isGmStat;
    private long sDate;
    private long eDate;
    private int lastSelection;
    private int selectionType = DatePickerConstant.SELECTION_TYPE_PERIOD_DATE;
    private boolean isLoading = true;

    public GMStatHeaderViewHelper(View itemView, boolean isGmStat) {
        this.itemView = itemView;
        this.isGmStat = isGmStat;
        initView(itemView);

        resetToLoading();
    }

    public static List<String> getDates(List<Integer> dateGraph, String[] monthNames) {
        if (dateGraph == null || dateGraph.size() <= 0)
            return null;

        String startDate = getDateWithYear(dateGraph.get(0), monthNames);

        int lastIndex = dateGraph.size() - 1;
        String endDate = getDateWithYear(dateGraph.get(lastIndex), monthNames);

        List<String> dates = new ArrayList<>();
        dates.add(startDate);
        dates.add(endDate);

        return dates;
    }

    private void initView(View itemView) {
        monthNamesAbrev = itemView.getResources().getStringArray(R.array.month_names_abrev);

        calendarRange = (LoaderTextView) itemView.findViewById(R.id.calendar_range);

        calendarArrowIcon = (LoaderImageView) itemView.findViewById(R.id.calendar_arrow_icon);

        calendarIcon = (LoaderImageView) itemView.findViewById(R.id.calendar_icon);

        gredyColor = ResourcesCompat.getColor(itemView.getResources(), R.color.grey_400, null);

        greenColor = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_up, null);
    }

    public void resetToLoading() {
        calendarRange.resetLoader();
        calendarArrowIcon.resetLoader();
        calendarIcon.resetLoader();

        isLoading = false;
    }

    public void bindData(List<Integer> dateGraph, int lastSelection) {

        this.lastSelection = lastSelection;

        resetToLoading();

        if (dateGraph == null || dateGraph.size() <= 0)
            return;

        String startDate = getDateWithYear(dateGraph.get(0), monthNamesAbrev);
        this.sDate = getDateWithYear(dateGraph.get(0));

        int lastIndex = (dateGraph.size() > 7) ? 6 : dateGraph.size() - 1;
        String endDate = getDateWithYear(dateGraph.get(lastIndex), monthNamesAbrev);
        this.eDate = getDateWithYear(dateGraph.get(lastIndex));


        calendarRange.setText(startDate + " - " + endDate);

        setImageIcon();

        stopLoading();

        if (!isGmStat) {
            calendarRange.setTextColor(gredyColor);
            calendarArrowIcon.setVisibility(View.GONE);
        } else {
            calendarRange.setTextColor(greenColor);
            calendarArrowIcon.setVisibility(View.VISIBLE);
        }
    }

    protected void setImageIcon() {
        Drawable setDateNext = AppCompatDrawableManager.get().getDrawable(itemView.getContext()
                , R.drawable.ic_set_date_next);
        calendarArrowIcon.setImageDrawable(setDateNext);
        calendarIcon.setImageResource(R.mipmap.ic_icon_calendar_02);
    }

    public void stopLoading() {
        calendarRange.stopLoading();
        calendarArrowIcon.stopLoading();
        calendarIcon.stopLoading();

        isLoading = true;
    }

    public void bindDate(long sDate, long eDate, int lastSelectionPeriod, int selectionType) {
        this.sDate = sDate;
        this.eDate = eDate;
        this.lastSelection = lastSelectionPeriod;
        this.selectionType = selectionType;

        String startDate = null;
        if (sDate != -1) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(sDate);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", locale);
            startDate = dateFormat.format(cal.getTime());
            startDate = getDateWithYear(Integer.parseInt(startDate), monthNamesAbrev);
        }

        String endDate = null;
        if (eDate != -1) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(eDate);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", locale);
            endDate = dateFormat.format(cal.getTime());
            int end = Integer.parseInt(endDate);
            Log.d("MNORMANSYAH", "endDate " + endDate + " int " + Integer.parseInt(endDate) + " end " + end);
            endDate = getDateWithYear(endDate, monthNamesAbrev);
        }

        calendarRange.setText(startDate + " - " + endDate);

        setImageIcon();
        stopLoading();
    }

    public void onClick(GMStatActivityFragment gmStatActivityFragment) {
        if (!isLoading || !isGmStat) {
            return;
        }
        Intent intent = new Intent(gmStatActivityFragment.getActivity(), DatePickerActivity.class);
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.DATE, -1);
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date minDate = new Date();
        try {
            minDate = dateFormat.parse(MIN_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.setTime(minDate);
        minCalendar.set(Calendar.HOUR_OF_DAY, 0);
        minCalendar.set(Calendar.MINUTE, 0);
        minCalendar.set(Calendar.SECOND, 0);
        minCalendar.set(Calendar.MILLISECOND, 0);

        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, sDate);
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, eDate);

        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, MAX_DATE_RANGE);

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, getPeriodRangeList(gmStatActivityFragment.getActivity()));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, lastSelection);
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, selectionType);

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, gmStatActivityFragment.getActivity().getString(R.string.set_date));
        gmStatActivityFragment.getActivity().startActivityForResult(intent, MOVE_TO_SET_DATE);
    }

    private ArrayList<PeriodRangeModel> getPeriodRangeList(Context context) {
        ArrayList<PeriodRangeModel> periodRangeList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -1);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), startCalendar.getTimeInMillis(), context.getString(R.string.yesterday)));
        startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.seven_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.thirty_days_ago)));
        return periodRangeList;
    }

    public long geteDate() {
        return eDate;
    }

    public long getsDate() {
        return sDate;
    }
}