package com.tokopedia.seller.gmstat.views.helper;

import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.library.LoaderTextView;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class TransactionDataLoading {
    private final View parentView;

    private LoaderImageView dataTransactionHeaderLoadingIc;

    private LoaderTextView dataTransactionHeaderLoadingText;

    private LoaderTextView buyerNumberHeaderLoading;

    private LoaderTextView transactionCountLoading;

    private LoaderImageView transactionCountIconLoading;

    private LoaderImageView trasactionChartLoading;

    public TransactionDataLoading(View itemView) {
        initView(itemView);
        parentView = itemView.findViewById(R.id.transaction_data_loading);

        dataTransactionHeaderLoadingIc.resetLoader();
        dataTransactionHeaderLoadingText.resetLoader();
        buyerNumberHeaderLoading.resetLoader();
        transactionCountLoading.resetLoader();
        transactionCountIconLoading.resetLoader();
        trasactionChartLoading.resetLoader();
    }

    private void initView(View itemView) {
        dataTransactionHeaderLoadingIc = (LoaderImageView) itemView.findViewById(R.id.data_transaction_header_loading_ic);

        dataTransactionHeaderLoadingText = (LoaderTextView) itemView.findViewById(R.id.data_transaction_header_loading_text);

        buyerNumberHeaderLoading = (LoaderTextView) itemView.findViewById(R.id.buyer_number_header_loading);

        transactionCountLoading = (LoaderTextView) itemView.findViewById(R.id.transaction_count_loading);

        transactionCountIconLoading = (LoaderImageView) itemView.findViewById(R.id.transaction_count_icon_loading);

        trasactionChartLoading = (LoaderImageView) itemView.findViewById(R.id.transaction_chart_loading);
    }

    public void displayLoading() {
        parentView.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        parentView.setVisibility(View.GONE);
    }
}
