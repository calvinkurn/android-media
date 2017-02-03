package com.tokopedia.seller.topads.lib.datepicker;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.lib.datepicker.model.PeriodRangeModel;

/**
 * Created by Nathaniel on 1/16/2017.
 */
public class PeriodChooseViewHelper {

    CheckBox checkBoxPeriod;
    TextView periodHeader;
    TextView periodDate;
    View overlaySetDate;
    DatePickerUtils.PeriodListener periodListener;
    private View itemView;
    private int position;
    private PeriodRangeModel periodRangeModel;
    public PeriodChooseViewHelper(View itemView, int position) {
        this.itemView = itemView;
        this.position = position;

        checkBoxPeriod = (CheckBox) itemView.findViewById(R.id.checkbox_period);
        periodHeader = (TextView) itemView.findViewById(R.id.period_header);
        periodDate = (TextView) itemView.findViewById(R.id.period_date);
        overlaySetDate = itemView.findViewById(R.id.overlay_set_date);
        overlaySetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckForOther();
            }
        });
    }

    public void setPeriodListener(DatePickerUtils.PeriodListener periodListener) {
        this.periodListener = periodListener;
    }

    public void onCheckForOther() {
        if (periodListener.isAllNone(!checkBoxPeriod.isChecked(), position)) {
            return;
        }
        checkBoxPeriod.setChecked(!checkBoxPeriod.isChecked());
        onCheckBoxPeriod(!checkBoxPeriod.isChecked());
    }

    public void onCheckBoxPeriod(boolean checked) {
        periodRangeModel.isChecked = checked;
        if (periodListener != null) {
            periodListener.updateCheck(checked, position);
        }
    }

    public void resetToFalse() {
        checkBoxPeriod.setChecked(false);
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