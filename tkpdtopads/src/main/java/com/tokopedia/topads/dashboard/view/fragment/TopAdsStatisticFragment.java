package com.tokopedia.topads.dashboard.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.label.DateLabelView;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.common.williamchart.base.BaseWilliamChartConfig;
import com.tokopedia.seller.common.williamchart.base.BaseWilliamChartModel;
import com.tokopedia.seller.common.williamchart.config.GrossGraphDataSetConfig;
import com.tokopedia.seller.common.williamchart.model.TooltipModel;
import com.tokopedia.seller.common.williamchart.renderer.XRenderer;
import com.tokopedia.seller.common.williamchart.tooltip.TooltipWithDynamicPointer;
import com.tokopedia.seller.common.williamchart.util.TopAdsBaseWilliamChartConfig;
import com.tokopedia.seller.common.williamchart.util.TopAdsTooltipConfiguration;
import com.tokopedia.seller.common.williamchart.view.LineChartView;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.fragment.TopAdsBaseDatePickerFragment;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenter;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenterImpl;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.view.listener.TopAdsStatisticActivityViewListener;
import com.tokopedia.topads.dashboard.view.listener.TopAdsStatisticViewListener;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsStatisticPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsStatisticPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsStatisticFragment extends TopAdsBaseDatePickerFragment<TopAdsStatisticPresenter> implements TopAdsStatisticViewListener {

    TextView contentTitleGraph;
    LineChartView contentGraph;

    TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener;
    private TopAdsBaseWilliamChartConfig topAdsBaseWilliamChartConfig;
    private BaseWilliamChartConfig baseWilliamChartConfig;
    private List<Cell> cells;
    private String[] mLabels;
    private ArrayList<TooltipModel> mLabelDisplay = new ArrayList<>();
    private float[] mValues;
    private DateLabelView dateLabelView;

    @Override
    protected BaseDatePickerPresenter getDatePickerPresenter() {
        BaseDatePickerPresenterImpl baseDatePickerPresenter = new BaseDatePickerPresenterImpl(getActivity());
        return baseDatePickerPresenter;
    }

    @Override
    protected void loadData() {
        if(dateLabelView != null)
            dateLabelView.setDate(startDate, endDate);
    }

    public TopAdsStatisticFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onRestoreState(Bundle bundle) {

    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsStatisticPresenterImpl(this, getActivity());
    }

    @Override
    protected void onAttachListener(Context activity) {
        if (activity instanceof TopAdsStatisticActivityViewListener) {
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
        contentTitleGraph = (TextView) view.findViewById(R.id.content_title_graph);
        contentGraph = (LineChartView) view.findViewById(R.id.content_graph);
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        contentTitleGraph.setText(getTitleGraph());
    }

    private void generateLineChart() {
        try {
            contentGraph.dismissAllTooltips();
            final List<Integer> indexToDisplay = calculateIndexToDisplay();


            if (topAdsBaseWilliamChartConfig == null) {
                topAdsBaseWilliamChartConfig = new TopAdsBaseWilliamChartConfig();
            }

            if (baseWilliamChartConfig == null) {
                baseWilliamChartConfig = new BaseWilliamChartConfig();
            }

            BaseWilliamChartModel baseWilliamChartModel =
                    new BaseWilliamChartModel(mLabels, mValues);


            contentGraph.addDataDisplayDots(mLabelDisplay);
            TooltipWithDynamicPointer tooltip = new TooltipWithDynamicPointer(getActivity(),
                    R.layout.item_tooltip_topads, R.id.tooltip_value, R.id.tooltip_title, R.id.tooltip_pointer);
            baseWilliamChartConfig
                    .reset()
                    .addBaseWilliamChartModels(baseWilliamChartModel, new GrossGraphDataSetConfig())
                    .setBasicGraphConfiguration(topAdsBaseWilliamChartConfig)
                    .setTooltip(tooltip, new TopAdsTooltipConfiguration())
                    .setxRendererListener(new XRenderer.XRendererListener() {
                        @Override
                        public boolean filterX(@IntRange(from = 0) int i) {
                            if (mValues != null) {
                                if (i == 0 || mValues.length - 1 == i)
                                    return true;

                                if (mValues.length <= 10) {
                                    return true;
                                }

                                return indexToDisplay.contains(i);
                            } else {
                                return true;
                            }
                        }
                    })
                    .setDotDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.oval_2_copy_6))
                    .buildChart(contentGraph);
        } catch (Exception e) {
            if (e != null && e.getMessage() != null) {
                Log.e("TopAdsStatisticFragment", e.getMessage());
            } else {
                Log.e("TopAdsStatisticFragment", "Null Pointer");
            }
        }
    }

    @NonNull
    private List<Integer> calculateIndexToDisplay() {
        //filter display dot at graph to avoid tight display graph
        final List<Integer> indexToDisplay = new ArrayList<>();
        int divider;
        if(mValues.length > 50){
            divider = 10;
        }else{
            divider = 5;
        }
        int divided = mValues.length / divider;
        for (int j = 1; j <= divided - 1; j++) {
            indexToDisplay.add((j * divider) - 1);
        }
        return indexToDisplay;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        this.cells = topAdsStatisticActivityViewListener.getDataCell();
        mLabels = generateLabels();
        mValues = generateValues();
        mLabelDisplay = generateLabelDisplay();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if (datePickerPresenter!= null && datePickerPresenter.isDateUpdated(startDate, endDate)) {
                startDate = datePickerPresenter.getStartDate();
                endDate = datePickerPresenter.getEndDate();
                loadData();
            }
        }
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void updateDataCell(List<Cell> cells) {
        this.cells = cells;
        mLabels = generateLabels();
        mValues = generateValues();
        mLabelDisplay = generateLabelDisplay();
        generateLineChart();
    }

    @Override
    public View getDateLabelView() {
        return dateLabelView;
    }

    @Override
    public void onResume() {
        startDate = null;
        endDate = null;
        super.onResume();
    }


    protected String[] generateLabels() {
        if (cells != null && cells.size() > 0) {
            String[] labels = new String[cells.size()];
            for (int i = 0; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                String label = getDate(cell);
                labels[i] = label;
            }
            return labels;
        } else {
            return null;
        }
    }

    private String getDate(Cell cell) {
        SimpleDateFormat formatterLabel = new SimpleDateFormat("dd MMM");
        return formatterLabel.format(cell.getDate());
    }

    protected float[] generateValues() {
        if (cells != null && cells.size() > 0) {
            float[] values = new float[cells.size()];
            for (int i = 0; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                float value = getValueData(cell);
                values[i] = value;
            }
            return values;
        } else {
            return null;
        }
    }


    private ArrayList<TooltipModel> generateLabelDisplay() {
        if (cells != null && cells.size() > 0) {
            ArrayList<TooltipModel> valuesDisplay = new ArrayList<>();
            for (int i = 0; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                String value = getValueDisplay(cell);
                String title = getDate(cell);
                valuesDisplay.add(new TooltipModel(title, value));
            }
            return valuesDisplay;
        } else {
            return null;
        }
    }

    protected abstract String getValueDisplay(Cell cell);

    protected abstract float getValueData(Cell cell);

    protected abstract String getTitleGraph();
}
