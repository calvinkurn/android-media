package com.tokopedia.seller.gmstat.views.viewholder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.DatePickerRules;
import com.tokopedia.seller.gmstat.utils.DateValidationListener;
import com.tokopedia.seller.gmstat.views.models.StartOrEndPeriodModel;

import java.util.Calendar;

import butterknife.ButterKnife;

import static com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils.getDateWithYear;
import static com.tokopedia.seller.gmstat.views.SetDateFragment.reverseDate;
import static com.tokopedia.seller.gmstat.views.models.StartOrEndPeriodModel.YESTERDAY;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class CustomViewHolder extends RecyclerView.ViewHolder{

//        @BindArray(R.array.month_names)
//        String[] monthNames;

    String[] monthNamesAbrev;

    TextView customHeader;

    TextView customDate;

    ImageView customDropDown;

//        DateValidationListener dateValidationListener;

    void initView(View rootView){
        monthNamesAbrev = rootView.getResources().getStringArray(R.array.month_names_abrev);
        customHeader = (TextView) rootView.findViewById(R.id.custom_header);
        customDate = (TextView) rootView.findViewById(R.id.custom_date);
        customDropDown = (ImageView) rootView.findViewById(R.id.custom_drop_down);
        rootView.findViewById(R.id.custom_date).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onChooseDate();
                    }
                }
        );
        rootView.findViewById(R.id.custom_drop_down).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onChooseDate();
                    }
                }
        );
    }

    DatePickerRules datePickerRules;
    private Calendar cal;

    public void setDatePickerRules(DatePickerRules datePickerRules) {
        this.datePickerRules = datePickerRules;
    }

    public void setDateValidationListener(DateValidationListener dateValidationListener) {
//            this.dateValidationListener = dateValidationListener;
    }

    private StartOrEndPeriodModel startOrEndPeriodModel;

    public void onChooseDate(){
        if(startOrEndPeriodModel == null || !(this.itemView.getContext() instanceof Activity))
            return;
        Calendar minDate = Calendar.getInstance();
        minDate.set(2015, 6, 25);
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DATE, YESTERDAY);
        DatePickerUtil datePicker =
                new DatePickerUtil(
                        (Activity) this.itemView.getContext(),
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.YEAR)
                );

        datePicker.setMinDate(minDate.getTimeInMillis());
        datePicker.setMaxDate(maxDate.getTimeInMillis());

        if(startOrEndPeriodModel.isEndDate){
            datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
                @Override
                public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear - 1, dayOfMonth);
                    Log.d("MNORMANSYAH", "year : "+year+" monthOfYear "+monthOfYear+ " dayOfMonth "+dayOfMonth);
                    String month = ((monthOfYear+1 < 10)?("0"+(monthOfYear+1)):(monthOfYear+1)+"");
                    String day = ((dayOfMonth < 10)?("0"+dayOfMonth):dayOfMonth+"");
                    String data = year+""+month+""+day;
                    Log.d("MNORMANSYAH", "data : "+data);

                    datePickerRules.seteDate(newDate.getTimeInMillis());
                }
            });
        }

        if(startOrEndPeriodModel.isStartDate){
            datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
                @Override
                public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear - 1, dayOfMonth);
                    String month = ((monthOfYear+1 < 10)?("0"+(monthOfYear+1)):(monthOfYear+1)+"");
                    String day = ((dayOfMonth < 10)?("0"+dayOfMonth):dayOfMonth+"");
                    String data = year+""+month+"" +
                            ""+day;
                    Log.d("MNORMANSYAH", "data : "+data);

                    datePickerRules.setsDate(newDate.getTimeInMillis());
                }
            });
        }
    }

    public CustomViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        initView(itemView);
    }

    public void bindData(StartOrEndPeriodModel startOrEndPeriodModel){
        this.startOrEndPeriodModel = startOrEndPeriodModel;
        customHeader.setText(startOrEndPeriodModel.textHeader);
        if(startOrEndPeriodModel.isEndDate) {
            String endDate = startOrEndPeriodModel.getEndDate();
            String[] split = endDate.split(" ");
            customDate.setText(getDateWithYear(Integer.parseInt(reverseDate(split)), monthNamesAbrev));

//                dateValidationListener.addEDate(startOrEndPeriodModel.endDate);
            cal = Calendar.getInstance();
            cal.setTimeInMillis(startOrEndPeriodModel.endDate);
        }
        if(startOrEndPeriodModel.isStartDate) {
            String startDate = startOrEndPeriodModel.getStartDate();
            String[] split = startDate.split(" ");
            customDate.setText(getDateWithYear(Integer.parseInt(reverseDate(split)), monthNamesAbrev));

//                dateValidationListener.addSDate(startOrEndPeriodModel.startDate);
            cal = Calendar.getInstance();
            cal.setTimeInMillis(startOrEndPeriodModel.startDate);
        }
    }
}
