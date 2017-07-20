package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.fragment.DatePickerCustomFragment;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.seller.common.datepicker.view.widget.DatePeriodView;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatatisticDateUtils;

/**
 * Created by nathan on 7/12/17.
 */

public class GMStatisticDatePickerCustomFragment extends DatePickerCustomFragment {

    public static DatePickerCustomFragment newInstance(long sDate, long eDate, long minStartDate, long maxEndDate, int maxDateRange, boolean compared) {
        DatePickerCustomFragment fragment = new GMStatisticDatePickerCustomFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(DatePickerConstant.EXTRA_START_DATE, sDate);
        bundle.putLong(DatePickerConstant.EXTRA_END_DATE, eDate);
        bundle.putLong(DatePickerConstant.EXTRA_MIN_START_DATE, minStartDate);
        bundle.putLong(DatePickerConstant.EXTRA_MAX_END_DATE, maxEndDate);
        bundle.putInt(DatePickerConstant.EXTRA_MAX_DATE_RANGE, maxDateRange);
        bundle.putBoolean(DatePickerConstant.EXTRA_COMPARE_DATE, compared);
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean compareDate;
    private ExpandableOptionSwitch expandableOptionSwitch;
    private DatePeriodView datePeriodView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gm_statistic_date_picker_custom, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        expandableOptionSwitch = (ExpandableOptionSwitch) view.findViewById(R.id.expandable_option_switch_compare_date);
        datePeriodView = (DatePeriodView) view.findViewById(R.id.date_period_view_compared);
        if (savedInstanceState != null) {
            compareDate = savedInstanceState.getBoolean(DatePickerConstant.EXTRA_COMPARE_DATE);
        }
        expandableOptionSwitch.setExpand(compareDate);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void updateDateView() {
        super.updateDateView();
        if (datePeriodView != null) {
            PeriodRangeModel periodRangeModel = GMStatatisticDateUtils.getComparedDate(startDate, endDate);
            datePeriodView.setDate(periodRangeModel.getStartDate(), periodRangeModel.getEndDate());
        }
    }
}
