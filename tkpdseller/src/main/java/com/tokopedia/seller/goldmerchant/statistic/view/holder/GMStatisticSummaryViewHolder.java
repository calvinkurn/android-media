package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.view.View;

import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.GmStatisticSummaryView;

/**
 * Created by nathan on 7/24/17.
 */

public class GMStatisticSummaryViewHolder implements GMStatisticViewHolder{

    private LoadingStateView successTransactionLoadingStateView;
    private LoadingStateView conversionLoadingStateView;
    private LoadingStateView productSeenLoadingStateView;
    private LoadingStateView productSoldLoadingStateView;
    private GmStatisticSummaryView successTransactionSummaryView;
    private GmStatisticSummaryView conversionSummaryView;
    private GmStatisticSummaryView productSeenSummaryView;
    private GmStatisticSummaryView productSoldSummaryView;
    private String errorValue;

    public GMStatisticSummaryViewHolder(View view) {
        errorValue = view.getContext().getString(R.string.label_empty_value);
        successTransactionLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_success_transaction);
        conversionLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_conversion);
        productSeenLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_product_seen);
        productSoldLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_product_sold);
        successTransactionSummaryView = (GmStatisticSummaryView) view.findViewById(R.id.summary_view_success_transaction);
        conversionSummaryView = (GmStatisticSummaryView) view.findViewById(R.id.summary_view_conversion);
        productSeenSummaryView = (GmStatisticSummaryView) view.findViewById(R.id.summary_view_product_seen);
        productSoldSummaryView = (GmStatisticSummaryView) view.findViewById(R.id.summary_view_product_sold);
    }

    public void setData(GetProductGraph getProductGraph) {
        successTransactionSummaryView.setContentText(KMNumbers.formatNumbers(getProductGraph.getSuccessTrans()));
        successTransactionSummaryView.setPercentage(getProductGraph.getDiffTrans());
        conversionSummaryView.setContentText(KMNumbers.formatToPercentString(
                conversionSummaryView.getContext(), getProductGraph.getConversionRate()));
        conversionSummaryView.setPercentage(getProductGraph.getDiffConv());
        productSeenSummaryView.setContentText(KMNumbers.formatNumbers(getProductGraph.getProductView()));
        productSeenSummaryView.setPercentage(getProductGraph.getDiffView());
        productSoldSummaryView.setContentText(KMNumbers.formatNumbers(getProductGraph.getProductSold()));
        productSoldSummaryView.setPercentage(getProductGraph.getDiffSold());
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    @Override
    public void setViewState(int state) {
        if (state == LoadingStateView.VIEW_LOADING) {
            successTransactionLoadingStateView.setViewState(state);
            conversionLoadingStateView.setViewState(state);
            productSeenLoadingStateView.setViewState(state);
            productSoldLoadingStateView.setViewState(state);
            return;
        }
        successTransactionLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        conversionLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        productSeenLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        productSoldLoadingStateView.setViewState(LoadingStateView.VIEW_CONTENT);
        switch (state) {
            case LoadingStateView.VIEW_ERROR:
            case LoadingStateView.VIEW_EMPTY:
                successTransactionSummaryView.setContentText(errorValue);
                successTransactionSummaryView.setNoDataPercentage();
                conversionSummaryView.setContentText(errorValue);
                conversionSummaryView.setNoDataPercentage();
                productSeenSummaryView.setContentText(errorValue);
                productSeenSummaryView.setNoDataPercentage();
                productSoldSummaryView.setContentText(errorValue);
                productSoldSummaryView.setNoDataPercentage();
        }
    }
}