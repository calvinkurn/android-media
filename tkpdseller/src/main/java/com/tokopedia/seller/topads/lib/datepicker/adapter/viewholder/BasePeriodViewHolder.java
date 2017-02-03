package com.tokopedia.seller.topads.lib.datepicker.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.lib.datepicker.DatePickerUtils;
import com.tokopedia.seller.topads.lib.datepicker.model.PeriodRangeModel;

/**
 * Created by Nathaniel on 1/16/2017.
 */

@Deprecated
public class BasePeriodViewHolder extends RecyclerView.ViewHolder {
    CheckBox checkBoxPeriod;
    TextView periodHeader;
    TextView periodDate;
    DatePickerUtils.PeriodListener periodListener;
    private PeriodRangeModel periodRangeModel;

    public BasePeriodViewHolder(View itemView) {
        super(itemView);
        checkBoxPeriod = (CheckBox) itemView.findViewById(R.id.checkbox_period);
        periodHeader = (TextView) itemView.findViewById(R.id.period_header);
        periodDate = (TextView) itemView.findViewById(R.id.period_date);
        checkBoxPeriod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onCheckBoxPeriod(b);
            }
        });
    }

    public void setPeriodListener(DatePickerUtils.PeriodListener periodListener) {
        this.periodListener = periodListener;
    }

    public void onCheckBoxPeriod(boolean checked) {
        periodRangeModel.isChecked = checked;
        if (periodListener != null) {
            periodListener.updateCheck(checked, getLayoutPosition());
        }
    }

    public void bindData(PeriodRangeModel periodRangeModel) {
        this.periodRangeModel = periodRangeModel;
        if (periodRangeModel.isChecked) {
            checkBoxPeriod.setChecked(true);
        } else {
            checkBoxPeriod.setChecked(false);
        }

        periodHeader.setText(periodRangeModel.getLabel());

        String description = periodRangeModel.getDescription();
        Log.d("MNORMANSYAH", "description : " + description);
        String[] range = description.split("-");
        int[] split = new int[range.length];
        int i;
        for (i = 0; i < range.length; i++) {
            String[] split1 = range[i].split(" ");
            split[i] = Integer.parseInt(DatePickerUtils.reverseDate(split1));
        }
        String[] monthNamesAbrev = itemView.getContext().getResources().getStringArray(R.array.month_names_abrev);
        if (split.length > 1) {
            String res = String.format("%s - %s", DatePickerUtils.getDateWithYear(split[0], monthNamesAbrev), DatePickerUtils.getDateWithYear(split[1], monthNamesAbrev));
            periodDate.setText(res);
        }

        if (split.length == 1) {
            String res = String.format("%s", DatePickerUtils.getDateWithYear(split[0], monthNamesAbrev));
            periodDate.setText(res);
        }
    }
}
