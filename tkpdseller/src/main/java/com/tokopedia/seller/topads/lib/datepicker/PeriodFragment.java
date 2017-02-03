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
import android.widget.LinearLayout;

import com.tokopedia.seller.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class PeriodFragment extends Fragment {

    public interface Callback {

        void onDateSubmitted(int selectionPeriod, long startDate, long endDate);

    }

    private RecyclerView recycleView;
    private Button submitButton;
    private LinearLayout periodLayout;

    private List<PeriodChooseViewHelper> periodChooseViewHelpers;
    private ArrayList<PeriodRangeModel> periodRangeModelList;
    private List<SetDateFragment.BasePeriodModel> basePeriodModels;
    private SetDateFragment.PeriodListener periodListener = new SetDateFragment.PeriodListener() {
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

    private PeriodAdapter periodAdapter;

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

        recycleView = (RecyclerView) rootView.findViewById(R.id.period_recyclerview);
        submitButton = (Button) rootView.findViewById(R.id.save_date);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDate();
            }
        });
        periodLayout = (LinearLayout) rootView.findViewById(R.id.period_linlay);

        int lastSelection = 1;
        Bundle bundle = getArguments();
        if (bundle != null) {
            lastSelection = bundle.getInt(DatePickerActivity.SELECTION_PERIOD, 1);
            periodRangeModelList = bundle.getParcelableArrayList(DatePickerActivity.DATE_PERIOD_LIST);
        }

        //[START] old code
        periodAdapter = new PeriodAdapter();

        basePeriodModels = new ArrayList<>();
        for (PeriodRangeModel periodRangeModel : periodRangeModelList) {
            basePeriodModels.add(periodRangeModel);
        }

        if (lastSelection < basePeriodModels.size()) {
            //[START] set last selection
            PeriodRangeModel periodRangeModel = (PeriodRangeModel) basePeriodModels.get(lastSelection);
            periodRangeModel.isChecked = true;
            basePeriodModels.set(lastSelection, periodRangeModel);
        }

        periodAdapter.setBasePeriodModels(basePeriodModels);

        recycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycleView.setAdapter(periodAdapter);
        //[END] old code

        periodChooseViewHelpers = new ArrayList<>();
        for (int i = 0; i < basePeriodModels.size(); i++) {
            @SuppressWarnings("ConstantConditions") View view = LayoutInflater.from(container.getContext()).inflate(R.layout.periode_layout, periodLayout, false);
            PeriodChooseViewHelper periodChooseViewHelper = new PeriodChooseViewHelper(view, i);
            periodChooseViewHelper.bindData((PeriodRangeModel) basePeriodModels.get(i));
            periodChooseViewHelper.setPeriodListener(periodListener);
            periodChooseViewHelpers.add(periodChooseViewHelper);
            periodLayout.addView(view);
        }
        return rootView;
    }

    private void saveDate() {
        if (callback != null) {
            for (int i = 0; i < basePeriodModels.size(); i++) {
                PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                if (prm.isChecked) {
                    callback.onDateSubmitted(i, prm.startDate, prm.endDate);
                }
            }
        }
    }
}
