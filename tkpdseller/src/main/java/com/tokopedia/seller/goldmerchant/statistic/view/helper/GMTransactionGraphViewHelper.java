package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.widget.LineChartContainerWidget;
import com.tokopedia.seller.gmstat.views.widget.TitleCardView;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionGraphType;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewWithPreviousModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphViewModel;
import com.tokopedia.seller.lib.widget.GMDateRangeView;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.util.DefaultTooltipConfiguration;
import com.tokopedia.seller.lib.williamchart.util.EmptyDataTransactionDataSetConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphChartConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMTransactionGraphViewHelper extends BaseGMViewHelper<GMTransactionGraphViewModel> {

    private final String[] gmStatTransactionEntries;
    private int gmStatGraphSelection;

    private LineChartView gmStatisticIncomeGraph;
    private String[] monthNamesAbrev;
    private Drawable oval2Copy6;

    private BaseWilliamChartConfig baseWilliamChartConfig;
    private TitleCardView gmTitleCardView;
    private LineChartContainerWidget gmLineChartContainer;

    private GMDateRangeView gmStatisticTransactionRangeMain;
    private GMDateRangeView gmStatisticTransactionRangeCompare;

    private boolean showingSimpleDialog;
    private GMTransactionGraphViewModel data;

    public GMTransactionGraphViewHelper(@Nullable Context context) {
        super(context);

        gmStatGraphSelection = 0;
        gmStatTransactionEntries = context.getResources().getStringArray(R.array.lib_gm_stat_transaction_entries);

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
        gmStatisticTransactionRangeMain = (GMDateRangeView) itemView.findViewById(R.id.gm_statistic_transaction_range_main);
        gmStatisticTransactionRangeCompare = (GMDateRangeView) itemView.findViewById(R.id.gm_statistic_transaction_range_compare);
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

    private void showGraphSelectionDialog() {
        showingSimpleDialog = true;
        BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(context)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setItemLayout(R.layout.bottomsheetbuilder_list_adapter_without_padding)
                .addTitleItem(context.getString(R.string.gold_merchant_transaction_summary_text));

        for (int i = 0; i < gmStatTransactionEntries.length; i++) {
            bottomSheetBuilder.addItem(i, gmStatTransactionEntries[i], null);
        }

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        gmStatGraphSelection = findSelection(gmStatTransactionEntries, item.getTitle().toString());
                        Log.d("Item click", item.getTitle() + " findSelection : " + gmStatGraphSelection);
                        GMTransactionGraphViewHelper.this.bind(data);
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

    private int findSelection(String[] values, String selection) {
        int searchIndex = -1;

        int count = 0;
        for (String value : values) {
            if (value.equals(selection)) {
                return searchIndex = count;
            }
            count++;
        }
        return searchIndex;
    }

    public int selection() {
        return gmStatGraphSelection;
    }

    private void bind(@Nullable GMGraphViewWithPreviousModel data) {
        setHeaderValue(data);
        if (data.isCompare) {
            gmStatisticTransactionRangeCompare.setVisibility(View.VISIBLE);
            gmStatisticTransactionRangeCompare.bind(data.pDateRangeModel);
            gmStatisticTransactionRangeCompare.setDrawable(R.drawable.circle_grey);
            gmStatisticTransactionRangeMain.bind(data.dateRangeModel);
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
            gmStatisticTransactionRangeCompare.setVisibility(View.GONE);
            fillTransactionGraphMain(data);
        }
    }

    private void fillTransactionGraphMain(@Nullable GMGraphViewWithPreviousModel data) {
        gmStatisticTransactionRangeMain.bind(data.dateRangeModel);
        showTransactionGraph(data.values, data.dates);
    }

    private void setHeaderValue(GMGraphViewWithPreviousModel data) {
        gmTitleCardView.setTitle(gmStatTransactionEntries[gmStatGraphSelection]);

        gmLineChartContainer.setPercentage((double) (data.percentage * 100));
        gmLineChartContainer.setAmount(Integer.toString(data.amount));
    }

    protected void showTransactionGraph(final List<BaseWilliamChartModel> baseWilliamChartModels) {
        // resize linechart according to data
//        if (context != null && context instanceof Activity)
//            GMStatisticUtil.resizeChart(baseWilliamChartModels.get(0).size(), gmStatisticIncomeGraph, (Activity) context);

        // get index to display
        final List<Integer> indexToDisplay = GMStatisticUtil.indexToDisplay(baseWilliamChartModels.get(0).getValues());

        // get tooltip
        Tooltip tooltip = getTooltip(
                context,
                getTooltipResLayout()
        );

        baseWilliamChartConfig
                .reset()
                .addBaseWilliamChartModels(baseWilliamChartModels.get(1), new EmptyDataTransactionDataSetConfig())
                .addBaseWilliamChartModels(baseWilliamChartModels.get(0), new GrossGraphDataSetConfig())
                .setBasicGraphConfiguration(new GrossGraphChartConfig())
                .setDotDrawable(oval2Copy6)
                .setTooltip(tooltip, new DefaultTooltipConfiguration())
                .setxRendererListener(new XRenderer.XRendererListener() {
                    @Override
                    public boolean filterX(@IntRange(from = 0L) int i) {
                        if (i == 0 || baseWilliamChartModels.get(0).getValues().length - 1 == i)
                            return true;

                        if (baseWilliamChartModels.get(0).getValues().length <= 15) {
                            return true;
                        }

                        return indexToDisplay.contains(i);

                    }
                }).buildChart(gmStatisticIncomeGraph);
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
        if (context != null && context instanceof Activity)
            GMStatisticUtil.resizeChart(baseWilliamChartModel.size(), gmStatisticIncomeGraph, (Activity) context);

        // get index to display
        final List<Integer> indexToDisplay = GMStatisticUtil.indexToDisplay(baseWilliamChartModel.getValues());

        // get tooltip
        Tooltip tooltip = getTooltip(
                context,
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
                }).buildChart(gmStatisticIncomeGraph);
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
