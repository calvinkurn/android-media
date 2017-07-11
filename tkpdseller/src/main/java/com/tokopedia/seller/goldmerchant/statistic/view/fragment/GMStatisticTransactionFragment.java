package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatisticTransactionApi;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMPercentageViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMTopAdsAmountViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMDateRangeDateViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMTopAdsAmountViewHelper;
import com.tokopedia.seller.lib.widget.GMDateRangeView;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.util.DefaultTooltipConfiguration;
import com.tokopedia.seller.lib.williamchart.util.EmptyDataTransactionDataSetConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphChartConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author normansyahputa on 7/6/17.
 */

public class GMStatisticTransactionFragment extends BaseDaggerFragment {
    public static final String TAG = "GMStatisticTransactionF";

    @Inject
    ImageHandler imageHandler;

    @Inject
    GMStatisticTransactionApi gmStatisticTransactionApi;

    @Inject
    SessionHandler sessionHandler;
    // gm percentage helper
    GMPercentageViewHelper gmPercentageViewHelper;
    private View rootView;
    private LineChartView gmStatisticIncomeGraph;
    private LinearLayout gmStatisticGraphContainerInner;
    private HorizontalScrollView gmStatisticGraphContainer;
    private String[] monthNamesAbrev;
    private Drawable oval2Copy6;
    private BaseWilliamChartConfig baseWilliamChartConfig;
    private GMDateRangeView gmStatisticTransactionRangeMain;
    private GMDateRangeView gmStatisticTransactionRangeCompare;
    private GMTopAdsAmountViewHelper gmTopAdsAmountViewHelper;
    private LabelView gmStatisticProductListText;

