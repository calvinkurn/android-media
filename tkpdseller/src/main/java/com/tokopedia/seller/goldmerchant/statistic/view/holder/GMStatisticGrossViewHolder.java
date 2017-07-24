package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.widget.LineChartContainerWidget;
import com.tokopedia.seller.gmstat.views.widget.LoadingStateView;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.GmStatisticSummaryView;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

/**
 * Created by nathan on 7/24/17.
 */

public class GMStatisticGrossViewHolder {

    private LoadingStateView grossLoadingStateView;
    private LineChartContainerWidget grossLineChartContainer;
    private LineChartView grossLineChartView;

    public GMStatisticGrossViewHolder(View view) {
        grossLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_gross);
        grossLineChartContainer = (LineChartContainerWidget) view.findViewById(R.id.line_chart_container_gross);
        grossLineChartView = (LineChartView) view.findViewById(R.id.line_chart_gross);
        grossLoadingStateView.setLoadingState(true);
    }

    public void setViewState(int state) {
        grossLoadingStateView.setViewState(state);
    }

    public void setData(GMTransactionGraphMergeModel getTransactionGraph) {
        grossLineChartContainer.setSubtitle(grossLineChartContainer.getContext().getString(R.string.gm_statistic_dashboard_gross));
        grossLineChartContainer.setAmount(String.valueOf(getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel.amount));
        grossLineChartContainer.setMainDate(getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }
}