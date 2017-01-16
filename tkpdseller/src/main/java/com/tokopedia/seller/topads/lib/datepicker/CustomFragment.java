package com.tokopedia.seller.topads.lib.datepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class CustomFragment extends Fragment {
    @BindView(R2.id.period_recyclerview)
    RecyclerView periodRecyclerView;
    private Unbinder unbinder;
    private PeriodAdapter periodAdapter;
    @BindView(R2.id.period_linlay)
    LinearLayout periodLinLay;
    private long sDate, eDate;
    private long minStartDate;
    private long maxEndDate;
    private int maxDateRange;

    @OnClick(R2.id.save_date)
    public void saveDate() {
        if (getActivity() != null && getActivity() instanceof SetDateFragment.SetDate) {
            long sDate = periodAdapter.datePickerRules.sDate;
            long eDate = periodAdapter.datePickerRules.eDate;
            ((SetDateFragment.SetDate) getActivity()).returnStartAndEndDate(sDate, eDate, -1, SetDateActivity.CUSTOM_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            sDate = bundle.getLong(SetDateActivity.CUSTOM_START_DATE, -1);
            eDate = bundle.getLong(SetDateActivity.CUSTOM_END_DATE, -1);
            minStartDate = bundle.getLong(SetDateActivity.MIN_START_DATE, -1);
            maxEndDate = bundle.getLong(SetDateActivity.MAX_END_DATE, -1);
            maxDateRange = bundle.getInt(SetDateActivity.MAX_DATE_RANGE, -1);
        }
        View rootView = inflater.inflate(R.layout.period_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        periodLinLay.setVisibility(View.GONE);
        periodRecyclerView.setVisibility(View.VISIBLE);
        periodAdapter = new PeriodAdapter(rootView, sDate, eDate, minStartDate, maxEndDate, maxDateRange);

        List<SetDateFragment.BasePeriodModel> basePeriodModels = new ArrayList<>();
        StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
        startOrEndPeriodModel.setStartDate(sDate);
        startOrEndPeriodModel.setEndDate(eDate);
        startOrEndPeriodModel.setMinStartDate(minStartDate);
        startOrEndPeriodModel.setMaxEndDate(maxEndDate);
        startOrEndPeriodModel.setMaxDateRange(maxDateRange);
        basePeriodModels.add(startOrEndPeriodModel);

        startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
        startOrEndPeriodModel.setStartDate(sDate);
        startOrEndPeriodModel.setEndDate(eDate);
        startOrEndPeriodModel.setMinStartDate(minStartDate);
        startOrEndPeriodModel.setMaxEndDate(maxEndDate);
        startOrEndPeriodModel.setMaxDateRange(maxDateRange);
        basePeriodModels.add(startOrEndPeriodModel);

        periodAdapter.setBasePeriodModels(basePeriodModels);

        periodRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        periodRecyclerView.setAdapter(periodAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static Fragment newInstance() {
        return new CustomFragment();
    }

    public static Fragment newInstance(long sDate, long eDate, long minStartDate, long maxEndDate, int maxDateRange) {
        Fragment fragment = new CustomFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SetDateActivity.CUSTOM_START_DATE, sDate);
        bundle.putLong(SetDateActivity.CUSTOM_END_DATE, eDate);
        bundle.putLong(SetDateActivity.MIN_START_DATE, minStartDate);
        bundle.putLong(SetDateActivity.MAX_END_DATE, maxEndDate);
        bundle.putInt(SetDateActivity.MAX_DATE_RANGE, maxDateRange);
        fragment.setArguments(bundle);
        return fragment;
    }
}