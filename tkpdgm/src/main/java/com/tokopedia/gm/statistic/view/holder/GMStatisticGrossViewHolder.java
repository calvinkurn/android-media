package com.tokopedia.gm.statistic.view.holder;

import android.view.View;

import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.R;
import com.tokopedia.seller.common.williamchart.base.BaseWilliamChartConfig;
import com.tokopedia.seller.common.williamchart.base.BaseWilliamChartModel;
import com.tokopedia.seller.common.williamchart.util.GMStatisticUtil;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.gm.statistic.view.model.GMGraphViewWithPreviousModel;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.gm.statistic.view.widget.LineChartContainerWidget;
import com.tokopedia.seller.common.williamchart.Tools;
import com.tokopedia.seller.common.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created by nathan on 7/24/17.
 */

public class GMStatisticGrossViewHolder implements GMStatisticViewHolder {

    private String[] monthNamesAbrev;

    private LoadingStateView grossLoadingStateView;
    private LineChartContainerWidget grossLineChartContainer;
    private LineChartView grossLineChartView;

    public GMStatisticGrossViewHolder(View view) {
        grossLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_gross);
        grossLineChartContainer = (LineChartContainerWidget) view.findViewById(R.id.line_chart_container_gross);
        grossLineChartView = (LineChartView) view.findViewById(R.id.line_chart_gross);
        monthNamesAbrev = view.getContext().getResources().getStringArray(R.array.lib_date_picker_month_entries);
    }

    public void setData(GMTransactionGraphMergeModel getTransactionGraph) {
        grossLineChartContainer.setAmount(
                KMNumbers.formatRupiahString(getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel.amount));
        grossLineChartContainer.setMainDate(getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel);
        GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel = getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel;
        showTransactionGraph(gmGraphViewWithPreviousModel.values, gmGraphViewWithPreviousModel.dates);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    /**
     * display two {@link BaseWilliamChartModel} models
     *
     * @param data
     * @param dateGraph
     */
    private void showTransactionGraph(List<Integer> data, List<Integer> dateGraph) {
        // create model for chart
        BaseWilliamChartModel baseWilliamChartModel = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);
        BaseWilliamChartConfig baseWilliamChartConfig = Tools.getCommonWilliamChartConfig(grossLineChartView, baseWilliamChartModel);
        baseWilliamChartConfig.buildChart(grossLineChartView);
    }

    @Override
    public void setViewState(int state) {
        grossLoadingStateView.setViewState(state);
    }
}