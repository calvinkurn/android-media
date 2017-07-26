package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.app.Activity;
import android.view.View;

import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewWithPreviousModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.LineChartContainerWidget;
import com.tokopedia.seller.lib.williamchart.Tools;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created by nathan on 7/24/17.
 */

public class GMStatisticGrossViewHolder {

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

    public void setViewState(int state) {
        grossLoadingStateView.setViewState(state);
    }

    public void setData(Activity activity, GMTransactionGraphMergeModel getTransactionGraph) {
        grossLineChartContainer.setAmount(
                KMNumbers.formatRupiahString(grossLineChartContainer.getContext(),
                getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel.amount));
        grossLineChartContainer.setMainDate(getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel);
        GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel = getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel;
        showTransactionGraph(activity, gmGraphViewWithPreviousModel.values, gmGraphViewWithPreviousModel.dates);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    /**
     * display two {@link BaseWilliamChartModel} models
     *
     * @param data
     * @param dateGraph
     */
    private void showTransactionGraph(Activity activity, List<Integer> data, List<Integer> dateGraph) {
        // create model for chart
        BaseWilliamChartModel baseWilliamChartModel = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);
        BaseWilliamChartConfig baseWilliamChartConfig = Tools.getCommonWilliamChartConfig(activity, grossLineChartView, baseWilliamChartModel);
        baseWilliamChartConfig.buildChart(grossLineChartView);
    }
}