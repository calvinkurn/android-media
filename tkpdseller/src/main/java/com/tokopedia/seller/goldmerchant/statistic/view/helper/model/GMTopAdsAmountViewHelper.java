package com.tokopedia.seller.goldmerchant.statistic.view.helper.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.widget.StatisticCardView;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticTransactionFragment;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.BaseGMViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMPercentageViewHelper2;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMTopAdsAmountViewModel;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.util.EmptyDataTransactionDataSetConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphChartConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMTopAdsAmountViewHelper extends BaseGMViewHelper<GMTopAdsAmountViewModel> {
    private LineChartView gmStatisticTopAdsGraph;
    private LinearLayout gmStatisticTopAdsGraphContainerInner;
    private HorizontalScrollView gmStatisticTopAdsGraphContainer;
    private BaseWilliamChartConfig baseWilliamChartConfig;
    private GMPercentageViewHelper2 gmPercentageViewHelper2;


    private String[] monthNamesAbrev;
    private Drawable oval2Copy6;
    private StatisticCardView gmStatisticTopAdsCardView;

    public GMTopAdsAmountViewHelper(@Nullable Context context) {
        super(context);
        baseWilliamChartConfig = new BaseWilliamChartConfig();
        gmPercentageViewHelper2 = new GMPercentageViewHelper2(context);
    }

    @Override
    public void initView(@Nullable View itemView) {
        oval2Copy6 = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.oval_2_copy_6, null);
        monthNamesAbrev = itemView.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        gmStatisticTopAdsGraph = (LineChartView) itemView.findViewById(R.id.gm_statistic_topads_graph);
        gmStatisticTopAdsGraphContainerInner = (LinearLayout) itemView.findViewById(R.id.gm_statistic_topads_graph_container_inner);
        gmStatisticTopAdsGraphContainer = (HorizontalScrollView) itemView.findViewById(R.id.gm_statistic_topads_graph_container);
        gmStatisticTopAdsCardView = (StatisticCardView) itemView.findViewById(R.id.topads_statistic_card_view);
        gmStatisticTopAdsCardView.setPercentageUtil(gmPercentageViewHelper2);
    }

    private void setTopAdsCardView(GMTopAdsAmountViewModel data) {
        gmStatisticTopAdsCardView.setTitle(context.getString(R.string.gold_merchant_top_ads_amount_title_text));
        gmStatisticTopAdsCardView.setSubtitle(context.getString(R.string.gold_merchant_top_ads_amount_subtitle_text));

        gmStatisticTopAdsCardView.setPercentage((double) (data.percentage * 100));
        gmStatisticTopAdsCardView.setAmount(Integer.toString(data.amount));
    }

    @Override
    public void bind(@Nullable GMTopAdsAmountViewModel data) {
        setTopAdsCardView(data);

        // create model for chart
        final BaseWilliamChartModel baseWilliamChartModel
                = GMStatisticTransactionFragment.joinDateAndGraph3(data.dates, data.values, monthNamesAbrev);

        BaseWilliamChartModel secondWilliamChartModel =
                new BaseWilliamChartModel(baseWilliamChartModel);
        secondWilliamChartModel.increment(25);

        // resize linechart according to data
        if (context != null && context instanceof Activity)
            GMStatisticTransactionFragment.resizeChart(baseWilliamChartModel.size(), gmStatisticTopAdsGraph, (Activity) context);

        // get index to display
        final List<Integer> indexToDisplay = GMStatisticTransactionFragment.indexToDisplay(baseWilliamChartModel.getValues());

        baseWilliamChartConfig
                .reset()
                .addBaseWilliamChartModels(baseWilliamChartModel, new GrossGraphDataSetConfig())
                .addBaseWilliamChartModels(secondWilliamChartModel, new EmptyDataTransactionDataSetConfig())
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
