package com.tokopedia.seller.topads.lib.datepicker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.seller.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class CustomDateFragment extends Fragment {

    public interface Callback {

        void onDateSubmitted(long startDate, long endDate);

    }

    private RecyclerView recyclerView;
    private PeriodAdapter periodAdapter;
    private long startDate;
    private long endDate;
    private long minStartDate;
    private long maxEndDate;
    private int maxDateRange;
    private Button saveButton;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static CustomDateFragment newInstance(long sDate, long eDate, long minStartDate, long maxEndDate, int maxDateRange) {
        CustomDateFragment fragment = new CustomDateFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(DatePickerActivity.CUSTOM_START_DATE, sDate);
        bundle.putLong(DatePickerActivity.CUSTOM_END_DATE, eDate);
        bundle.putLong(DatePickerActivity.MIN_START_DATE, minStartDate);
        bundle.putLong(DatePickerActivity.MAX_END_DATE, maxEndDate);
        bundle.putInt(DatePickerActivity.MAX_DATE_RANGE, maxDateRange);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            startDate = bundle.getLong(DatePickerActivity.CUSTOM_START_DATE, -1);
            endDate = bundle.getLong(DatePickerActivity.CUSTOM_END_DATE, -1);
            minStartDate = bundle.getLong(DatePickerActivity.MIN_START_DATE, -1);
            maxEndDate = bundle.getLong(DatePickerActivity.MAX_END_DATE, -1);
            maxDateRange = bundle.getInt(DatePickerActivity.MAX_DATE_RANGE, -1);
        }
        View rootView = inflater.inflate(R.layout.fragment_date_picker_period, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.period_recyclerview);
        saveButton = (Button) rootView.findViewById(R.id.save_date);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDate();
            }
        });
        recyclerView.setVisibility(View.VISIBLE);
        periodAdapter = new PeriodAdapter(rootView, startDate, endDate, minStartDate, maxEndDate, maxDateRange);

        List<SetDateFragment.BasePeriodModel> basePeriodModels = new ArrayList<>();
        StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
        startOrEndPeriodModel.setStartDate(startDate);
        startOrEndPeriodModel.setEndDate(endDate);
        startOrEndPeriodModel.setMinStartDate(minStartDate);
        startOrEndPeriodModel.setMaxEndDate(maxEndDate);
        startOrEndPeriodModel.setMaxDateRange(maxDateRange);
        basePeriodModels.add(startOrEndPeriodModel);

        startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
        startOrEndPeriodModel.setStartDate(startDate);
        startOrEndPeriodModel.setEndDate(endDate);
        startOrEndPeriodModel.setMinStartDate(minStartDate);
        startOrEndPeriodModel.setMaxEndDate(maxEndDate);
        startOrEndPeriodModel.setMaxDateRange(maxDateRange);
        basePeriodModels.add(startOrEndPeriodModel);

        periodAdapter.setBasePeriodModels(basePeriodModels);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(periodAdapter);

        return rootView;
    }

    public void saveDate() {
        if (callback != null) {
            callback.onDateSubmitted(periodAdapter.datePickerRules.sDate, periodAdapter.datePickerRules.eDate);
        }
    }
}