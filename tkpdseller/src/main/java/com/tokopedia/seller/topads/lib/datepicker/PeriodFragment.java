package com.tokopedia.seller.topads.lib.datepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class PeriodFragment extends Fragment {
    @BindView(R2.id.period_recyclerview)
    RecyclerView periodRecyclerView;
    private Unbinder unbinder;
    private PeriodAdapter periodAdapter;

    @BindView(R2.id.save_date)
    Button saveDate;

    @BindView(R2.id.period_linlay)
    LinearLayout periodLinLay;
    List<PeriodChooseViewHelper> periodChooseViewHelpers;
    ArrayList<PeriodRangeModel> periodRangeModelList;

    private long maxEndDate;

    @OnClick(R2.id.save_date)
    public void saveDate() {
        if (getActivity() != null && getActivity() instanceof SetDateFragment.SetDate) {
            for (int i = 0; i < basePeriodModels.size(); i++) {
                PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                if (prm.isChecked) {
                    long sDate = prm.startDate;
                    long eDate = prm.endDate;
                    ((SetDateFragment.SetDate) getActivity()).returnStartAndEndDate(sDate, eDate, i, SetDateActivity.PERIOD_TYPE);
                }
            }

        }
    }

    public static Fragment newInstance(int selectionPeriod, ArrayList<PeriodRangeModel> periodRangeModelList) {
        Fragment fragment = new PeriodFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SetDateActivity.SELECTION_PERIOD, selectionPeriod);
        bundle.putParcelableArrayList(SetDateActivity.DATE_PERIOD_LIST, periodRangeModelList);
        fragment.setArguments(bundle);
        return fragment;
    }

    List<SetDateFragment.BasePeriodModel> basePeriodModels;

    SetDateFragment.PeriodListener periodListener = new SetDateFragment.PeriodListener() {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.period_layout, container, false);

        int lastSelection = 1;
        Bundle bundle = getArguments();
        if (bundle != null) {
            lastSelection = bundle.getInt(SetDateActivity.SELECTION_PERIOD, 1);
            maxEndDate = bundle.getLong(SetDateActivity.MAX_END_DATE, Calendar.getInstance().getTimeInMillis());
            periodRangeModelList = bundle.getParcelableArrayList(SetDateActivity.DATE_PERIOD_LIST);
        }

        unbinder = ButterKnife.bind(this, rootView);
        //[START] old code
        periodAdapter = new PeriodAdapter();
        if(saveDate!=null){
            saveDate.setTransformationMethod(null);
        }

        basePeriodModels = new ArrayList<>();
        for (PeriodRangeModel periodRangeModel: periodRangeModelList) {
            basePeriodModels.add(periodRangeModel);
        }

        if (lastSelection < basePeriodModels.size()) {
            //[START] set last selection
            PeriodRangeModel periodRangeModel = (PeriodRangeModel) basePeriodModels.get(lastSelection);
            periodRangeModel.isChecked = true;
            basePeriodModels.set(lastSelection, periodRangeModel);
        }

        periodAdapter.setBasePeriodModels(basePeriodModels);

        periodRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        periodRecyclerView.setAdapter(periodAdapter);
        //[END] old code

        periodChooseViewHelpers = new ArrayList<>();
        for (int i = 0; i < basePeriodModels.size(); i++) {
            @SuppressWarnings("ConstantConditions") View view = LayoutInflater.from(container.getContext()).inflate(R.layout.periode_layout, periodLinLay, false);
            PeriodChooseViewHelper periodChooseViewHelper = new PeriodChooseViewHelper(view, i);
            periodChooseViewHelper.bindData((PeriodRangeModel) basePeriodModels.get(i));
            periodChooseViewHelper.setPeriodListener(periodListener);
            periodChooseViewHelpers.add(periodChooseViewHelper);
            periodLinLay.addView(view);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static Fragment newInstance() {
        return new PeriodFragment();
    }
}
