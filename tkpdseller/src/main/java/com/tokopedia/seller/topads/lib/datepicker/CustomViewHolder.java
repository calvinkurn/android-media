package com.tokopedia.seller.topads.lib.datepicker;

/**
 * Created by Nathaniel on 1/16/2017.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.seller.R;

import java.util.Calendar;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView customHeader;
    TextView customDate;
    ImageView customDropDown;

    DatePickerRules datePickerRules;
    private Calendar cal;

    public void setDatePickerRules(DatePickerRules datePickerRules) {
        this.datePickerRules = datePickerRules;
    }

    private StartOrEndPeriodModel startOrEndPeriodModel;

    public CustomViewHolder(View itemView) {
        super(itemView);
        customHeader = (TextView) itemView.findViewById(R.id.custom_header);
        customDate = (TextView) itemView.findViewById(R.id.custom_date);
        customDropDown = (ImageView) itemView.findViewById(R.id.custom_drop_down);
        customDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChooseDate();
            }
        });
        customDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChooseDate();
            }
        });
    }

    public void bindData(StartOrEndPeriodModel startOrEndPeriodModel) {
        this.startOrEndPeriodModel = startOrEndPeriodModel;
        customHeader.setText(startOrEndPeriodModel.textHeader);
        String[] monthNamesAbrev = itemView.getContext().getResources().getStringArray(R.array.month_names_abrev);
        if (startOrEndPeriodModel.isEndDate) {
            String endDate = startOrEndPeriodModel.getEndDate();
            String[] split = endDate.split(" ");
            customDate.setText(SetDateFragment.getDateWithYear(Integer.parseInt(SetDateFragment.reverseDate(split)), monthNamesAbrev));
            cal = Calendar.getInstance();
            cal.setTimeInMillis(startOrEndPeriodModel.endDate);
        }
        if (startOrEndPeriodModel.isStartDate) {
            String startDate = startOrEndPeriodModel.getStartDate();
            String[] split = startDate.split(" ");
            customDate.setText(SetDateFragment.getDateWithYear(Integer.parseInt(SetDateFragment.reverseDate(split)), monthNamesAbrev));
            cal = Calendar.getInstance();
            cal.setTimeInMillis(startOrEndPeriodModel.startDate);
        }
    }

    public void onChooseDate() {
        if (startOrEndPeriodModel == null || !(this.itemView.getContext() instanceof Activity))
            return;
        Calendar minDate = Calendar.getInstance();
        minDate.setTimeInMillis(startOrEndPeriodModel.minStartDate);
        Calendar maxDate = Calendar.getInstance();
        maxDate.setTimeInMillis(startOrEndPeriodModel.maxEndDate);
        DatePickerUtil datePicker =
                new DatePickerUtil(
                        (Activity) this.itemView.getContext(),
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.YEAR)
                );

        datePicker.setMinDate(datePickerRules.minSDate);
        datePicker.setMaxDate(datePickerRules.maxEDate);

        if (startOrEndPeriodModel.isEndDate) {
            datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
                @Override
                public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear - 1, dayOfMonth);
                    Log.d("MNORMANSYAH", "year : " + year + " monthOfYear " + monthOfYear + " dayOfMonth " + dayOfMonth);
                    String month = ((monthOfYear + 1 < 10) ? ("0" + (monthOfYear + 1)) : (monthOfYear + 1) + "");
                    String day = ((dayOfMonth < 10) ? ("0" + dayOfMonth) : dayOfMonth + "");
                    String data = year + "" + month + "" + day;
                    Log.d("MNORMANSYAH", "data : " + data);

                    datePickerRules.seteDate(newDate.getTimeInMillis());
                }
            });
        }

        if (startOrEndPeriodModel.isStartDate) {
            datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
                @Override
                public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear - 1, dayOfMonth);
                    String month = ((monthOfYear + 1 < 10) ? ("0" + (monthOfYear + 1)) : (monthOfYear + 1) + "");
                    String day = ((dayOfMonth < 10) ? ("0" + dayOfMonth) : dayOfMonth + "");
                    String data = year + "" + month + "" + "" + day;
                    Log.d("MNORMANSYAH", "data : " + data);

                    datePickerRules.setsDate(newDate.getTimeInMillis());
                }
            });
        }
    }
}