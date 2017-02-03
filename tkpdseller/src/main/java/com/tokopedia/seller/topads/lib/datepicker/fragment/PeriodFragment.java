package com.tokopedia.seller.topads.lib.datepicker.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.lib.datepicker.DatePickerActivity;
import com.tokopedia.seller.topads.lib.datepicker.DatePickerUtils;
import com.tokopedia.seller.topads.lib.datepicker.PeriodChooseViewHelper;
import com.tokopedia.seller.topads.lib.datepicker.model.PeriodRangeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class PeriodFragment extends Fragment {

    public interface Callback {

        void onDateSubmitted(int selectionPeriod, long startDate, long endDate);

    }

    private Button submitButton;
    private LinearLayout periodLayout;

    private List<PeriodChooseViewHelper> periodChooseViewHelpers;
    private ArrayList<PeriodRangeModel> periodRangeModelList;
    private List<DatePickerUtils.BasePeriodModel> basePeriodModels;
    private DatePickerUtils.PeriodListener periodListener = new DatePickerUtils.PeriodListener() {
        @Override
        public void updateCheck(boolean checked, int index) {

            // dont unselect period.
            if (isAllNone(checked, index))
                return;

            for (int i = 0; i < basePeriodModels.size(); i++) {
                if (index != i) {
                    if (basePeriodModels.get(i) instanceof PeriodRangeModel) {
                        PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                        prm.isChecked = false;
                        basePeriodModels.set(i, prm);

                        periodChooseViewHelpers.get(i).resetToFalse();
                    }
                } else {
                    if (basePeriodModels.get(i) instanceof PeriodRangeModel) {
                        PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                        prm.isChecked = true;
                        basePeriodModels.set(i, prm);
                    }
                }
            }
        }

        @Override
        public boolean isAllNone(boolean isChecked, int index) {
            int isNoneAll = 0;
            for (int i = 0; i < basePeriodModels.size(); i++) {
                if (i == index && !isChecked) {
                    isNoneAll++;
                    continue;
                }
                if (!((PeriodRangeModel) basePeriodModels.get(i)).isChecked) {
                    isNoneAll++;
                }
            }

            // dont unselect period.
            return isNoneAll == basePeriodModels.size();
        }
    };

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static PeriodFragment newInstance(int selectionPeriod, ArrayList<PeriodRangeModel> periodRangeModelList) {
        PeriodFragment fragment = new PeriodFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerActivity.SELECTION_PERIOD, selectionPeriod);
        bundle.putParcelableArrayList(DatePickerActivity.DATE_PERIOD_LIST, periodRangeModelList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_date_picker_period, container, false);

        submitButton = (Button) rootView.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDate();
            }
        });
        periodLayout = (LinearLayout) rootView.findViewById(R.id.period_linlay);

        int selection = 1;
        Bundle bundle = getArguments();
        if (bundle != null) {
            selection = bundle.getInt(DatePickerActivity.SELECTION_PERIOD, 1);
            periodRangeModelList = bundle.getParcelableArrayList(DatePickerActivity.DATE_PERIOD_LIST);
        }

        basePeriodModels = new ArrayList<>();
        for (PeriodRangeModel periodRangeModel : periodRangeModelList) {
            basePeriodModels.add(periodRangeModel);
        }

        if (selection < basePeriodModels.size()) {
            //[START] set last selection
            PeriodRangeModel periodRangeModel = (PeriodRangeModel) basePeriodModels.get(selection);
            periodRangeModel.isChecked = true;
            basePeriodModels.set(selection, periodRangeModel);
        }

        periodChooseViewHelpers = new ArrayList<>();
        for (int i = 0; i < basePeriodModels.size(); i++) {
            @SuppressWarnings("ConstantConditions") View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_date_picker_periode, periodLayout, false);
            PeriodChooseViewHelper periodChooseViewHelper = new PeriodChooseViewHelper(view, i);
            periodChooseViewHelper.bindData((PeriodRangeModel) basePeriodModels.get(i));
            periodChooseViewHelper.setPeriodListener(periodListener);
            periodChooseViewHelpers.add(periodChooseViewHelper);
            periodLayout.addView(view);
        }
        return rootView;
    }

    private void submitDate() {
        if (callback == null) {
            return;
        }
        for (int i = 0; i < basePeriodModels.size(); i++) {
            PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
            if (prm.isChecked) {
                callback.onDateSubmitted(i, prm.startDate, prm.endDate);
            }
        }
    }
}