package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.data.model.data.Cell;
import com.tokopedia.seller.topads.view.presenter.TopAdsStatisticPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsStatisticPresenterImpl;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphChartConfig;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticActivityViewListener;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticViewListener;

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
    private GrossGraphChartConfig grossGraphChartConfig;
    private List<Cell> cells;
    private String[] mLabels;
    private ArrayList<String> mLabelDisplay = new ArrayList<>();
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

            //inflating layout tooltip
            @LayoutRes int layoutTooltip = R.layout.gm_stat_tooltip_lolipop;
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP) {
                layoutTooltip = R.layout.gm_stat_tooltip;
            }

            if (grossGraphChartConfig == null) {
                grossGraphChartConfig = new GrossGraphChartConfig(mLabels, mValues);
            }
            contentGraph.addDataDisplayDots(mLabelDisplay);
            Tooltip tooltip = new Tooltip(getActivity(),
                    layoutTooltip,
                    R.id.gm_stat_tooltip_textview);
            grossGraphChartConfig
                    .setmLabels(mLabels)
                    .setmValues(mValues, new XRenderer.XRendererListener() {
                        @Override
                        public boolean filterX(@IntRange(from = 0L) int i) {
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
                    .setTooltip(tooltip)
                    .buildChart(grossGraphChartConfig.buildLineChart(contentGraph));
        } catch (Exception e) {
            if (e != null && e.getMessage() != null) {
                Log.e("TopAdsStatisticFragment", e.getMessage());
            } else {
                Log.e("TopAdsStatisticFragment", "Null Pointer");
            }
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
                SimpleDateFormat formatterLabel = new SimpleDateFormat("dd MMM");
                String label = formatterLabel.format(cell.getDate());
                labels[i] = label;
            }
            return labels;
        } else {
            return null;
        }
    }

    ;

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


    private ArrayList<String> generateLabelDisplay() {
        if (cells != null && cells.size() > 0) {
            ArrayList<String> valuesDisplay = new ArrayList<>();
            for (int i = 0; i < cells.size(); i++) {
                Cell cell = cells.get(i);
                String value = getValueDisplay(cell);
                valuesDisplay.add(value);
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
