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

public class GMStatisticSummaryViewHolder {

    private LoadingStateView successTransactionLoadingStateView;
    private LoadingStateView conversionLoadingStateView;
    private LoadingStateView productSeenLoadingStateView;
    private LoadingStateView productSoldLoadingStateView;
    private GmStatisticSummaryView successTransactionSummaryView;
    private GmStatisticSummaryView conversionSummaryView;
    private GmStatisticSummaryView productSeenSummaryView;
    private GmStatisticSummaryView productSoldSummaryView;

    public GMStatisticSummaryViewHolder(View view) {
        successTransactionLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_success_transaction);
        conversionLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_conversion);
        productSeenLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_product_seen);
        productSoldLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_product_sold);
        successTransactionSummaryView = (GmStatisticSummaryView) view.findViewById(R.id.summary_view_success_transaction);
        conversionSummaryView = (GmStatisticSummaryView) view.findViewById(R.id.summary_view_conversion);
        productSeenSummaryView = (GmStatisticSummaryView) view.findViewById(R.id.summary_view_product_seen);
        productSoldSummaryView = (GmStatisticSummaryView) view.findViewById(R.id.summary_view_product_sold);
        successTransactionLoadingStateView.setLoadingState(true);
        conversionLoadingStateView.setLoadingState(true);
        productSeenLoadingStateView.setLoadingState(true);
        productSoldLoadingStateView.setLoadingState(true);
    }

    public void setViewState(int state) {
        successTransactionLoadingStateView.setViewState(state);
        conversionLoadingStateView.setViewState(state);
        productSeenLoadingStateView.setViewState(state);
        productSoldLoadingStateView.setViewState(state);
    }

    public void setData(GetProductGraph getProductGraph) {
        successTransactionSummaryView.setContentText(String.valueOf(getProductGraph.getSuccessTrans()));
        successTransactionSummaryView.setPercentage(getProductGraph.getDiffTrans());
        conversionSummaryView.setContentText(String.valueOf(getProductGraph.getConversionRate()));
        conversionSummaryView.setPercentage(getProductGraph.getDiffConv());
        productSeenSummaryView.setContentText(String.valueOf(getProductGraph.getProductView()));
        productSeenSummaryView.setPercentage(getProductGraph.getDiffView());
        productSoldSummaryView.setContentText(String.valueOf(getProductGraph.getProductSold()));
        productSoldSummaryView.setPercentage(getProductGraph.getDiffSold());
        setViewState(LoadingStateView.VIEW_CONTENT);
    }
}