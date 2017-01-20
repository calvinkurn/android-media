package com.tokopedia.seller.gmstat.views.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.listeners.PeriodListener;
import com.tokopedia.seller.gmstat.views.models.PeriodRangeModel;

import butterknife.ButterKnife;

import static com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils.getDateWithYear;
import static com.tokopedia.seller.gmstat.views.SetDateFragment.reverseDate;

/**
 * Created by normansyahputa on 1/18/17.
 */

@Deprecated
public class BasePeriodViewHolder extends RecyclerView.ViewHolder{

    void initView(View rootView){
        checkBoxPeriod = (CheckBox) rootView.findViewById(R.id.checkbox_period);
        checkBoxPeriod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckBoxPeriod(isChecked);
            }
        });

        periodHeader = (TextView) rootView.findViewById(R.id.period_header);

        periodDate = (TextView) rootView.findViewById(R.id.period_date);
        monthNamesAbrev = rootView.getResources().getStringArray(R.array.month_names_abrev);

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

    public void onCheckBoxPeriod(boolean checked){
        periodRangeModel.isChecked = checked;
        if(periodListener != null){
            periodListener.updateCheck(checked, getLayoutPosition());
        }
    }

    public BasePeriodViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        initView(itemView);
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
