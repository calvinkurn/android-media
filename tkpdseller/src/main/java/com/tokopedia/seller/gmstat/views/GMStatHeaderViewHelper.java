package com.tokopedia.seller.gmstat.views;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils.getDateWithYear;
import static com.tokopedia.seller.gmstat.views.BaseGMStatActivity.IS_GOLD_MERCHANT;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.CUSTOM_END_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.CUSTOM_START_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.PERIOD_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.SELECTION_PERIOD;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.SELECTION_TYPE;

/**
 * Created by normansyahputa on 11/21/16.
 */

public class GMStatHeaderViewHelper {

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
    private int selectionType = PERIOD_TYPE;
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
        if (!isLoading) {
            return;
        }

        // prevent to set date if non gold merchant.
        if (!isGmStat)
            return;

        Intent moveToSetDate = new Intent(gmStatActivityFragment.getActivity(), DatePickerActivity.class);
        moveToSetDate.putExtra(DatePickerActivity.SELECTION_PERIOD, lastSelection);
        moveToSetDate.putExtra(DatePickerActivity.SELECTION_TYPE, selectionType);
        moveToSetDate.putExtra(DatePickerActivity.CUSTOM_START_DATE, sDate);
        moveToSetDate.putExtra(DatePickerActivity.CUSTOM_END_DATE, eDate);
        gmStatActivityFragment.getActivity().startActivityForResult(moveToSetDate, MOVE_TO_SET_DATE);
    }

    public long geteDate() {
        return eDate;
    }

    public long getsDate() {
        return sDate;
    }
}
