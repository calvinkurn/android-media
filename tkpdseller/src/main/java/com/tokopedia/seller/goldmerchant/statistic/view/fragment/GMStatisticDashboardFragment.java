package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.presenters.GMDashboardPresenter;
import com.tokopedia.seller.gmstat.presenters.GMFragmentView;
import com.tokopedia.seller.gmstat.utils.GMNetworkErrorHelper;
import com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils;
import com.tokopedia.seller.gmstat.utils.GridDividerItemDecoration;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.BuyerDataViewHelper;
import com.tokopedia.seller.gmstat.views.DataTransactionViewHelper;
import com.tokopedia.seller.gmstat.views.MarketInsightViewHelper;
import com.tokopedia.seller.gmstat.views.OnActionClickListener;
import com.tokopedia.seller.gmstat.views.PopularProductViewHelper;
import com.tokopedia.seller.gmstat.views.adapter.GMStatWidgetAdapter;
import com.tokopedia.seller.gmstat.views.models.GrossIncome;
import com.tokopedia.seller.gmstat.views.models.LoadingGMModel;
import com.tokopedia.seller.gmstat.views.models.LoadingGMTwoModel;
import com.tokopedia.seller.gmstat.views.widget.LoadingStateView;
import com.tokopedia.seller.gmstat.views.widget.TitleCardView;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMStatisticDashboardComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatClearCacheUseCase;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewWithPreviousModel;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMStatisticSummaryViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.util.DataTransactionChartConfig;
import com.tokopedia.seller.lib.williamchart.util.DefaultTooltipConfiguration;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphChartConfig;
import com.tokopedia.seller.lib.williamchart.util.GrossGraphDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * A placeholder fragment containing a simple view.
 * created by norman 02/01/2017
 */
public class GMStatisticDashboardFragment extends GMStatisticBaseDatePickerFragment implements GMFragmentView {
    public static final String TAG = "GMStatisticDashboardFragment";
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    @Inject
    GMStatClearCacheUseCase gmStatClearCacheUseCase;

    @Inject
    GMDashboardPresenter gmDashboardPresenter;

    @Inject
    SessionHandler sessionHandler;

    private String tryAgainText;
    private String unknownExceptionDescription;
    private String messageExceptionDescription;
    private String defaultExceptionDescription;
    private String[] monthNamesAbrev;
    private LineChartView grossIncomeGraph2;
    private RecyclerView gmStatRecyclerView;
    private GMStatWidgetAdapter gmStatWidgetAdapter;
    private LoaderImageView grossIncomeGraph2Loading;
    private HorizontalScrollView grossIncomeGraphContainer;
    private Drawable oval2Copy6;

    private GridLayoutManager gridLayoutManager;
    private MarketInsightViewHelper marketInsightViewHelper;
    private PopularProductViewHelper popularProductViewHelper;

    private View rootView;
    private DataTransactionViewHelper dataTransactionViewHelper;
    private BaseWilliamChartConfig baseWilliamChartConfig;
    private GMNetworkErrorHelper gmNetworkErrorHelper;

    private BuyerDataViewHelper buyerDataViewHelper;

    private GMStatisticSummaryViewHolder gmStatisticSummaryViewHolder;

    public GMStatisticDashboardFragment() {
    }