    public static Fragment createInstance() {
        return new GMStatisticTransactionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gm_statistic_transaction, container, false);
        initVar();
        initView();
        return rootView;
    }

    private void initVar() {
        monthNamesAbrev = rootView.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        baseWilliamChartConfig = new BaseWilliamChartConfig();
        oval2Copy6 = ResourcesCompat.getDrawable(getResources(), R.drawable.oval_2_copy_6, null);

        gmPercentageViewHelper = new GMPercentageViewHelper(getActivity());
        gmTopAdsAmountViewHelper = new GMTopAdsAmountViewHelper(getActivity());
    }

    private void initView() {
        if (rootView == null)
            return;

        gmStatisticGraphContainer = (HorizontalScrollView) rootView.findViewById(R.id.gm_statistic_transaction_graph_container);
        gmStatisticGraphContainerInner = (LinearLayout) rootView.findViewById(R.id.gm_statistic_transaction_graph_container_inner);
        gmStatisticIncomeGraph = (LineChartView) rootView.findViewById(R.id.gm_statistic_transaction_income_graph);

        gmPercentageViewHelper.initView(rootView);
        gmTopAdsAmountViewHelper.initView(rootView);

        gmStatisticTransactionRangeMain = (GMDateRangeView) rootView.findViewById(R.id.gm_statistic_transaction_range_main);
        gmStatisticTransactionRangeCompare = (GMDateRangeView) rootView.findViewById(R.id.gm_statistic_transaction_range_compare);
        gmStatisticProductListText = (LabelView) rootView.findViewById(R.id.gm_statistic_label_sold_product_list_view);
    }

    @Override
    protected void initInjector() {
        DaggerGMTransactionComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .build().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        gmStatisticTransactionApi.getTransactionGraph(sessionHandler.getShopID(), new HashMap<String, String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<GetTransactionGraph>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Response<GetTransactionGraph> stringResponse) {
                        Log.e(TAG, stringResponse.body().toString());

                        // get data from network
                        GetTransactionGraph getTransactionGraph = stringResponse.body();

                        // get range from network
                        List<Integer> dateGraph = getTransactionGraph.getDateGraph();
                        Pair<Long, String> startDateString = GoldMerchantDateUtils.getDateStringWithoutYear(dateGraph,
                                GMStatisticTransactionFragment.this.monthNamesAbrev,
                                0);
                        Pair<Long, String> endDateString = GoldMerchantDateUtils.getDateString(dateGraph,
                                GMStatisticTransactionFragment.this.monthNamesAbrev,
                                dateGraph.size() - 1);
                        if (startDateString.getModel2() == null || endDateString.getModel2() == null)
                            return;


                        // currently not used
                        String dateRangeFormatString = getString(R.string.gold_merchant_date_range_format_text, startDateString.getModel2(), endDateString.getModel2());

                        GMDateRangeDateViewModel gmDateRangeDateViewModel
                                = new GMDateRangeDateViewModel();
                        gmDateRangeDateViewModel.setStartDate(startDateString);
                        gmDateRangeDateViewModel.setEndDate(endDateString);

                        GMDateRangeDateViewModel gmDateRangeDateViewModel2
                                = new GMDateRangeDateViewModel(gmDateRangeDateViewModel);

                        if (gmStatisticTransactionRangeMain != null)
                            gmStatisticTransactionRangeMain.bind(gmDateRangeDateViewModel);

                        gmStatisticTransactionRangeCompare.bind(gmDateRangeDateViewModel2);
                        gmStatisticTransactionRangeCompare.setDrawable(R.drawable.circle_grey);


                        // display percentage demo
                        processTransactionGraph(getTransactionGraph.getGrossGraph(), dateGraph);

                        gmPercentageViewHelper.bind(getTransactionGraph.getDiffGrossRevenue());

                        // set top ads amount
                        GMTopAdsAmountViewModel gmTopAdsAmountViewModel
                                = new GMTopAdsAmountViewModel();
                        gmTopAdsAmountViewModel.dates = dateGraph;
                        gmTopAdsAmountViewModel.values = joinAdsGraph(getTransactionGraph.getAdsPGraph(), getTransactionGraph.getAdsSGraph());
                        gmTopAdsAmountViewModel.title = getString(R.string.gold_merchant_top_ads_amount_title_text);
                        gmTopAdsAmountViewModel.subtitle = getString(R.string.gold_merchant_top_ads_amount_subtitle_text);

                        // TODO get topads percentage maybe cpc
                        gmTopAdsAmountViewModel.percentage = 1f;

                        // TODO get topads amount
                        gmTopAdsAmountViewModel.amount = 1_00_000;

                        gmTopAdsAmountViewHelper.bind(
                                gmTopAdsAmountViewModel
                        );

                    }
                });

        gmStatisticTransactionApi.getTransactionTable(sessionHandler.getShopID(), new HashMap<String, String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<GetTransactionTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Response<GetTransactionTable> stringResponse) {
                        Log.e(TAG, stringResponse.body().toString());
                    }
                });
    }

    private List<Integer> joinAdsGraph(List<Integer> adsPGraph, List<Integer> adsSGraph) {
        if (adsPGraph == null || adsSGraph == null || adsPGraph.size() != adsSGraph.size()) {
            return null;
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < adsSGraph.size(); i++) {
            Integer product = adsPGraph.get(i);
            Integer shop = adsSGraph.get(i);

            result.add(product + shop);
        }
        return result;
    }

    protected void processTransactionGraph(List<Integer> data, List<Integer> dateGraph) {
        // create model for chart
        final BaseWilliamChartModel baseWilliamChartModel
                = GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);

        BaseWilliamChartModel secondWilliamChartModel =
                new BaseWilliamChartModel(baseWilliamChartModel);
        secondWilliamChartModel.increment(25);

        // resize linechart according to data
        GMStatisticUtil.resizeChart(baseWilliamChartModel.size(), gmStatisticIncomeGraph, getActivity());

        // get index to display
        final List<Integer> indexToDisplay = GMStatisticUtil.indexToDisplay(baseWilliamChartModel.getValues());

        // get tooltip
        Tooltip tooltip = getTooltip(
                GMStatisticTransactionFragment.this.getActivity(),
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

    private
    @LayoutRes
    int getTooltipResLayout() {
        @LayoutRes int layoutTooltip = R.layout.gm_stat_tooltip_lollipop;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP) {
            layoutTooltip = R.layout.gm_stat_tooltip;
        }
        return layoutTooltip;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
