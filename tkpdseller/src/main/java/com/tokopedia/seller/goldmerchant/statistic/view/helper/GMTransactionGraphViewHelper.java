package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.widget.StatisticCardView;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMTransactionGraphViewModel;
import com.tokopedia.seller.lib.widget.GMDateRangeView;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.util.DefaultTooltipConfiguration;
import com.tokopedia.seller.lib.williamchart.util.EmptyDataTransactionDataSetConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphChartConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMTransactionGraphViewHelper extends BaseGMViewHelper<GMTransactionGraphViewModel> {

    private final String[] gmStatTransactionEntries;
    private int gmStatGraphSelection;

    private LineChartView gmStatisticIncomeGraph;
    private LinearLayout gmStatisticGraphContainerInner;
    private HorizontalScrollView gmStatisticGraphContainer;

    private String[] monthNamesAbrev;
    private Drawable oval2Copy6;

    private BaseWilliamChartConfig baseWilliamChartConfig;
    private StatisticCardView gmStatisticCardView;

    private GMDateRangeView gmStatisticTransactionRangeMain;
    private GMDateRangeView gmStatisticTransactionRangeCompare;

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
        gmStatisticCardView = (StatisticCardView) itemView.findViewById(R.id.gold_merchant_statistic_card_view);
        gmStatisticGraphContainer = (HorizontalScrollView) itemView.findViewById(R.id.gm_statistic_transaction_graph_container);
        gmStatisticGraphContainerInner = (LinearLayout) itemView.findViewById(R.id.gm_statistic_transaction_graph_container_inner);
        gmStatisticIncomeGraph = (LineChartView) itemView.findViewById(R.id.gm_statistic_transaction_income_graph);
        gmStatisticTransactionRangeMain = (GMDateRangeView) itemView.findViewById(R.id.gm_statistic_transaction_range_main);
        gmStatisticTransactionRangeCompare = (GMDateRangeView) itemView.findViewById(R.id.gm_statistic_transaction_range_compare);
    }

    public int selection() {
        return gmStatGraphSelection;
    }

    @Override
    public void bind(@Nullable GMTransactionGraphViewModel data) {
        if (data.isCompare) {

        } else {
            gmStatisticTransactionRangeMain.bind(data.dateRangeModel);
            processTransactionGraph(data.values, data.dates);
        }
    }

    protected void processTransactionGraph(List<Integer> data, List<Integer> dateGraph) {
        // create model for chart
        final BaseWilliamChartModel baseWilliamChartModel
                = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);

        BaseWilliamChartModel secondWilliamChartModel =
                new BaseWilliamChartModel(baseWilliamChartModel);
        secondWilliamChartModel.increment(25);

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
                .addBaseWilliamChartModels(secondWilliamChartModel, new EmptyDataTransactionDataSetConfig())
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
