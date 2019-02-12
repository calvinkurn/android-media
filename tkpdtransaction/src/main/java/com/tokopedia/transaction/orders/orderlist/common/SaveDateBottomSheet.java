package com.tokopedia.transaction.orders.orderlist.common;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.transaction.R;
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SaveDateBottomSheet extends BottomSheets {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String TITLE = "Filter Tanggal";
    public static final String RESET_TITLE = "RESET";

    public static final Locale LOCALE = new Locale("in", "ID");
    private static final String MIN_DATE = "01/01/2017";
    private String filterStartDate = "";
    private String filterFinalDate = "";
    TextView startDate;
    TextView endDate;

    public SaveDateBottomSheet() {

    }

    public static SaveDateBottomSheet newInstance(Bundle extras) {
        SaveDateBottomSheet fragment = new SaveDateBottomSheet();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.select_filter_date;
    }

    DateFilterResult dateFilterResult;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dateFilterResult = (DateFilterResult)context;

    }


    @Override
    public void initView(View view) {
        startDate = view.findViewById(R.id.set_current_date);
        endDate = view.findViewById(R.id.set_end_date);
        TextView saveDate = view.findViewById(R.id.save_date);
        Calendar now = Calendar.getInstance();
        if (TextUtils.isEmpty(getArguments().getString(SaveDateBottomSheetActivity.START_DATE))) {
            filterStartDate = MIN_DATE;
        } else {
            filterStartDate = getArguments().getString(SaveDateBottomSheetActivity.START_DATE);
        }

        if (TextUtils.isEmpty(getArguments().getString(SaveDateBottomSheetActivity.END_DATE))) {
            filterFinalDate = formatDate(DATE_FORMAT,now.getTime());
        } else {
            filterFinalDate = getArguments().getString(SaveDateBottomSheetActivity.END_DATE);
        }
        setDateText();
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelCalendarBottomSheet travelCalendarBottomSheet = new TravelCalendarBottomSheet.Builder()
                        .setShowHoliday(true)
                        .setMinDate(getMinimumDate())
                        .setMaxDate(now.getTime())
                        .setTitle("Filter Tanggal")
                        .setSelectedDate(getDate(startDate.getText().toString()))
                        .setBottomSheetState(BottomSheets.BottomSheetsState.NORMAL)
                        .build();

                travelCalendarBottomSheet.setListener(new TravelCalendarBottomSheet.ActionListener() {
                    @Override
                    public void onClickDate(@NotNull Date dateSelected) {
                        filterStartDate = formatDate(DATE_FORMAT, dateSelected);
                        setDateText();
                    }
                });

                travelCalendarBottomSheet.show(getActivity().getSupportFragmentManager(), "travel calendar");

            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TravelCalendarBottomSheet travelCalendarBottomSheet = new TravelCalendarBottomSheet.Builder()
                        .setShowHoliday(true)
                        .setMinDate(getMinimumDate())
                        .setMaxDate(now.getTime())
                        .setTitle("Filter Tanggal")
                        .setSelectedDate(getDate(startDate.getText().toString()))
                        .setBottomSheetState(BottomSheets.BottomSheetsState.NORMAL)
                        .build();

                travelCalendarBottomSheet.setListener(new TravelCalendarBottomSheet.ActionListener() {
                    @Override
                    public void onClickDate(@NotNull Date dateSelected) {
                        filterFinalDate = formatDate(DATE_FORMAT, dateSelected);
                        setDateText();
                    }
                });

                travelCalendarBottomSheet.show(getActivity().getSupportFragmentManager(), "travel calendar");
            }
        });
        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFilterResult.setResult(getStartDate(),getEndDate());
                dismiss();
            }
        });
    }

    private Date getMinimumDate() {
        return  getDate(MIN_DATE);
    }

    private void setDateText() {
        startDate.setText(filterStartDate);
        endDate.setText(filterFinalDate);
    }


    public String getStartDate() {
        return startDate.getText().toString();
    }

    public String getEndDate() {
        return endDate.getText().toString();
    }

    @Override
    protected String title() {
        return TITLE;
    }

    @Override
    protected String resetButtonTitle() {
        return RESET_TITLE;
    }

    private Date getDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, LOCALE);
        Date minDate = new Date();
        try {
            if(date == null ) {
                minDate = dateFormat.parse(MIN_DATE);
            }else {
                minDate = dateFormat.parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return minDate;
    }

    public static String formatDate(String format, Date date){
            DateFormat dateFormat = new SimpleDateFormat(format, LOCALE);
            return dateFormat.format(date);
    }

    public interface  DateFilterResult{
        void setResult(String startDate,String endDate);
    }

    @Override
    protected void onResetButtonClicked() {
        super.onResetButtonClicked();
        Calendar now = Calendar.getInstance();
        startDate.setText(MIN_DATE);
        endDate.setText(formatDate(DATE_FORMAT,now.getTime()));
    }
}
