package com.tokopedia.seller.topads.lib.datepicker;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by Nathaniel on 1/16/2017.
 */

@Deprecated
public class BasePeriodViewHolder extends RecyclerView.ViewHolder {
    @BindView(R2.id.checkbox_period)
    CheckBox checkBoxPeriod;

    @BindView(R2.id.period_header)
    TextView periodHeader;

    @BindView(R2.id.period_date)
    TextView periodDate;
    private PeriodRangeModel periodRangeModel;
    SetDateFragment.PeriodListener periodListener;

//        @BindArray(R.array.month_names)
//        String[] monthNames;

    public void setPeriodListener(SetDateFragment.PeriodListener periodListener) {
        this.periodListener = periodListener;
    }

    @OnCheckedChanged(R2.id.checkbox_period)
    public void onCheckBoxPeriod(boolean checked) {
        periodRangeModel.isChecked = checked;
        if (periodListener != null) {
            periodListener.updateCheck(checked, getLayoutPosition());
        }
    }

    public BasePeriodViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
            split[i] = Integer.parseInt(SetDateFragment.reverseDate(split1));
        }
        String[] monthNamesAbrev = itemView.getContext().getResources().getStringArray(R.array.month_names_abrev);
        if (split.length > 1) {
            String res = String.format("%s - %s", SetDateFragment.getDateWithYear(split[0], monthNamesAbrev), SetDateFragment.getDateWithYear(split[1], monthNamesAbrev));
            periodDate.setText(res);
        }

        if (split.length == 1) {
            String res = String.format("%s", SetDateFragment.getDateWithYear(split[0], monthNamesAbrev));
            periodDate.setText(res);
        }
    }
}
