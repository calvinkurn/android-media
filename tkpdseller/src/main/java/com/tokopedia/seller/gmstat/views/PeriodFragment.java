package com.tokopedia.seller.gmstat.views;

import android.app.Activity;
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

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.adapter.PeriodAdapter;
import com.tokopedia.seller.gmstat.views.helper.PeriodChooseViewHelper;
import com.tokopedia.seller.gmstat.views.listeners.PeriodListener;
import com.tokopedia.seller.gmstat.views.models.BasePeriodModel;
import com.tokopedia.seller.gmstat.views.models.PeriodRangeModel;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.seller.gmstat.views.SetDateConstant.PERIOD_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.SELECTION_PERIOD;

/**
 * Created on 1/19/17.
 * @author normansyahputa
 */

public class PeriodFragment extends BasePresenterFragment {

    RecyclerView periodRecyclerView;
    Button saveDate;
    LinearLayout periodLinLay;
    List<PeriodChooseViewHelper> periodChooseViewHelpers;
    List<BasePeriodModel> basePeriodModels;
    PeriodListener periodListener = new PeriodListener() {
        @Override
        public void updateCheck(boolean checked, int index) {

            // check if options get selected only one.
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
            return isNoneAll == basePeriodModels.size();
        }
    };

    public static Fragment newInstance(int lastSelectionPeriod) {
        Fragment fragment = new PeriodFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTION_PERIOD, lastSelectionPeriod);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance() {
        return new PeriodFragment();
    }

    public void initViews(View rootView) {
        periodRecyclerView = (RecyclerView) rootView.findViewById(R.id.period_recyclerview);
        saveDate = (Button) rootView.findViewById(R.id.save_date);
        periodLinLay = (LinearLayout) rootView.findViewById(R.id.period_linlay);

        rootView.findViewById(R.id.save_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDate();
            }
        });
    }

    public void saveDate() {
        if (getActivity() != null && getActivity() instanceof SetDateFragment.SetDate) {
            for (int i = 0; i < basePeriodModels.size(); i++) {
                PeriodRangeModel prm = (PeriodRangeModel) basePeriodModels.get(i);
                if (prm.isChecked) {
                    long sDate = prm.startDate;
                    long eDate = prm.endDate;
                    ((SetDateFragment.SetDate) getActivity()).returnStartAndEndDate(sDate, eDate, i, PERIOD_TYPE);
                }
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.period_layout, container, false);

        int lastSelection = 1;
        Bundle bundle = getArguments();
        if (bundle != null) {
            lastSelection = bundle.getInt(SELECTION_PERIOD, 1);
        }
        initViews(rootView);

        //[START] old code
        PeriodAdapter periodAdapter = new PeriodAdapter();

        basePeriodModels = new ArrayList<>();
        PeriodRangeModel e = new PeriodRangeModel(false, 1);
        e.headerText = "Kemarin";
        basePeriodModels.add(e);
        e = new PeriodRangeModel(true, 7);
        e.headerText = "7 hari terakhir";
        basePeriodModels.add(e);
        e = new PeriodRangeModel(true, 31);
        e.headerText = "30 hari terakhir";
        basePeriodModels.add(e);

        //[START] set last selection
        PeriodRangeModel periodRangeModel = (PeriodRangeModel) basePeriodModels.get(lastSelection);
        periodRangeModel.isChecked = true;
        basePeriodModels.set(lastSelection, periodRangeModel);
        //[END] set last selection

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

    //[START] unused methods
    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
    }

    @Override
    public void onSaveState(Bundle state) {
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    public void onRestoreState(Bundle savedState) {
    }

    @Override
    protected void initialListener(Activity activity) {
    }

    @Override
    protected void setupArguments(Bundle arguments) {
    }

    @Override
    protected int getFragmentLayout() {
        return 0;
    }

    @Override
    protected void initView(View view) {
    }
    //[END] unused methods


}
