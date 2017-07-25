package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.v4.content.res.ResourcesCompat;
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
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.util.DefaultTooltipConfiguration;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphChartConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created by nathan on 7/24/17.
 */

public class GMStatisticGrossViewHolder {

    private BaseWilliamChartConfig baseWilliamChartConfig;
    private String[] monthNamesAbrev;
    private Drawable oval2Copy6;

    private LoadingStateView grossLoadingStateView;
    private LineChartContainerWidget grossLineChartContainer;
    private LineChartView grossLineChartView;

    public GMStatisticGrossViewHolder(View view) {
        grossLoadingStateView = (LoadingStateView) view.findViewById(R.id.loading_state_view_gross);
        grossLineChartContainer = (LineChartContainerWidget) view.findViewById(R.id.line_chart_container_gross);
        grossLineChartView = (LineChartView) view.findViewById(R.id.line_chart_gross);

        monthNamesAbrev = view.getContext().getResources().getStringArray(R.array.lib_date_picker_month_entries);
        baseWilliamChartConfig = new BaseWilliamChartConfig();
        oval2Copy6 = ResourcesCompat.getDrawable(view.getContext().getResources(), R.drawable.oval_2_copy_6, null);
    }

    public void setViewState(int state) {
        grossLoadingStateView.setViewState(state);
    }

    public void setData(GMTransactionGraphMergeModel getTransactionGraph) {
        grossLineChartContainer.setSubtitle(grossLineChartContainer.getContext().getString(R.string.gm_statistic_dashboard_gross));
        grossLineChartContainer.setAmount(String.valueOf(getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel.amount));
        grossLineChartContainer.setMainDate(getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel);
        GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel = getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel;
        showTransactionGraph(gmGraphViewWithPreviousModel.values, gmGraphViewWithPreviousModel.pDates);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    /**
     * display two {@link BaseWilliamChartModel} models
     *
     * @param data
     * @param dateGraph
     */
    protected void showTransactionGraph(List<Integer> data, List<Integer> dateGraph) {
        // create model for chart
        final BaseWilliamChartModel baseWilliamChartModel
                = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);

        // resize linechart according to data
//        if (context != null && context instanceof Activity) {
//            GMStatisticUtil.resizeChart(baseWilliamChartModel.size(), grossLineChartView, (Activity) context);
//        }

        // get index to display
        final List<Integer> indexToDisplay = GMStatisticUtil.indexToDisplay(baseWilliamChartModel.getValues());

        // get tooltip
        Tooltip tooltip = getTooltip(
                grossLineChartView.getContext(),
                getTooltipResLayout()
        );

        baseWilliamChartConfig
                .reset()
                .addBaseWilliamChartModels(baseWilliamChartModel, new GrossGraphDataSetConfig())
                .setBasicGraphConfiguration(new GrossGraphChartConfig())
                .setDotDrawable(oval2Copy6)
                .setTooltip(tooltip, new DefaultTooltipConfiguration())
                .setxRendererListener(new XRenderer.XRendererListener() {
                    @Override
                    public boolean filterX(@IntRange(from = 0L) int i) {
                        if (i == 0 || baseWilliamChartModel.getValues().length - 1 == i)
                            return true;

                        if (baseWilliamChartModel.getValues().length <= 15) {
                            return true;
                        }

                        return indexToDisplay.contains(i);

                    }
                }).buildChart(grossLineChartView);
    }

    private Tooltip getTooltip(Context context, @LayoutRes int layoutRes) {
        return new Tooltip(context,
                layoutRes,
                R.id.gm_stat_tooltip_textview,
                new StringFormatRenderer() {
                    @Override
                    public String formatString(String s) {
                        return KMNumbers.formatNumbers(Float.valueOf(s));
                    }
                });
    }


    @LayoutRes
    private int getTooltipResLayout() {
        @LayoutRes int layoutTooltip = R.layout.gm_stat_tooltip_lollipop;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP) {
            layoutTooltip = R.layout.gm_stat_tooltip;
        }
        return layoutTooltip;
    }
}