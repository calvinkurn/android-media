package com.tokopedia.seller.gmstat.views;

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
import com.tokopedia.seller.gmstat.views.adapter.PeriodAdapter;
import com.tokopedia.seller.gmstat.views.models.BasePeriodModel;
import com.tokopedia.seller.gmstat.views.models.StartOrEndPeriodModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.tokopedia.seller.gmstat.views.SetDateActivity.CUSTOM_END_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.CUSTOM_START_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.CUSTOM_TYPE;

/**
 * Created by normansyahputa on 1/19/17.
 */

public class CustomFragment extends Fragment {
    RecyclerView periodRecyclerView;
    private Unbinder unbinder;
    private PeriodAdapter periodAdapter;
    LinearLayout periodLinLay;
    private long sDate, eDate;

    public void saveDate(){
        if(getActivity() != null && getActivity() instanceof SetDateFragment.SetDate){
            long sDate = periodAdapter.datePickerRules.sDate;
            long eDate = periodAdapter.datePickerRules.eDate;
            ((SetDateFragment.SetDate)getActivity()).returnStartAndEndDate(sDate, eDate, -1, CUSTOM_TYPE);
        }
    }

    void initView(View rootView){
        periodLinLay = (LinearLayout) rootView.findViewById(R.id.period_linlay);
        periodRecyclerView = (RecyclerView) rootView.findViewById(R.id.period_recyclerview);
        rootView.findViewById(R.id.save_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDate();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            sDate = bundle.getLong(CUSTOM_START_DATE, -1);
            eDate = bundle.getLong(CUSTOM_END_DATE, -1);
        }
        View rootView = inflater.inflate(R.layout.period_layout, container, false);
        initView(rootView);
        unbinder = ButterKnife.bind(this, rootView);

        periodLinLay.setVisibility(View.GONE);
        periodRecyclerView.setVisibility(View.VISIBLE);
        periodAdapter = new PeriodAdapter(rootView, sDate, eDate);

        List<BasePeriodModel> basePeriodModels = new ArrayList<>();
        StartOrEndPeriodModel startOrEndPeriodModel = new StartOrEndPeriodModel(true, false, "Tanggal Mulai");
        startOrEndPeriodModel.setStartDate(sDate);
        basePeriodModels.add(startOrEndPeriodModel);
        startOrEndPeriodModel = new StartOrEndPeriodModel(false, true, "Tanggal Selesai");
        startOrEndPeriodModel.setEndDate(eDate);
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

    public static Fragment newInstance(long sDate, long eDate){
        Fragment fragment = new CustomFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(CUSTOM_START_DATE, sDate);
        bundle.putLong(CUSTOM_END_DATE, eDate);
        fragment.setArguments(bundle);
        return fragment;
    }
}
