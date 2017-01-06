package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.presenter.TopAdsStatisticPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsStatisticPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsStatisticActivity;
import com.tokopedia.seller.topads.view.library.williamchart.view.LineChartView;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticActivityViewListener;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticViewListener;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsStatisticFragment extends BasePresenterFragment<TopAdsStatisticPresenter> implements TopAdsStatisticViewListener {

    @BindView(R2.id.content_title_graph)
    TextView contentTitleGraph;

    @BindView(R2.id.content_graph)
    LineChartView contentGraph;

    TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener;
    private List<Cell> cells;

    public TopAdsStatisticFragment() {
        // Required empty public constructor
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle bundle) {

    }

    @Override
    public void onRestoreState(Bundle bundle) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsStatisticPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        if(activity instanceof TopAdsStatisticActivityViewListener){
            topAdsStatisticActivityViewListener = (TopAdsStatisticActivityViewListener) activity;
        }
    }

    @Override
    protected void setupArguments(Bundle bundle) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_statistic;
    }

    @Override
    protected void initView(View view) {

    }

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
    public void updateDataCell(List<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.cells = topAdsStatisticActivityViewListener.getDataCell();
    }

    public abstract String getValueData(Cell cell);
}
