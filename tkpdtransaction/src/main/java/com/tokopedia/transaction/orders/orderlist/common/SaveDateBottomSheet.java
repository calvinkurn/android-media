package com.tokopedia.transaction.orders.orderlist.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.transaction.R;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.travelcalendar.view.TravelCalendarActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.tokopedia.travelcalendar.view.TravelCalendarActivity.DATE_SELECTED;

public class SaveDateBottomSheet extends BottomSheets {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String TITLE = "Filter Tanggal";

    public static final Locale LOCALE = new Locale("in", "ID");
    private static final String MIN_DATE = "01/01/2017";
    private String filterStartDate = "";
    private String filterFinalDate = "";
    TextView startDate;
    TextView endDate;

    public SaveDateBottomSheet() {

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
        filterStartDate = MIN_DATE;
        filterFinalDate = formatDate(DATE_FORMAT,now.getTime());
        setDateText();
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(TravelCalendarActivity.newInstance(getContext(), getMinimumDate(), getMinimumDate(), now.getTime(), TravelCalendarActivity.DEFAULT_TYPE), 1);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(TravelCalendarActivity.newInstance(getContext(), getMinimumDate(), getMinimumDate(), now.getTime(), TravelCalendarActivity.DEFAULT_TYPE), 2);
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

    private Date getMinimumDate() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, LOCALE);
        Date minDate = new Date();
        try {
            minDate = dateFormat.parse(MIN_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return minDate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Date date = (Date) data.getSerializableExtra(DATE_SELECTED);
            if (requestCode == 1) {
                filterStartDate = formatDate(DATE_FORMAT,date);
            } else if (requestCode == 2) {
                filterFinalDate = formatDate(DATE_FORMAT,date);

            }
            setDateText();
        }
    }

    public static String formatDate(String format, Date date){
            DateFormat dateFormat = new SimpleDateFormat(format, LOCALE);
            return dateFormat.format(date);
    }

    public interface  DateFilterResult{
        public void setResult(String startDate,String endDate);
    }

}
