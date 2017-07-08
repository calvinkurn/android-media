package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
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
import com.tokopedia.seller.gmstat.views.DataTransactionViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatisticTransactionApi;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.util.DefaultTooltipConfiguration;
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

    private View rootView;
    private LineChartView gmStatisticIncomeGraph;
    private LinearLayout gmStatisticGraphContainerInner;
    private HorizontalScrollView gmStatisticGraphContainer;
    private String[] monthNamesAbrev;
    private Drawable oval2Copy6;
    private BaseWilliamChartConfig baseWilliamChartConfig;

    public static Fragment createInstance() {
        return new GMStatisticTransactionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gm_statistic_transaction, container, false);
        initView();
        initVar();
        return rootView;
    }

    private void initVar() {
        monthNamesAbrev = rootView.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        baseWilliamChartConfig = new BaseWilliamChartConfig();
        oval2Copy6 = ResourcesCompat.getDrawable(getResources(), R.drawable.oval_2_copy_6, null);
    }

    private void initView() {
        if (rootView == null)
            return;

        gmStatisticGraphContainer = (HorizontalScrollView) rootView.findViewById(R.id.gm_statistic_transaction_graph_container);
        gmStatisticGraphContainerInner = (LinearLayout) rootView.findViewById(R.id.gm_statistic_transaction_graph_container_inner);
        gmStatisticIncomeGraph = (LineChartView) rootView.findViewById(R.id.gm_statistic_transaction_income_graph);
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
                        Pair<Long, String> startDateString = GoldMerchantDateUtils.getDateString(dateGraph,
                                GMStatisticTransactionFragment.this.monthNamesAbrev,
                                0);
                        Pair<Long, String> endDateString = GoldMerchantDateUtils.getDateString(dateGraph,
                                GMStatisticTransactionFragment.this.monthNamesAbrev,
                                dateGraph.size() - 1);
                        if (startDateString.getModel2() == null || endDateString.getModel2() == null)
                            return;

                        // currently not used
                        String dateRangeFormatString = getString(R.string.gold_merchant_date_range_format_text, startDateString.getModel2(), endDateString.getModel2());

                        // create model for chart
                        final BaseWilliamChartModel baseWilliamChartModel
                                = joinDateAndGraph3(dateGraph, getTransactionGraph.getGrossGraph());

                        // resize linechart according to data
                        resizeChart(baseWilliamChartModel.size(), gmStatisticIncomeGraph);

                        // get index to display
                        final List<Integer> indexToDisplay = indexToDisplay(baseWilliamChartModel.getValues());

                        // get tooltip
                        Tooltip tooltip = getTooltip(
                                GMStatisticTransactionFragment.this.getActivity(),
                                getTooltipResLayout()
                        );

                        baseWilliamChartConfig
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

    private List<Integer> indexToDisplay(float[] values) {
        final List<Integer> indexToDisplay = new ArrayList<>();
        int divide = values.length / 10;
        for (int j = 1; j <= divide - 1; j++) {
            indexToDisplay.add((j * 10) - 1);
        }
        return indexToDisplay;
    }

    /**
     * limitation of william chart ( for big width it cannot draw, effectively for size of 15 )
     * https://github.com/diogobernardino/WilliamChart/issues/152
     *
     * @param numChart
     */
    private void resizeChart(int numChart, LineChartView chartView) {
        Log.d(TAG, "resizeChart " + numChart);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) DataTransactionViewHelper.dpToPx(getActivity(), 360); //displaymetrics.widthPixels;
        /*
            set only 8 values in  Window width rest are on sroll or dynamically change the width of linechart
            is  window width/8 * total values returns you the total width of linechart with scrolling and set it in
            layout Params of linechart .
        */
        double newSizeRatio = ((double) numChart) / 7;
        if (newSizeRatio > 1) {
            chartView.setLayoutParams(new LinearLayout.LayoutParams((int) DataTransactionViewHelper.dpToPx(getActivity(), 680), chartView.getLayoutParams().height));//(int) (newSizeRatio * width / 2)
        } else {
            chartView.setLayoutParams(new LinearLayout.LayoutParams(width, chartView.getLayoutParams().height));
        }
    }

    private List<Pair<Integer, String>> joinDateAndGraph(List<Integer> dateGraph, List<Integer> graph) {
        List<Pair<Integer, String>> pairs = new ArrayList<>();
        if (dateGraph == null || graph == null || dateGraph.isEmpty() || graph.isEmpty())
            return null;

        int lowerSize;
        if (dateGraph.size() > graph.size()) {
            lowerSize = graph.size();
        } else {
            lowerSize = dateGraph.size();
        }

        for (int i = 0; i < lowerSize; i++) {
            Integer date = dateGraph.get(i);
            Integer gross = graph.get(i);

            pairs.add(new Pair<>(gross, GoldMerchantDateUtils.getDate(date)));
        }

        return pairs;
    }

    private Pair<String[], float[]> joinDateAndGraph2(List<Integer> dateGraph, List<Integer> graph) {
        List<Pair<Integer, String>> pairs = joinDateAndGraph(dateGraph, graph);
        if (pairs == null)
            return null;

        String[] labels = new String[pairs.size()];
        float[] values = new float[pairs.size()];
        int i = 0;
        for (Pair<Integer, String> integerStringPair : pairs) {
            labels[i] = GoldMerchantDateUtils.getDateRaw(integerStringPair.getModel2(), monthNamesAbrev);
            values[i] = integerStringPair.getModel1();
            i++; // increment the index here
        }

        return new Pair<>(labels, values);
    }

    private BaseWilliamChartModel joinDateAndGraph3(List<Integer> dateGraph, List<Integer> graph) {
        Pair<String[], float[]> pair = joinDateAndGraph2(dateGraph, graph);
        return (pair != null) ? new BaseWilliamChartModel(pair.getModel1(), pair.getModel2()) : null;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
