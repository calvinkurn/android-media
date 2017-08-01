package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.design.card.EmptyCardContentView;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.LineChartContainerWidget;
import com.tokopedia.seller.common.williamchart.Tools;
import com.tokopedia.seller.common.williamchart.view.LineChartView;
import com.tokopedia.seller.topads.dashboard.data.model.data.DataDeposit;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMTopAdsAmountViewHolder implements GMStatisticViewHolder {

    public interface OnTopAdsViewHolderListener {

        void onManageTopAdsClicked();
    }

    private TitleCardView titleCardView;
    private LineChartView gmStatisticTopAdsGraph;
    private LineChartContainerWidget gmTopAdsLineChartWidget;

    private String[] monthNamesAbrev;
    private OnTopAdsViewHolderListener onTopAdsViewHolderListener;

    public void setOnTopAdsViewHolderListener(OnTopAdsViewHolderListener onTopAdsViewHolderListener) {
        this.onTopAdsViewHolderListener = onTopAdsViewHolderListener;
    }

    public GMTopAdsAmountViewHolder(View view) {
        titleCardView = (TitleCardView) view.findViewById(R.id.topads_statistic_card_view);
        View vSeeTopAds = titleCardView.findViewById(R.id.tv_see_topads);
        gmStatisticTopAdsGraph = (LineChartView) view.findViewById(R.id.gm_statistic_topads_graph);
        gmTopAdsLineChartWidget = (LineChartContainerWidget) titleCardView.findViewById(R.id.topads_line_chart_container);
        monthNamesAbrev = view.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        vSeeTopAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTopAdsViewHolderListener != null) {
                    onTopAdsViewHolderListener.onManageTopAdsClicked();
                }
            }
        });
    }

    private void setTopAdsCardView(GMGraphViewModel gmGraphViewModel) {
        gmTopAdsLineChartWidget.setSubtitle(gmTopAdsLineChartWidget.getContext().getString(R.string.gm_statistic_top_ads_amount_subtitle_text));
        gmTopAdsLineChartWidget.setPercentage(gmGraphViewModel.percentage);
        gmTopAdsLineChartWidget.setAmount(KMNumbers.formatRupiahString(titleCardView.getContext(), gmGraphViewModel.amount));
    }

    public void bind(@Nullable GMGraphViewModel data) {
        setTopAdsCardView(data);
        BaseWilliamChartModel baseWilliamChartModel = GMStatisticUtil.joinDateAndGraph3(data.dates, data.values, monthNamesAbrev);
        // create model for chart
        BaseWilliamChartConfig baseWilliamChartConfig = Tools.getCommonWilliamChartConfig(gmStatisticTopAdsGraph, baseWilliamChartModel);
        baseWilliamChartConfig.buildChart(gmStatisticTopAdsGraph);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    public void bindTopAdsCreditNotUsed(@Nullable GMGraphViewModel data, DataDeposit dataDeposit) {
        titleCardView.setEmptyViewRes(R.layout.item_empty_gm_stat_topads);
        EmptyCardContentView emptyCardContentView = (EmptyCardContentView) titleCardView.getEmptyView().findViewById(R.id.empty_card_content_view);
        if (data.amount > 0) {
            emptyCardContentView.setContentText(emptyCardContentView.getContext().getString(R.string.gm_statistic_top_ads_empty_desc_credit_not_used, dataDeposit.getAmountFmt()));
        }
        emptyCardContentView.setActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTopAdsViewHolderListener != null) {
                    onTopAdsViewHolderListener.onManageTopAdsClicked();
                }
            }
        });
        setViewState(LoadingStateView.VIEW_EMPTY);
    }

    @Override
    public void setViewState(int state) {
        titleCardView.setViewState(state);
    }
}