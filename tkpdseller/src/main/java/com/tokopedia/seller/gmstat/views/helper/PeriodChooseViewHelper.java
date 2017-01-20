package com.tokopedia.seller.gmstat.views.helper;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.listeners.PeriodListener;
import com.tokopedia.seller.gmstat.views.models.PeriodRangeModel;

import butterknife.ButterKnife;

import static com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils.getDateWithYear;
import static com.tokopedia.seller.gmstat.views.SetDateFragment.reverseDate;

/**
 * Created by normansyahputa on 1/19/17.
 */

public class PeriodChooseViewHelper {

    private View itemView;
    private int position;

    public PeriodChooseViewHelper(View itemView, int position){
        this.itemView = itemView;
        this.position = position;
        ButterKnife.bind(this, itemView);
        initView(itemView);
    }


    void initView(View itemView){
        checkBoxPeriod = (CheckBox) itemView.findViewById(R.id.checkbox_period);
        periodHeader = (TextView) itemView.findViewById(R.id.period_header);
        periodDate = (TextView) itemView.findViewById(R.id.period_date);
        itemView.findViewById(R.id.overlay_set_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckForOther();
            }
        });
        monthNamesAbrev = itemView.getResources().getStringArray(R.array.month_names_abrev);
    }

    CheckBox checkBoxPeriod;

    TextView periodHeader;

    TextView periodDate;
    private PeriodRangeModel periodRangeModel;
    PeriodListener periodListener;

//        @BindArray(R.array.month_names)
//        String[] monthNames;

    String[] monthNamesAbrev;

    public void setPeriodListener(PeriodListener periodListener) {
        this.periodListener = periodListener;
    }

    public void onCheckForOther(){
        if(periodListener.isAllNone(!checkBoxPeriod.isChecked(), position)){
            return;
        }
        checkBoxPeriod.setChecked(!checkBoxPeriod.isChecked());
        onCheckBoxPeriod(!checkBoxPeriod.isChecked());
    }

    //        @OnCheckedChanged(R.id.checkbox_period)
    public void onCheckBoxPeriod(boolean checked){
        periodRangeModel.isChecked = checked;
        if(periodListener != null){
            periodListener.updateCheck(checked, position);
        }
    }

    public void resetToFalse(){
        checkBoxPeriod.setChecked(false);
    }

    public void bindData(PeriodRangeModel periodRangeModel){
        this.periodRangeModel = periodRangeModel;
        if(periodRangeModel.isChecked){
            checkBoxPeriod.setChecked(true);
        }else{
            checkBoxPeriod.setChecked(false);
        }

        if(periodRangeModel.range == 1 && !periodRangeModel.isRange){
            periodHeader.setText(R.string.yesterday);
        }else if(periodRangeModel.isRange){
            if(periodRangeModel.range==7){
                periodHeader.setText(R.string.seven_days_ago);
            }else if(periodRangeModel.range == 31){
                periodHeader.setText(R.string.thirty_days_ago);
            }
        }
//            periodHeader.setText(periodRangeModel.headerText);

        String description = periodRangeModel.getDescription();
        Log.d("MNORMANSYAH", "description : "+description);
        String[] range = description.split("-");
        int[] split = new int[range.length];
        int i;
        for(i=0;i<range.length;i++){
            String[] split1 = range[i].split(" ");
            split[i] = Integer.parseInt(reverseDate(split1));
        }

        if(split.length  >1 ){
            String res = String.format("%s - %s", getDateWithYear(split[0], monthNamesAbrev), getDateWithYear(split[1], monthNamesAbrev));
            periodDate.setText(res);
        }

        if(split.length  ==1 ){
            String res = String.format("%s", getDateWithYear(split[0], monthNamesAbrev));
            periodDate.setText(res);
        }
    }
}
