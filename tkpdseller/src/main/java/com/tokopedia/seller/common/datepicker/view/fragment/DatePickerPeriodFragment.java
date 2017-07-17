package com.tokopedia.seller.common.datepicker.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.adapter.DatePickerPeriodAdapter;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;

import java.util.ArrayList;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class DatePickerPeriodFragment extends TkpdFragment {

    public interface Callback {

        void onDateSubmitted(int selectionPeriod, long startDate, long endDate);

    }

    private DatePickerPeriodAdapter adapter;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static DatePickerPeriodFragment newInstance(int selectionPeriod, ArrayList<PeriodRangeModel> periodRangeModelList) {
        DatePickerPeriodFragment fragment = new DatePickerPeriodFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerConstant.EXTRA_SELECTION_PERIOD, selectionPeriod);
        bundle.putParcelableArrayList(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, periodRangeModelList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker_period, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button submitButton = (Button) view.findViewById(R.id.button_submit);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDate();
            }
        });
        adapter = new DatePickerPeriodAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        Bundle bundle = getArguments();
        ArrayList<PeriodRangeModel> periodRangeModelList = null;
        if (bundle != null) {
            int selection = bundle.getInt(DatePickerConstant.EXTRA_SELECTION_PERIOD, 0);
            periodRangeModelList = bundle.getParcelableArrayList(DatePickerConstant.EXTRA_DATE_PERIOD_LIST);
            adapter.setSelectedPosition(selection);
        }
        if (periodRangeModelList == null) {
            periodRangeModelList = new ArrayList<>();
        }
        adapter.setData(periodRangeModelList);
    }

    private void submitDate() {
        if (callback != null) {
            PeriodRangeModel selectedDate = adapter.getSelectedDate();
            callback.onDateSubmitted(adapter.getSelectedPosition(), selectedDate.getStartDate(), selectedDate.getEndDate());
        } else {
            getActivity().finish();
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}