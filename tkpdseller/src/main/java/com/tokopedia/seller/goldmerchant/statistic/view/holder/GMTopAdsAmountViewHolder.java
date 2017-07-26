package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.BaseGMViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMPercentageViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.LineChartContainerWidget;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.GrossGraphChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.GrossGraphDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.List;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMTopAdsAmountViewHolder extends BaseGMViewHelper<GMGraphViewModel> {
    private LineChartView gmStatisticTopAdsGraph;
    private BaseWilliamChartConfig baseWilliamChartConfig;
    private GMPercentageViewHelper gmPercentageViewHelper;

    private String[] monthNamesAbrev;
    private Drawable oval2Copy6;
    private TitleCardView gmStatisticTopAdsCardView;
    private LineChartContainerWidget gmTopAdsLineChartWidget;

    private OnTopAdsViewHolderListener onTopAdsViewHolderListener;
    public interface OnTopAdsViewHolderListener{
        void onChangeDateClicked();
        void onFindOutTopAdsClicked();
    }

    public void setOnTopAdsViewHolderListener(OnTopAdsViewHolderListener onTopAdsViewHolderListener) {
        this.onTopAdsViewHolderListener = onTopAdsViewHolderListener;
    }

    public GMTopAdsAmountViewHolder(@Nullable Context context) {
        super(context);
        baseWilliamChartConfig = new BaseWilliamChartConfig();
        gmPercentageViewHelper = new GMPercentageViewHelper(context);
    }

    @Override
    public void initView(@Nullable View itemView) {
        oval2Copy6 = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.oval_2_copy_6, null);
        monthNamesAbrev = itemView.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        gmStatisticTopAdsGraph = (LineChartView) itemView.findViewById(R.id.gm_statistic_topads_graph);
        gmStatisticTopAdsCardView = (TitleCardView) itemView.findViewById(R.id.topads_statistic_card_view);
        gmTopAdsLineChartWidget = (LineChartContainerWidget) gmStatisticTopAdsCardView.findViewById(R.id.topads_line_chart_container);
        gmTopAdsLineChartWidget.setPercentageUtil(gmPercentageViewHelper);
    }

    private void setTopAdsCardView(GMGraphViewModel data) {
        gmTopAdsLineChartWidget.setSubtitle(context.getString(R.string.gold_merchant_top_ads_amount_subtitle_text));

        gmTopAdsLineChartWidget.setPercentage(data.percentage);
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
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    public void setViewState(int state) {
        gmStatisticTopAdsCardView.setViewState(state);
    }

    public void bindNoData(@Nullable GMGraphViewModel data) {
        gmStatisticTopAdsCardView.setEmptyViewRes(R.layout.item_empty_gm_stat_topads);
        EmptyViewHolder emptyNoDataViewHolder = new EmptyNoDataViewHolder(gmStatisticTopAdsCardView.getEmptyView());
        emptyNoDataViewHolder.bind(data);
        setViewState(LoadingStateView.VIEW_EMPTY);
    }


    public void bindNoTopAdsCredit(@Nullable GMGraphViewModel data) {
        gmStatisticTopAdsCardView.setEmptyViewRes(R.layout.item_empty_gm_stat_topads);
        EmptyViewHolder emptyNoDataViewHolder = new EmptyNoTopAdsCreditHolder(gmStatisticTopAdsCardView.getEmptyView());
        emptyNoDataViewHolder.bind(data);
        setViewState(LoadingStateView.VIEW_EMPTY);
    }


    public void bindTopAdsCreditNotUsed(@Nullable GMGraphViewModel data) {
        gmStatisticTopAdsCardView.setEmptyViewRes(R.layout.item_empty_gm_stat_topads);
        EmptyViewHolder emptyNoDataViewHolder = new EmptyTopAdsCreditNotUsedHolder(gmStatisticTopAdsCardView.getEmptyView());
        emptyNoDataViewHolder.bind(data);
        setViewState(LoadingStateView.VIEW_EMPTY);
    }

    abstract class EmptyViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvAction;
        View button;

        public EmptyViewHolder(View itemView) {
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSubtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            tvAction = (TextView) itemView.findViewById(R.id.tv_action);
            button = itemView.findViewById(R.id.button_action);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTopAdsViewHolderListener!= null) {
                        onTopAdsViewHolderListener.onFindOutTopAdsClicked();
                    }
                }
            });
        }

        public abstract void bind(@Nullable GMGraphViewModel data);
    }

    class EmptyNoDataViewHolder extends EmptyViewHolder {

        public EmptyNoDataViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(@Nullable GMGraphViewModel data) {
            tvTitle.setText(context.getString(R.string.gm_stat_top_ads_empty_title_no_data));
            tvSubtitle.setText(context.getString(R.string.gm_stat_top_ads_empty_desc_no_data));
            tvAction.setText(MethodChecker.fromHtml(context.getString(R.string.change_date)));
            tvAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTopAdsViewHolderListener!= null) {
                        onTopAdsViewHolderListener.onChangeDateClicked();
                    }
                }
            });
            button.setVisibility(View.GONE);
        }
    }

    class EmptyNoTopAdsCreditHolder extends EmptyViewHolder {

        public EmptyNoTopAdsCreditHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(@Nullable GMGraphViewModel data) {
            tvTitle.setText(context.getString(R.string.gm_stat_top_ads_empty_title_no_topads_credit));
            tvSubtitle.setText(context.getString(R.string.gm_stat_top_ads_empty_desc_no_topads_credit));
            tvAction.setText(context.getString(R.string.find_out));
            tvAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTopAdsViewHolderListener!= null) {
                        onTopAdsViewHolderListener.onFindOutTopAdsClicked();
                    }
                }
            });
            button.setVisibility(View.VISIBLE);
        }
    }

    class EmptyTopAdsCreditNotUsedHolder extends EmptyViewHolder {

        public EmptyTopAdsCreditNotUsedHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(@Nullable GMGraphViewModel data) {
            tvTitle.setText(context.getString(R.string.gm_stat_top_ads_empty_title_credit_not_used));
            if (data == null) {
                tvSubtitle.setVisibility(View.GONE);
            } else {
                tvSubtitle.setText(MethodChecker.fromHtml(context.getString(R.string.gm_stat_top_ads_empty_desc_credit_not_used,
                        KMNumbers.formatString((double) data.amount))));
                tvSubtitle.setVisibility(View.VISIBLE);
            }
            tvAction.setText(context.getString(R.string.find_out));
            tvAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTopAdsViewHolderListener!= null) {
                        onTopAdsViewHolderListener.onFindOutTopAdsClicked();
                    }
                }
            });
            button.setVisibility(View.VISIBLE);
        }
    }
}