    @Override
    protected void initInjector() {
        DaggerGMStatisticDashboardComponent
                .builder()
                .goldMerchantComponent(getComponent(GoldMerchantComponent.class))
                .gMStatisticModule(new GMStatisticModule())
                .build().inject(this);

        gmDashboardPresenter.attachView(this);
        gmStatClearCacheUseCase.execute(null, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                // no op
            }

            @Override
            public void onError(Throwable e) {
                // no op
            }

            @Override
            public void onNext(Boolean aBoolean) {
                // no op
            }
        });
    }

    void initViews(View rootView) {

        gmStatisticSummaryViewHolder = new GMStatisticSummaryViewHolder(rootView);

        tryAgainText = getString(R.string.try_again);
        unknownExceptionDescription = getString(R.string.unknown_exception_description);
        messageExceptionDescription = getString(R.string.message_exception_description);
        defaultExceptionDescription = getString(R.string.default_exception_description);

        monthNamesAbrev = rootView.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        grossIncomeGraph2 = (LineChartView) rootView.findViewById(R.id.gross_income_graph2);
        gmStatRecyclerView = (RecyclerView) rootView.findViewById(R.id.gmstat_recyclerview);
        grossIncomeGraph2Loading = (LoaderImageView) rootView.findViewById(R.id.gross_income_graph2_loading);
        grossIncomeGraphContainer = (HorizontalScrollView) rootView.findViewById(R.id.gross_income_graph_container);
        oval2Copy6 = ResourcesCompat.getDrawable(getResources(), R.drawable.oval_2_copy_6, null);

        // analytic below : https://phab.tokopedia.com/T18496
        final ScrollView contentGMStat = (ScrollView) rootView.findViewById(R.id.content_gmstat);
        contentGMStat.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = contentGMStat.getChildAt(contentGMStat.getChildCount() - 1);
                int diff = (view.getBottom() + contentGMStat.getPaddingBottom()
                        - (contentGMStat.getHeight() + contentGMStat.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    onScrollGMStatTracking();
                }
            }
        });
    }

    private BaseWilliamChartModel joinDateAndGrossGraph(List<Integer> data, List<Integer> dateGraph) {
        return GMStatisticUtil.joinDateAndGraph3(dateGraph, data, monthNamesAbrev);
    }

    protected void initAdapter() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gmStatRecyclerView.setLayoutManager(gridLayoutManager);
        GridDividerItemDecoration gridDividerItemDecoration = new GridDividerItemDecoration(getActivity());
        gmStatRecyclerView.addItemDecoration(gridDividerItemDecoration);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (gmStatWidgetAdapter.getItemViewType(position)) {
                    case LoadingGMModel.TYPE:
                        return 1;
                    case LoadingGMTwoModel.TYPE:
                    default:
                        return 2;
                }
            }
        });
        gmStatRecyclerView.setAdapter(gmStatWidgetAdapter);
    }

    protected void initAdapter(final GMStatWidgetAdapter gmStatWidgetAdapter) {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gmStatRecyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (gmStatWidgetAdapter.getItemViewType(position)) {
                    case LoadingGMModel.TYPE:
                        return 1;
                    case LoadingGMTwoModel.TYPE:
                    default:
                        return 2;
                }
            }
        });
        gmStatRecyclerView.setAdapter(gmStatWidgetAdapter);
    }

    @Override
    public void resetToLoading() {
        resetEmptyAdapter();
        initChartLoading();
        initPopularLoading();
        initTransactionDataLoading();
        initBuyerDataLoading();
        initMarketInsightLoading();
    }

    @Override
    public void onLoadGMStatTracking() {
        UnifyTracking.eventLoadGMStat();
    }

    @Override
    public void onScrollGMStatTracking() {
        UnifyTracking.eventScrollGMStat();
    }

    @Override
    public void onSuccessBuyerGraph(GetBuyerGraph getBuyerGraph) {
        buyerDataViewHelper.bindData(getBuyerGraph);
    }

    @Override
    public DatePickerViewModel datePickerViewModel() {
        return datePickerViewModel;
    }

    /**
     * reset 4 box to loading state
     */
    private void resetEmptyAdapter() {
        gmStatWidgetAdapter.clear();

        List<ItemType> loadingBases = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            loadingBases.add(new LoadingGMModel());

        loadingBases.add(new LoadingGMTwoModel());
        gmStatWidgetAdapter.addAll(loadingBases);
        gmStatWidgetAdapter.notifyDataSetChanged();
    }

    protected void initEmptyAdapter() {
        List<ItemType> loadingBases = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            loadingBases.add(new LoadingGMModel());

        loadingBases.add(new LoadingGMTwoModel());

        gmStatWidgetAdapter = new GMStatWidgetAdapter(loadingBases);
        initAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gmDashboardPresenter.setFirstTime(false);
        rootView = inflater.inflate(R.layout.fragment_gm_statistic_dashboard, container, false);

        initViews(rootView);
        initNumberFormatter();
        initEmptyAdapter();
        initChartLoading();
        TitleCardView popularProductCardView = (TitleCardView) rootView.findViewById(R.id.popular_product_card_view);
        popularProductViewHelper = new PopularProductViewHelper(popularProductCardView);

        TitleCardView transactionDataCardView = (TitleCardView) rootView.findViewById(R.id.transaction_data_card_view);
        dataTransactionViewHelper = new DataTransactionViewHelper(transactionDataCardView, sessionHandler.isGoldMerchant(getActivity()));

        TitleCardView marketInsightCardView = (TitleCardView) rootView.findViewById(R.id.market_insight_card_view);
        marketInsightViewHelper = new MarketInsightViewHelper(marketInsightCardView, sessionHandler.isGoldMerchant(getActivity()));

        TitleCardView buyerDataCardView = (TitleCardView) rootView.findViewById(R.id.buyer_data_card_view);
        buyerDataViewHelper = new BuyerDataViewHelper(buyerDataCardView);

        initPopularLoading();
        initTransactionDataLoading();
        initBuyerDataLoading();
        initMarketInsightLoading();
        gmDashboardPresenter.initInstance();
        baseWilliamChartConfig = new BaseWilliamChartConfig();
        dataTransactionViewHelper.setDataTransactionChartConfig(new DataTransactionChartConfig(getActivity()));
        return rootView;
    }

    private void initNumberFormatter() {
        KMNumbers.overrideSuffixes(1000L, "Rb");
        KMNumbers.overrideSuffixes(1000000L, "jt");
    }

    private void initMarketInsightLoading() {
        marketInsightViewHelper.showLoading();
    }

    private void initBuyerDataLoading() {
        buyerDataViewHelper.showLoading();
    }

    private void initTransactionDataLoading() {
        dataTransactionViewHelper.showLoading();
    }

    private void initPopularLoading() {
        popularProductViewHelper.showLoading();
    }

    private void initChartLoading() {
        grossIncomeGraphContainer.setVisibility(View.GONE);
        grossIncomeGraph2Loading.setVisibility(View.VISIBLE);
        grossIncomeGraph2Loading.resetLoader();
    }

    private void displayChart() {
        grossIncomeGraphContainer.setVisibility(View.VISIBLE);
        grossIncomeGraph2Loading.setVisibility(View.GONE);
    }

    @Override
    public void loadData() {
        super.loadData();
        gmNetworkErrorHelper = new GMNetworkErrorHelper(null, rootView);
        gmDashboardPresenter.onResume();
    }

    public void displayDefaultValue() {
        gmDashboardPresenter.displayDefaultValue(getActivity().getAssets());
    }

    @Override
    public void fetchData() {
        gmDashboardPresenter.fetchData();
    }

    @Override
    public void fetchData(long sDate, long eDate, int lastSelectionPeriod, int selectionType) {
        gmDashboardPresenter.fetchData(sDate, eDate, lastSelectionPeriod, selectionType);
    }

    @Override
    public void onPause() {
        super.onPause();
        gmNetworkErrorHelper.onPause();
        gmDashboardPresenter.onPause();
        if (grossIncomeGraph2 != null)
            grossIncomeGraph2.dismissAllTooltips();
    }

    @Override
    public void onGetShopCategoryEmpty() {
        marketInsightViewHelper.bindNoShopCategory();
    }

    @Override
    public void onSuccessTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph, long sDate, long eDate, int lastSelectionPeriod, int selectionType) {
        GMGraphViewWithPreviousModel grossRevenueModel = getTransactionGraph.gmTransactionGraphViewModel.grossRevenueModel;
        GrossIncome grossIncome = new GrossIncome(grossRevenueModel.amount);
        List<ItemType> baseGMModels = new ArrayList<>();
        baseGMModels.add(grossIncome);

        String startDate = GoldMerchantDateUtils.getDateWithoutYear(GoldMerchantDateUtils.getDateFormatForInput(grossRevenueModel.dateRangeModel.getStartDate()), monthNamesAbrev);
        String endDate = GoldMerchantDateUtils.getDateWithYear(GoldMerchantDateUtils.getDateFormatForInput(grossRevenueModel.dateRangeModel.getEndDate()), monthNamesAbrev);

        List<Integer> dateGraph = grossRevenueModel.dates;
        //[START] use date from network
        if (startDate != null || endDate != null) {
            grossIncome.textDescription = getString(R.string.gold_merchant_date_range_format_text, startDate, endDate);
        }
        //[END] use date from network
        //[START] override sDate and eDate with local selection.
        if (sDate != -1 || eDate != -1) {
            String sDateWithYear = GoldMerchantDateUtils.getDateWithYear(
                    GoldMerchantDateUtils.getDateFormatForInput(sDate), monthNamesAbrev);
            String eDateWithYear = GoldMerchantDateUtils.getDateWithYear(
                    GoldMerchantDateUtils.getDateFormatForInput(eDate), monthNamesAbrev);
            grossIncome.textDescription = sDateWithYear + " - " + eDateWithYear;

            dateGraph = GoldMerchantDateUtils.generateDateRanges(sDate, eDate);
        }
        //[END] override sDate and eDate with local selection.
        final BaseWilliamChartModel baseWilliamChartModel =
                joinDateAndGrossGraph(grossRevenueModel.values, dateGraph);

        if (baseWilliamChartModel != null) {
            //[]START] try used willam chart
            displayChart();

            GMStatisticUtil.resizeChart(baseWilliamChartModel.size(), grossIncomeGraph2, getActivity());

            // get index to display
            final List<Integer> indexToDisplay = GMStatisticUtil.indexToDisplay(baseWilliamChartModel.getValues());

            @LayoutRes int layoutTooltip = R.layout.gm_stat_tooltip_lollipop;
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP) {
                layoutTooltip = R.layout.gm_stat_tooltip;
            }

            baseWilliamChartConfig
                    .reset()
                    .addBaseWilliamChartModels(baseWilliamChartModel, new GrossGraphDataSetConfig())
                    .setBasicGraphConfiguration(new GrossGraphChartConfig())
                    .setDotDrawable(oval2Copy6)
                    .setTooltip(new Tooltip(getActivity(),
                            layoutTooltip,
                            R.id.gm_stat_tooltip_textview,
                            new StringFormatRenderer() {
                                @Override
                                public String formatString(String s) {
                                    return KMNumbers.formatNumbers(Float.valueOf(s));
                                }
                            }), new DefaultTooltipConfiguration())
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
                    }).buildChart(grossIncomeGraph2);
            //[END] try used willam chart
        }


        gmStatWidgetAdapter.addAll(baseGMModels);
        gmStatWidgetAdapter.notifyDataSetChanged();

        dataTransactionViewHelper.bindData(getTransactionGraph.gmTransactionGraphViewModel.totalTransactionModel);
    }

    @Override
    public void onSuccessProductnGraph(GetProductGraph getProductGraph, boolean isFirstTime) {
        List<ItemType> baseGMModels = new ArrayList<>();
        gmStatWidgetAdapter.clear();
        gmStatWidgetAdapter.addAll(baseGMModels);
        gmStatisticSummaryViewHolder.setData(getProductGraph);
        gmStatisticSummaryViewHolder.setViewState(LoadingStateView.VIEW_CONTENT);
        if (!isFirstTime) {
            initAdapter(gmStatWidgetAdapter);
            gmDashboardPresenter.setFirstTime(true);
        }
    }

    @Override
    public void onSuccessPopularProduct(GetPopularProduct getPopularProduct) {
        popularProductViewHelper.bindData(getPopularProduct);
    }

    @Override
    public void onSuccessGetKeyword(List<GetKeyword> getKeywords) {
        marketInsightViewHelper.bindData(getKeywords);
    }

    @Override
    public void onSuccessGetCategory(String categoryName) {
        marketInsightViewHelper.bindCategory(categoryName);
    }

    @Override
    public void onComplete() {
        // tracking below : https://phab.tokopedia.com/T18496
        onLoadGMStatTracking();
    }

    @Override
    public void onError(Throwable e) {
        displayDefaultValue();

        final StringBuilder textMessage = new StringBuilder("");
        if (e instanceof UnknownHostException) {
            textMessage.append(unknownExceptionDescription);
        } else if (e instanceof MessageErrorException) {
            textMessage.append(messageExceptionDescription);
        } else {
            textMessage.append(defaultExceptionDescription);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && rootView != null) {
                    gmNetworkErrorHelper.showSnackbar(textMessage.toString(), tryAgainText
                            , new OnActionClickListener() {
                                @Override
                                public void onClick(@SuppressWarnings("UnusedParameters") View view) {
                                    gmDashboardPresenter.setFetchData(true);
                                    gmDashboardPresenter.fetchData();
                                }
                            });
                }
            }
        }, 100);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        gmDashboardPresenter.saveState(outState);
    }

    @Override
    protected void onDateSelected(Intent intent) {
        super.onDateSelected(intent);
        fetchData(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate(),
                datePickerViewModel.getDatePickerType(), datePickerViewModel.getDatePickerSelection());
    }

    @Override
    public void onFailure() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
    //[END] unused methods


    @Override
    public void onDestroy() {
        super.onDestroy();
        gmDashboardPresenter.detachView();
    }
}
