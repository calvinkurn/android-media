package com.tokopedia.seller.topads.dashboard.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.common.williamchart.model.TooltipModel;
import com.tokopedia.seller.common.williamchart.renderer.XRenderer;
import com.tokopedia.seller.common.williamchart.tooltip.TooltipWithDynamicPointer;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.GrossGraphDataSetConfig;
import com.tokopedia.seller.common.williamchart.util.TopAdsBaseWilliamChartConfig;
import com.tokopedia.seller.common.williamchart.util.TopAdsTooltipConfiguration;
import com.tokopedia.seller.common.williamchart.view.LineChartView;
import com.tokopedia.seller.topads.dashboard.data.model.data.Cell;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsStatisticActivityViewListener;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsStatisticViewListener;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsStatisticPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsStatisticPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsStatisticFragment extends BasePresenterFragment<TopAdsStatisticPresenter> implements TopAdsStatisticViewListener {

    TextView contentTitleGraph;
    LineChartView contentGraph;

    TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener;
    private TopAdsBaseWilliamChartConfig topAdsBaseWilliamChartConfig;
    private BaseWilliamChartConfig baseWilliamChartConfig;
    private List<Cell> cells;
    private String[] mLabels;
    private ArrayList<TooltipModel> mLabelDisplay = new ArrayList<>();
    private float[] mValues;

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
        presenter = new TopAdsStatisticPresenterImpl(this, getActivity());
    }

    @Override
    protected void initialListener(Activity activity) {
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
        contentTitleGraph.setText(getTitleGraph());
    }

    private void generateLineChart() {
        try {
            contentGraph.dismissAllTooltips();
            //filter display dot at graph to avoid tight display graph
            final List<Integer> indexToDisplay = new ArrayList<>();
            int divide = mValues.length / 10;
            for (int j = 1; j <= divide - 1; j++) {
                indexToDisplay.add((j * 10) - 1);
            }

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
                    .addBaseWilliamChartModels(baseWilliamChartModel, new GrossGraphDataSetConfig())
                    .setBasicGraphConfiguration(topAdsBaseWilliamChartConfig)
                    .setTooltip(tooltip, new TopAdsTooltipConfiguration())
                    .setxRendererListener(new XRenderer.XRendererListener() {
                        @Override
                        public boolean filterX(@IntRange(from = 0) int i) {
                            if (mValues != null) {
                                if (i == 0 || mValues.length - 1 == i)
                                    return true;

                                if (mValues.length <= 15) {
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

        }
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
    public void onResume() {
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
