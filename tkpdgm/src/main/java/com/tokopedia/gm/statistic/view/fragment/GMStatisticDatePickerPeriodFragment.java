package com.tokopedia.gm.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.gm.R;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.fragment.DatePickerPeriodFragment;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.seller.common.datepicker.view.widget.DatePeriodView;
import com.tokopedia.gm.statistic.utils.GMStatisticDateUtils;

import java.util.ArrayList;

/**
 * Created by nathan on 7/12/17.
 */

public class GMStatisticDatePickerPeriodFragment extends DatePickerPeriodFragment {

    public static DatePickerPeriodFragment newInstance(int selectionPeriod, ArrayList<PeriodRangeModel> periodRangeModelList, boolean comparedDate) {
        DatePickerPeriodFragment fragment = new GMStatisticDatePickerPeriodFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerConstant.EXTRA_SELECTION_PERIOD, selectionPeriod);
        bundle.putParcelableArrayList(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, periodRangeModelList);
        bundle.putBoolean(DatePickerConstant.EXTRA_COMPARE_DATE, comparedDate);
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean comparedDate;
    private ExpandableOptionSwitch expandableOptionSwitch;
    private DatePeriodView datePeriodView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gm_statistic_date_picker_period, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableOptionSwitch = (ExpandableOptionSwitch) view.findViewById(R.id.expandable_option_switch_compare_date);
        datePeriodView = (DatePeriodView) view.findViewById(R.id.date_period_view_compared);
        Bundle bundle = getArguments();
        if (bundle != null) {
            comparedDate = bundle.getBoolean(DatePickerConstant.EXTRA_COMPARE_DATE);
        }
        expandableOptionSwitch.setExpand(comparedDate);
        PeriodRangeModel periodRangeModel = adapter.getSelectedDate();
        if (periodRangeModel != null) {
            updateComparedDateView(periodRangeModel);
        }
    }

    @Override
    public void onItemClicked(PeriodRangeModel periodRangeModel) {
        super.onItemClicked(periodRangeModel);
        updateComparedDateView(periodRangeModel);
    }

    private void updateComparedDateView(PeriodRangeModel periodRangeModel) {
        PeriodRangeModel comparedPeriodRangeModel = GMStatisticDateUtils.getComparedDate(periodRangeModel.getStartDate(), periodRangeModel.getEndDate());
        datePeriodView.setDate(comparedPeriodRangeModel.getStartDate(), comparedPeriodRangeModel.getEndDate());
    }

    @Override
    protected Intent getSubmittedIntent() {
        Intent intent = super.getSubmittedIntent();
        intent.putExtra(DatePickerConstant.EXTRA_COMPARE_DATE, expandableOptionSwitch.isExpanded());
        return intent;
    }
}