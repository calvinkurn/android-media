package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.widget.LineChartContainerWidget;
import com.tokopedia.seller.gmstat.views.widget.TitleCardView;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewModel;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphChartConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMTopAdsAmountViewHelper extends BaseGMViewHelper<GMGraphViewModel> {
    private LineChartView gmStatisticTopAdsGraph;
    private BaseWilliamChartConfig baseWilliamChartConfig;
    private GMPercentageViewHelper gmPercentageViewHelper2;


    private String[] monthNamesAbrev;
    private Drawable oval2Copy6;
    private TitleCardView gmStatisticTopAdsCardView;
    private LineChartContainerWidget gmTopAdsLineChartWidget;

    public GMTopAdsAmountViewHelper(@Nullable Context context) {
        super(context);
        baseWilliamChartConfig = new BaseWilliamChartConfig();
        gmPercentageViewHelper2 = new GMPercentageViewHelper(context);
    }

    @Override
    public void initView(@Nullable View itemView) {
        oval2Copy6 = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.oval_2_copy_6, null);
        monthNamesAbrev = itemView.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        gmStatisticTopAdsGraph = (LineChartView) itemView.findViewById(R.id.gm_statistic_topads_graph);
        gmStatisticTopAdsCardView = (TitleCardView) itemView.findViewById(R.id.topads_statistic_card_view);
        gmTopAdsLineChartWidget = (LineChartContainerWidget) gmStatisticTopAdsCardView.findViewById(R.id.topads_line_chart_container);
        gmTopAdsLineChartWidget.setPercentageUtil(gmPercentageViewHelper2);
    }

    private void setTopAdsCardView(GMGraphViewModel data) {
        // TODO remove, just example empty state
        View emptyView = LayoutInflater.from(context).inflate(R.layout.empty_product_feed, gmStatisticTopAdsCardView, false);
        gmStatisticTopAdsCardView.setEmptyView(emptyView);
        gmStatisticTopAdsCardView.setEmptyState(true);

        gmStatisticTopAdsCardView.setTitle(context.getString(R.string.gold_merchant_top_ads_amount_title_text));
        gmTopAdsLineChartWidget.setSubtitle(context.getString(R.string.gold_merchant_top_ads_amount_subtitle_text));

        gmTopAdsLineChartWidget.setPercentage((double) (data.percentage * 100));
        gmTopAdsLineChartWidget.setAmount(Integer.toString(data.amount));
    }

    @Override
    public void bind(@Nullable GMGraphViewModel data) {
        setTopAdsCardView(data);

        // create model for chart
        final BaseWilliamChartModel baseWilliamChartModel
                = GMStatisticUtil.joinDateAndGraph3(data.dates, data.values, monthNamesAbrev);

        // resize linechart according to data
        if (context != null && context instanceof Activity)
            GMStatisticUtil.resizeChart(baseWilliamChartModel.size(), gmStatisticTopAdsGraph, (Activity) context);

        // get index to display
        final List<Integer> indexToDisplay = GMStatisticUtil.indexToDisplay(baseWilliamChartModel.getValues());

        baseWilliamChartConfig
                .reset()
                .addBaseWilliamChartModels(baseWilliamChartModel, new GrossGraphDataSetConfig())
                .setDotDrawable(oval2Copy6)
                .setBasicGraphConfiguration(new GrossGraphChartConfig())
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
                }).buildChart(gmStatisticTopAdsGraph);
    }

}
