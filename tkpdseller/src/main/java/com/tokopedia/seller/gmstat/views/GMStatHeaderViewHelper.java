package com.tokopedia.seller.gmstat.views;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.library.LoaderTextView;
import com.tokopedia.seller.gmstat.utils.DateHeaderFormatter;
import com.tokopedia.seller.lib.datepicker.DatePickerActivity;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.lib.datepicker.model.PeriodRangeModel;
import com.tokopedia.seller.reputation.view.activity.SellerReputationDatePickerActivity;

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

    public static final int MOVE_TO_SET_DATE = 1;
    public static final String YYYY_M_MDD = "yyyyMMdd";
    private static final int MAX_DATE_RANGE = 60;
    private static final String MIN_DATE = "25/07/2015";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final Locale locale = new Locale("in", "ID");
    protected LoaderTextView calendarRange;
    protected LoaderImageView calendarArrowIcon;
    protected LoaderImageView calendarIcon;
    protected View itemView;
    private String[] monthNamesAbrev;
    private int gredyColor;
    private int greenColor;
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

    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
    }

    private void initView(View itemView) {
        monthNamesAbrev = itemView.getResources().getStringArray(R.array.lib_date_picker_month_entries);

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

    /**
     * this is for {@link com.tokopedia.seller.reputation.view.adapter.SellerReputationAdapter}
     *
     * @param dateHeaderFormatter
     * @param sDate
     * @param eDate
     * @param lastSelectionPeriod
     * @param selectionType
     */
    public void bindDate(DateHeaderFormatter dateHeaderFormatter, long sDate, long eDate,
                         int lastSelectionPeriod, int selectionType) {
        this.sDate = sDate;
        this.eDate = eDate;
        this.lastSelection = lastSelectionPeriod;
        this.selectionType = selectionType;

        String startDate = null;
        if (sDate != -1) {
            startDate = dateHeaderFormatter.getStartDateFormat(sDate);
        }

        String endDate = null;
        if (eDate != -1) {
            endDate = dateHeaderFormatter.getEndDateFormat(eDate);
        }

        calendarRange.setText(startDate + " - " + endDate);

        setImageIcon();
        stopLoading();
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
            DateFormat dateFormat = new SimpleDateFormat(YYYY_M_MDD, locale);
            startDate = dateFormat.format(cal.getTime());
            startDate = getDateWithYear(Integer.parseInt(startDate), monthNamesAbrev);
        }

        String endDate = null;
        if (eDate != -1) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(eDate);
            DateFormat dateFormat = new SimpleDateFormat(YYYY_M_MDD, locale);
            endDate = dateFormat.format(cal.getTime());
            int end = Integer.parseInt(endDate);
            Log.d("MNORMANSYAH", "endDate " + endDate + " int " + Integer.parseInt(endDate) + " end " + end);
            endDate = getDateWithYear(endDate, monthNamesAbrev);
        }

        calendarRange.setText(startDate + " - " + endDate);

        setImageIcon();
        stopLoading();
    }

    public void onClick(Activity activity) {
        if (!isLoading || !isGmStat) {
            return;
        }
        Intent intent = new Intent(activity, DatePickerActivity.class);
        Calendar maxCalendar = getMaxDateCalendar();

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

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, getPeriodRangeList(activity));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, lastSelection);
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, selectionType);

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, activity.getString(R.string.set_date));
        activity.startActivityForResult(intent, MOVE_TO_SET_DATE);
    }

    public void onClick(Fragment fragment) {
        if (!isLoading || !isGmStat) {
            return;
        }
        Intent intent = new Intent(fragment.getActivity(), SellerReputationDatePickerActivity.class);
        Calendar maxCalendar = getMaxDateCalendar();

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

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, getPeriodRangeList(fragment.getActivity()));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, lastSelection);
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, selectionType);

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, fragment.getString(R.string.set_date));
        fragment.startActivityForResult(intent, MOVE_TO_SET_DATE);
    }

    @NonNull
    protected Calendar getMaxDateCalendar() {
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
        endCalendar.add(Calendar.DATE, -1);
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

    public long geteDate() {
        return eDate;
    }

    public long getsDate() {
        return sDate;
    }
}