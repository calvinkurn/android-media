package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.gmstat.utils.GMNetworkErrorHelper;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.BuyerDataViewHelper;
import com.tokopedia.seller.gmstat.views.DataTransactionViewHelper;
import com.tokopedia.seller.gmstat.views.MarketInsightViewHelper;
import com.tokopedia.seller.gmstat.views.OnActionClickListener;
import com.tokopedia.seller.gmstat.views.PopularProductViewHelper;
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
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMStatisticGrossViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMStatisticSummaryViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.listener.GMStatisticDashboardView;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMDashboardPresenter;
import com.tokopedia.seller.lib.williamchart.util.DataTransactionChartConfig;

import java.net.UnknownHostException;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * A placeholder fragment containing a simple view.
 * created by norman 02/01/2017
 */
public class GMStatisticDashboardFragment extends GMStatisticBaseDatePickerFragment implements GMStatisticDashboardView {
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

    private MarketInsightViewHelper marketInsightViewHelper;
    private PopularProductViewHelper popularProductViewHelper;

    private DataTransactionViewHelper dataTransactionViewHelper;
    private GMNetworkErrorHelper gmNetworkErrorHelper;

    private BuyerDataViewHelper buyerDataViewHelper;

    private GMStatisticSummaryViewHolder gmStatisticSummaryViewHolder;
    private GMStatisticGrossViewHolder gmStatisticGrossViewHolder;
    private NestedScrollView contentGMStat;

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
        gmStatisticGrossViewHolder = new GMStatisticGrossViewHolder(rootView);

        tryAgainText = getString(R.string.try_again);
        unknownExceptionDescription = getString(R.string.unknown_exception_description);
        messageExceptionDescription = getString(R.string.message_exception_description);
        defaultExceptionDescription = getString(R.string.default_exception_description);

        // analytic below : https://phab.tokopedia.com/T18496
        contentGMStat = (NestedScrollView) rootView.findViewById(R.id.content_gmstat);
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

    @Override
    public void resetToLoading() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gmDashboardPresenter.setFirstTime(false);
        View view = inflater.inflate(R.layout.fragment_gm_statistic_dashboard, container, false);

        initViews(view);
        initNumberFormatter();
        TitleCardView popularProductCardView = (TitleCardView) view.findViewById(R.id.popular_product_card_view);
        popularProductViewHelper = new PopularProductViewHelper(popularProductCardView);

        TitleCardView transactionDataCardView = (TitleCardView) view.findViewById(R.id.transaction_data_card_view);
        dataTransactionViewHelper = new DataTransactionViewHelper(transactionDataCardView, sessionHandler.isGoldMerchant(getActivity()));

        TitleCardView marketInsightCardView = (TitleCardView) view.findViewById(R.id.market_insight_card_view);
        marketInsightViewHelper = new MarketInsightViewHelper(marketInsightCardView, sessionHandler.isGoldMerchant(getActivity()));

        TitleCardView buyerDataCardView = (TitleCardView) view.findViewById(R.id.buyer_data_card_view);
        buyerDataViewHelper = new BuyerDataViewHelper(buyerDataCardView);

        gmNetworkErrorHelper = new GMNetworkErrorHelper(null, view);

        initPopularLoading();
        initTransactionDataLoading();
        initBuyerDataLoading();
        initMarketInsightLoading();
        gmDashboardPresenter.initInstance();
        dataTransactionViewHelper.setDataTransactionChartConfig(new DataTransactionChartConfig(getActivity()));
        return view;
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

    @Override
    public void loadData() {
        super.loadData();
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
    }

    @Override
    public void onGetShopCategoryEmpty() {
        marketInsightViewHelper.bindNoShopCategory();
    }

    @Override
    public void onSuccessTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph, long sDate, long eDate, int lastSelectionPeriod, int selectionType) {
        gmStatisticGrossViewHolder.setData(getTransactionGraph);
        gmStatisticGrossViewHolder.setViewState(LoadingStateView.VIEW_CONTENT);
        dataTransactionViewHelper.bindData(getTransactionGraph.gmTransactionGraphViewModel.totalTransactionModel);
    }

    @Override
    public void onSuccessProductnGraph(GetProductGraph getProductGraph, boolean isFirstTime) {
        gmStatisticSummaryViewHolder.setData(getProductGraph);
        gmStatisticSummaryViewHolder.setViewState(LoadingStateView.VIEW_CONTENT);
        if (!isFirstTime) {
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
                if (isAdded()) {
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
    public void onDestroy() {
        super.onDestroy();
        gmDashboardPresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}