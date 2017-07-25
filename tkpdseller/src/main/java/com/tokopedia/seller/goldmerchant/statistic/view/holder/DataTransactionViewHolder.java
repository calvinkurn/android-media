package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.Router;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.ArrowPercentageView;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.activity.GMStatisticTransactionActivity;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewWithPreviousModel;
import com.tokopedia.seller.lib.williamchart.util.DataTransactionChartConfig;
import com.tokopedia.seller.lib.williamchart.util.DataTransactionDataSetConfig;
import com.tokopedia.seller.lib.williamchart.util.EmptyDataTransactionDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created on 11/10/16.
 * @author normansyahputa
 */

public class DataTransactionViewHolder {
    private LineChartView transactionChart;

    private TitleCardView transactionDataCardView;
    private TextView tvTransactionCount;
    private boolean isGoldMerchant;
    private ArrowPercentageView arrowPercentageView;
    private View seeDetailView;

    private BaseWilliamChartConfig baseWilliamChartConfig;
    private DataTransactionChartConfig dataTransactionChartConfig;
    private String[] monthNamesAbrev;

    public DataTransactionViewHolder(TitleCardView transactionDataCardView, boolean isGoldMerchant) {
        baseWilliamChartConfig = new BaseWilliamChartConfig();

        this.transactionDataCardView = transactionDataCardView;
        this.isGoldMerchant = isGoldMerchant;

        monthNamesAbrev = transactionDataCardView.getContext().getResources()
                .getStringArray(R.array.lib_date_picker_month_entries);

        initView(transactionDataCardView);
    }

    public void setDataTransactionChartConfig(DataTransactionChartConfig dataTransactionChartConfig) {
        this.dataTransactionChartConfig = dataTransactionChartConfig;
    }

    public void moveToGMSubscribe() {
        Router.goToGMSubscribe(transactionDataCardView.getContext());
    }

    private void initView(final TitleCardView transactionDataCardView) {
        this.transactionDataCardView = transactionDataCardView;

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
            }
        });
    }

    public void setEmptyViewNoGM(){
        transactionDataCardView.setEmptyViewRes(R.layout.widget_transaction_data_empty_no_gm);
        transactionDataCardView.getEmptyView().findViewById(R.id.move_to_gmsubscribe)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToGMSubscribe();
                    }
                });
        transactionDataCardView.setViewState(LoadingStateView.VIEW_EMPTY);
    }

    protected void setEmptyStatePercentage() {
        arrowPercentageView.setNoDataPercentage();
    }

    public void displayGraphic(List<Integer> data, List<Integer> dateGraph, boolean emptyState) {

        BaseWilliamChartModel baseWilliamChartModel
                = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);
        baseWilliamChartConfig.reset();
        baseWilliamChartConfig.setBasicGraphConfiguration(dataTransactionChartConfig);

        if (emptyState) {
            baseWilliamChartConfig.addBaseWilliamChartModels(
                    baseWilliamChartModel, new EmptyDataTransactionDataSetConfig());
        } else {
            baseWilliamChartConfig.addBaseWilliamChartModels(
                    baseWilliamChartModel, new DataTransactionDataSetConfig());
        }
        baseWilliamChartConfig.buildChart(transactionChart);
    }

    public void bindData(GMGraphViewWithPreviousModel totalTransactionModel) {
        if (dataTransactionChartConfig == null)
            throw new RuntimeException("please pass configuration for graphic !!");

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
            return;
        }

        seeDetailView.setClickable(true);
        seeDetailView.setVisibility(View.VISIBLE);

        /* non empty state */
        tvTransactionCount.setText(KMNumbers.getFormattedString(totalTransactionModel.amount));

        Double diffSuccessTrans = totalTransactionModel.percentage;
        arrowPercentageView.setPercentage(diffSuccessTrans);

        displayGraphic(totalTransactionModel.values, totalTransactionModel.dates, false);
    }

    public void setViewState(int viewState) {
        transactionDataCardView.setViewState(viewState);
    }
}
