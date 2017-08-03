package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.Router;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.view.activity.GMStatisticTransactionActivity;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewWithPreviousModel;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.ArrowPercentageView;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.DataTransactionChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.DataTransactionDataSetConfig;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.EmptyDataTransactionDataSetConfig;
import com.tokopedia.seller.common.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created on 11/10/16.
 *
 * @author normansyahputa
 */

public class GMStatisticTransactionViewHolder implements GMStatisticViewHolder {
    private LineChartView transactionChart;

    private TitleCardView transactionDataCardView;
    private TextView tvTransactionCount;
    private ArrowPercentageView arrowPercentageView;
    private View seeDetailView;

    private String[] monthNamesAbrev;

    public GMStatisticTransactionViewHolder(View view) {
        transactionDataCardView = (TitleCardView) view.findViewById(R.id.transaction_data_card_view);
        transactionChart = (LineChartView) transactionDataCardView.findViewById(R.id.transaction_chart);
        tvTransactionCount = (TextView) transactionDataCardView.findViewById(R.id.tv_transaction_count);
        arrowPercentageView = (ArrowPercentageView) transactionDataCardView.findViewById(R.id.view_arrow_percentage);
        seeDetailView = transactionDataCardView.findViewById(R.id.see_detail_container);
        seeDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = transactionDataCardView.getContext();
                // force to move to new statistic
                Intent intent = new Intent(context, GMStatisticTransactionActivity.class);
                context.startActivity(intent);

                UnifyTracking.eventClickGMStatSeeDetailTransaction();
            }
        });
        monthNamesAbrev = transactionDataCardView.getContext().getResources()
                .getStringArray(R.array.lib_date_picker_month_entries);
    }

    public void bindData(GMGraphViewWithPreviousModel totalTransactionModel, boolean isGoldMerchant) {
        /* non gold merchant */
        if (!isGoldMerchant) {
            setEmptyViewNoGM();
            setEmptyStatePercentage();
            // to make the content overlay
            transactionDataCardView.getContentView().setVisibility(View.VISIBLE);
            seeDetailView.setClickable(false);
            return;
        }

        /* empty state */
        if (totalTransactionModel.values == null || totalTransactionModel.amount == 0) {

            setEmptyStatePercentage();

            displayGraphic(totalTransactionModel.values, totalTransactionModel.dates, true);
            seeDetailView.setVisibility(View.INVISIBLE);
            setViewState(LoadingStateView.VIEW_CONTENT);
            return;
        }

        setViewState(LoadingStateView.VIEW_CONTENT);

        seeDetailView.setClickable(true);
        seeDetailView.setVisibility(View.VISIBLE);

        /* non empty state */
        tvTransactionCount.setText(KMNumbers.getSummaryString(totalTransactionModel.amount));

        Double diffSuccessTrans = totalTransactionModel.percentage;
        arrowPercentageView.setPercentage(diffSuccessTrans);

        displayGraphic(totalTransactionModel.values, totalTransactionModel.dates, false);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    private void setEmptyViewNoGM() {
        transactionDataCardView.setEmptyViewRes(R.layout.partial_gm_statistic_transaction_empty_state_not_gm);
        transactionDataCardView.getEmptyView().findViewById(R.id.move_to_gmsubscribe)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UnifyTracking.eventClickGMStatBuyGMDetailTransaction();
                        Router.goToGMSubscribe(transactionDataCardView.getContext());
                    }
                });
        transactionDataCardView.setViewState(LoadingStateView.VIEW_EMPTY);
    }

    private void setEmptyStatePercentage() {
        arrowPercentageView.setNoDataPercentage();
    }

    private void displayGraphic(List<Integer> data, List<Integer> dateGraph, boolean emptyState) {
        BaseWilliamChartModel baseWilliamChartModel
                = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);
        BaseWilliamChartConfig baseWilliamChartConfig = new BaseWilliamChartConfig();
        baseWilliamChartConfig.setBasicGraphConfiguration(new DataTransactionChartConfig(transactionChart.getContext()));
        if (emptyState) {
            baseWilliamChartConfig.addBaseWilliamChartModels(
                    baseWilliamChartModel, new EmptyDataTransactionDataSetConfig());
        } else {
            baseWilliamChartConfig.addBaseWilliamChartModels(
                    baseWilliamChartModel, new DataTransactionDataSetConfig());
        }
        baseWilliamChartConfig.buildChart(transactionChart);
    }

    @Override
    public void setViewState(int viewState) {
        transactionDataCardView.setViewState(viewState);
    }
}
