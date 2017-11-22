package com.tokopedia.gm.statistic.view.holder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.R;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.common.williamchart.Tools;
import com.tokopedia.seller.common.williamchart.model.TooltipModel;
import com.tokopedia.seller.common.williamchart.renderer.TooltipFormatRenderer;
import com.tokopedia.seller.common.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.common.williamchart.util.TooltipConfiguration;
import com.tokopedia.seller.common.williamchart.view.LineChartView;
import com.tokopedia.gm.statistic.constant.GMTransactionGraphType;
import com.tokopedia.seller.common.williamchart.base.BaseWilliamChartConfig;
import com.tokopedia.seller.common.williamchart.base.BaseWilliamChartModel;
import com.tokopedia.seller.common.williamchart.util.GMStatisticUtil;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.seller.common.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.gm.statistic.view.model.GMGraphViewWithPreviousModel;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphViewModel;
import com.tokopedia.gm.statistic.view.widget.LineChartContainerWidget;
import com.tokopedia.gm.statistic.view.widget.config.EmptyDataTransactionDataSetConfig;
import com.tokopedia.seller.common.williamchart.config.GrossGraphDataSetConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMTransactionGraphViewHolder implements GMStatisticViewHolder {

    public static final int WIDTH_TOOLTIP_COMPARE = 40;
    public static final int HEIGHT_TOOLTIP_COMPARE = 32;
    private final String[] gmStatTransactionEntries;
    private final boolean[] selections;
    private TitleCardView gmTitleCardView;
    private LineChartView gmStatisticIncomeGraph;
    private LineChartContainerWidget gmLineChartContainer;
    private int gmStatGraphSelection;
    private String[] monthNamesAbrev;

    private GMTransactionGraphViewModel gmTransactionGraphViewModel;
    private boolean compareDate;

    private ArrayList<TooltipModel> tooltipModels;

    public GMTransactionGraphViewHolder(View view) {
        gmTitleCardView = (TitleCardView) view.findViewById(R.id.gold_merchant_statistic_card_view);
        gmTitleCardView.setOnArrowDownClickListener(new TitleCardView.OnArrowDownClickListener() {
            @Override
            public void onArrowDownClicked() {
                if (gmTransactionGraphViewModel == null) {
                    return;
                }
                showGraphSelectionDialog();
            }
        });
        gmLineChartContainer = (LineChartContainerWidget) gmTitleCardView.findViewById(R.id.gold_merchant_line_chart_container);
        gmStatisticIncomeGraph = (LineChartView) view.findViewById(R.id.gm_statistic_transaction_income_graph);

        gmStatGraphSelection = 0;
        gmStatTransactionEntries = view.getResources().getStringArray(R.array.lib_gm_stat_transaction_entries);
        selections = new boolean[gmStatTransactionEntries.length];
        selections[gmStatGraphSelection] = true;
        monthNamesAbrev = view.getResources().getStringArray(R.array.lib_date_picker_month_entries);
    }

    public void bind(@Nullable GMTransactionGraphViewModel gmTransactionGraphViewModel, boolean compareDate) {
        this.compareDate = compareDate;
        this.gmTransactionGraphViewModel = gmTransactionGraphViewModel;
        switch (selection()) {
            case GMTransactionGraphType.GROSS_REVENUE:
                bindForSelection(gmTransactionGraphViewModel.grossRevenueModel);
                break;
            case GMTransactionGraphType.NET_REVENUE:
                bindForSelection(gmTransactionGraphViewModel.netRevenueModel);
                break;
            case GMTransactionGraphType.REJECT_TRANS:
                bindForSelection(gmTransactionGraphViewModel.rejectTransactionModel);
                break;
            case GMTransactionGraphType.REJECTED_AMOUNT:
                bindForSelection(gmTransactionGraphViewModel.rejectedAmountModel);
                break;
            case GMTransactionGraphType.SHIPPING_COST:
                bindForSelection(gmTransactionGraphViewModel.shippingCostModel);
                break;
            case GMTransactionGraphType.SUCCESS_TRANS:
                bindForSelection(gmTransactionGraphViewModel.successTransactionModel);
                break;
            case GMTransactionGraphType.TOTAL_TRANSACTION:
                bindForSelection(gmTransactionGraphViewModel.totalTransactionModel);
                break;
        }
    }

    private void showGraphSelectionDialog() {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(gmTitleCardView.getContext())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(gmTitleCardView.getContext().getString(R.string.gm_statistic_summary_text));

        for (int i = 0; i < gmStatTransactionEntries.length; i++) {
            if (bottomSheetBuilder instanceof CheckedBottomSheetBuilder) {
                ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(i, gmStatTransactionEntries[i], null, selections[i]);
            } else {
                bottomSheetBuilder.addItem(i, gmStatTransactionEntries[i], null);
            }
        }

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        String itemTitle = item.getTitle().toString();

                        UnifyTracking.eventClickGMStatFilterNameTransaction(itemTitle);

                        gmStatGraphSelection = GMStatisticUtil.findSelection(gmStatTransactionEntries, itemTitle);
                        resetSelection(gmStatGraphSelection);
                        bind(gmTransactionGraphViewModel, compareDate);
                    }
                })
                .createDialog();
        bottomSheetDialog.show();
    }

    private void resetSelection(int newSelection) {
        for (int i = 0; i < selections.length; i++) {
            selections[i] = i == newSelection;
        }
    }

    public int selection() {
        return gmStatGraphSelection;
    }

    private void bindForSelection(@Nullable GMGraphViewWithPreviousModel data) {
        setHeaderValue(data);
        if (compareDate) {
            gmLineChartContainer.setPercentage(data.percentage);
            gmLineChartContainer.setCompareDate(data);
            gmLineChartContainer.setMainDate(data);
            // create model for current Data
            final BaseWilliamChartModel baseWilliamChartModel
                    = GMStatisticUtil.joinDateAndGraph3(data.dates, data.values, monthNamesAbrev);

            // create model for previous Data
            final BaseWilliamChartModel previousbaseWilliamChartModel
                    = GMStatisticUtil.joinDateAndGraph3(data.dates, data.pValues, monthNamesAbrev);

            ArrayList<BaseWilliamChartModel> baseWilliamChartModels = new ArrayList<>();
            baseWilliamChartModels.add(previousbaseWilliamChartModel);
            baseWilliamChartModels.add(baseWilliamChartModel);

            showTransactionGraph(baseWilliamChartModels);
        } else {
            gmLineChartContainer.hidePercentageView();
            fillTransactionGraphMain(data);
        }
    }

    private void fillTransactionGraphMain(@Nullable GMGraphViewWithPreviousModel data) {
        gmLineChartContainer.setMainDate(data);
        gmLineChartContainer.setCompareDate(null);
        showTransactionGraph(data.values, data.dates);
    }

    private void setHeaderValue(GMGraphViewWithPreviousModel data) {
        gmTitleCardView.setTitle(gmStatTransactionEntries[gmStatGraphSelection]);

        switch (selection()) {
            case GMTransactionGraphType.GROSS_REVENUE:
            case GMTransactionGraphType.NET_REVENUE:
            case GMTransactionGraphType.REJECTED_AMOUNT:
            case GMTransactionGraphType.SHIPPING_COST:
                gmLineChartContainer.setAmount(KMNumbers.formatRupiahString(data.amount));
                break;
            case GMTransactionGraphType.REJECT_TRANS:
            case GMTransactionGraphType.SUCCESS_TRANS:
            case GMTransactionGraphType.TOTAL_TRANSACTION:
                gmLineChartContainer.setAmount(Long.toString(data.amount));
                break;
        }
    }

    private void showTransactionGraph(List<BaseWilliamChartModel> baseWilliamChartModels) {
        tooltipModels = joinTooltipData(baseWilliamChartModels.get(1).getValues(), baseWilliamChartModels.get(0).getValues());
        gmStatisticIncomeGraph.addDataDisplayDots(tooltipModels);
        BaseWilliamChartConfig baseWilliamChartConfig = Tools.getCommonWilliamChartConfig(gmStatisticIncomeGraph, baseWilliamChartModels.get(0),
                new EmptyDataTransactionDataSetConfig(), getTooltip(gmStatisticIncomeGraph.getContext(), getTooltipResLayout()), getTooltipConfiguration());
        baseWilliamChartConfig.addBaseWilliamChartModels(baseWilliamChartModels.get(1), new GrossGraphDataSetConfig());
        baseWilliamChartConfig.buildChart(gmStatisticIncomeGraph);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    private TooltipConfiguration getTooltipConfiguration() {
        return new TooltipConfiguration() {
            @Override
            public int width() {
                return (int) Tools.fromDpToPx(WIDTH_TOOLTIP_COMPARE);
            }

            @Override
            public int height() {
                return (int) Tools.fromDpToPx(HEIGHT_TOOLTIP_COMPARE);
            }
        };
    }

    private ArrayList<TooltipModel> joinTooltipData(float[] values, float[] values1) {
        ArrayList<TooltipModel> tooltipModels = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            tooltipModels.add(new TooltipModel("", String.format("%d,%d", (int) values[i], (int) values1[i])));
        }
        return tooltipModels;
    }

    private Tooltip getTooltip(Context context, @LayoutRes int layoutRes) {
        return new Tooltip(context,
                layoutRes,
                R.id.gm_stat_tooltip_textview,
                new TooltipFormatRenderer() {
                    @Override
                    public void formatValue(List<TextView> textViews, TooltipModel tooltipModel) {
                        String[] value = tooltipModels.get(tooltipModel.getPosition()).getValue().split(",");
                        textViews.get(0).setText(KMNumbers.formatSuffixNumbers(Float.valueOf(value[0])));
                        textViews.get(1).setText(KMNumbers.formatSuffixNumbers(Float.valueOf(value[1])));
                    }
                }, true);
    }


    @LayoutRes
    private int getTooltipResLayout() {
        return R.layout.item_gm_statistic_tooltip_compare;
    }

    /**
     * display two {@link BaseWilliamChartModel} models
     *
     * @param data
     * @param dateGraph
     */
    private void showTransactionGraph(List<Integer> data, List<Integer> dateGraph) {
        BaseWilliamChartModel baseWilliamChartModel = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);
        gmStatisticIncomeGraph.clearDataDisplayDots();
        BaseWilliamChartConfig baseWilliamChartConfig = Tools.getCommonWilliamChartConfig(gmStatisticIncomeGraph, baseWilliamChartModel);
        baseWilliamChartConfig.buildChart(gmStatisticIncomeGraph);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    @Override
    public void setViewState(int state) {
        gmTitleCardView.setViewState(state);
        if (state == LoadingStateView.VIEW_ERROR) {
            gmTransactionGraphViewModel = null;
        }
    }
}