package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionGraphType;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.builder.CheckedBottomSheetBuilder;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.BaseGMViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMPercentageViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewWithPreviousModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.LineChartContainerWidget;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.EmptyDataTransactionDataSetConfig;
import com.tokopedia.seller.lib.williamchart.Tools;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.util.DefaultTooltipConfiguration;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;
import com.tokopedia.tkpdlib.bottomsheetbuilder.BottomSheetBuilder;
import com.tokopedia.tkpdlib.bottomsheetbuilder.adapter.BottomSheetItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMTransactionGraphViewHolder extends BaseGMViewHelper<GMTransactionGraphViewModel> {

    private final String[] gmStatTransactionEntries;
    private final boolean[] selections;
    private int gmStatGraphSelection;

    private LineChartView gmStatisticIncomeGraph;
    private String[] monthNamesAbrev;
    private Drawable oval2Copy6;

    private BaseWilliamChartConfig baseWilliamChartConfig;
    private TitleCardView gmTitleCardView;
    private LineChartContainerWidget gmLineChartContainer;

    private boolean showingSimpleDialog;
    private GMTransactionGraphViewModel data;

    public GMTransactionGraphViewHolder(@Nullable Context context) {
        super(context);

        gmStatGraphSelection = 0;
        gmStatTransactionEntries = context.getResources().getStringArray(R.array.lib_gm_stat_transaction_entries);
        selections = new boolean[gmStatTransactionEntries.length];
        selections[gmStatGraphSelection] = true;

        monthNamesAbrev = context.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        baseWilliamChartConfig = new BaseWilliamChartConfig();
        oval2Copy6 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.oval_2_copy_6, null);
    }

    @Override
    public void initView(@Nullable View itemView) {
        gmTitleCardView = (TitleCardView) itemView.findViewById(R.id.gold_merchant_statistic_card_view);
        gmTitleCardView.setOnArrowDownClickListener(new TitleCardView.OnArrowDownClickListener() {
            @Override
            public void onArrowDownClicked() {
                showGraphSelectionDialog();
            }
        });
        gmLineChartContainer = (LineChartContainerWidget) gmTitleCardView.findViewById(R.id.gold_merchant_line_chart_container);
        gmLineChartContainer.setPercentageUtil(new GMPercentageViewHelper(context));
        gmStatisticIncomeGraph = (LineChartView) itemView.findViewById(R.id.gm_statistic_transaction_income_graph);

        setViewState(LoadingStateView.VIEW_LOADING);
    }

    @Override
    public void bind(@Nullable GMTransactionGraphViewModel data) {
        this.data = data;

        switch (selection()) {
            case GMTransactionGraphType.GROSS_REVENUE:
                bind(data.grossRevenueModel);
                break;
            case GMTransactionGraphType.NET_REVENUE:
                bind(data.netRevenueModel);
                break;
            case GMTransactionGraphType.REJECT_TRANS:
                bind(data.rejectTransactionModel);
                break;
            case GMTransactionGraphType.REJECTED_AMOUNT:
                bind(data.rejectedAmountModel);
                break;
            case GMTransactionGraphType.SHIPPING_COST:
                bind(data.shippingCostModel);
                break;
            case GMTransactionGraphType.SUCCESS_TRANS:
                bind(data.successTransactionModel);
                break;
            case GMTransactionGraphType.TOTAL_TRANSACTION:
                bind(data.totalTransactionModel);
                break;
        }
    }

    public void setViewState(int state) {
        gmTitleCardView.setViewState(state);
    }

    private void showGraphSelectionDialog() {
        showingSimpleDialog = true;
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(context)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(context.getString(R.string.gold_merchant_transaction_summary_text));

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
                        gmStatGraphSelection = GMStatisticUtil.findSelection(gmStatTransactionEntries, item.getTitle().toString());
                        Log.d("Item click", item.getTitle() + " findSelection : " + gmStatGraphSelection);
                        resetSelection(gmStatGraphSelection);
                        GMTransactionGraphViewHolder.this.bind(data);
                        showingSimpleDialog = false;
                    }
                })
                .createDialog();
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                showingSimpleDialog = false;
            }
        });
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

    private void bind(@Nullable GMGraphViewWithPreviousModel data) {
        setHeaderValue(data);
        if (data.isCompare) {
            gmLineChartContainer.setCompareDate(data);
            gmLineChartContainer.setMainDate(data);
            // create model for current Data
            final BaseWilliamChartModel baseWilliamChartModel
                    = GMStatisticUtil.joinDateAndGraph3(data.dates, data.values, monthNamesAbrev);

            // create model for previous Data
            final BaseWilliamChartModel previousbaseWilliamChartModel
                    = GMStatisticUtil.joinDateAndGraph3(data.pDates, data.pValues, monthNamesAbrev);

            ArrayList<BaseWilliamChartModel> baseWilliamChartModels = new ArrayList<>();
            baseWilliamChartModels.add(baseWilliamChartModel);
            baseWilliamChartModels.add(previousbaseWilliamChartModel);

            showTransactionGraph(baseWilliamChartModels);
        } else {
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

        gmLineChartContainer.setPercentage(data.percentage);

        switch (selection()) {
            case GMTransactionGraphType.GROSS_REVENUE:
            case GMTransactionGraphType.NET_REVENUE:
            case GMTransactionGraphType.REJECTED_AMOUNT:
            case GMTransactionGraphType.SHIPPING_COST:
                gmLineChartContainer.setAmount(KMNumbers.formatRupiahString(context, data.amount));
                break;
            case GMTransactionGraphType.REJECT_TRANS:
            case GMTransactionGraphType.SUCCESS_TRANS:
            case GMTransactionGraphType.TOTAL_TRANSACTION:
                gmLineChartContainer.setAmount(Integer.toString(data.amount));
                break;
        }
    }

    private void showTransactionGraph(final List<BaseWilliamChartModel> baseWilliamChartModels) {
        // get tooltip
        Tooltip tooltip = getTooltip(context, getTooltipResLayout());
        BaseWilliamChartConfig baseWilliamChartConfig = Tools.getCommonWilliamChartConfig((Activity) context,
                gmStatisticIncomeGraph, baseWilliamChartModels.get(0));
        baseWilliamChartConfig.addBaseWilliamChartModels(baseWilliamChartModels.get(1), new EmptyDataTransactionDataSetConfig());
        baseWilliamChartConfig.setTooltip(tooltip, new DefaultTooltipConfiguration());
        baseWilliamChartConfig.buildChart(gmStatisticIncomeGraph);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    /**
     * display two {@link BaseWilliamChartModel} models
     *
     * @param data
     * @param dateGraph
     */
    private void showTransactionGraph(List<Integer> data, List<Integer> dateGraph) {
        BaseWilliamChartModel baseWilliamChartModel = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);

        BaseWilliamChartConfig baseWilliamChartConfig = Tools.getCommonWilliamChartConfig((Activity) context,
                gmStatisticIncomeGraph, baseWilliamChartModel);
        Tooltip tooltip = getTooltip(context, getTooltipResLayout());
        baseWilliamChartConfig.setTooltip(tooltip, new DefaultTooltipConfiguration());
        baseWilliamChartConfig.buildChart(gmStatisticIncomeGraph);
        setViewState(LoadingStateView.VIEW_CONTENT);
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