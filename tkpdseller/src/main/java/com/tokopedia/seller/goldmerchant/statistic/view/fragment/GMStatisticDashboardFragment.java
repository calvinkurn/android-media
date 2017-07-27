package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMStatisticDashboardComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.BuyerDataViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.DataTransactionViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMStatisticGrossViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMStatisticSummaryViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.MarketInsightViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.PopularProductViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.listener.GMStatisticDashboardView;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMDashboardPresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.widget.config.DataTransactionChartConfig;

import java.util.List;

import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 * created by norman 02/01/2017
 */
public class GMStatisticDashboardFragment extends GMStatisticBaseDatePickerFragment implements GMStatisticDashboardView {
    public static final String TAG = "GMStatisticDashboardFragment";

    @Inject
    GMDashboardPresenter gmDashboardPresenter;

    @Inject
    SessionHandler sessionHandler;

    private NestedScrollView contentGMStat;

    private GMStatisticSummaryViewHolder gmStatisticSummaryViewHolder;
    private GMStatisticGrossViewHolder gmStatisticGrossViewHolder;
    private PopularProductViewHolder popularProductViewHolder;
    private DataTransactionViewHolder dataTransactionViewHolder;
    private BuyerDataViewHolder buyerDataViewHolder;
    private MarketInsightViewHolder marketInsightViewHolder;

    private SnackbarRetry snackbarRetry;

    public GMStatisticDashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datePickerPresenter.clearDatePickerSetting();
    }

    @Override
    protected void initInjector() {
        DaggerGMStatisticDashboardComponent
                .builder()
                .goldMerchantComponent(getComponent(GoldMerchantComponent.class))
                .gMStatisticModule(new GMStatisticModule())
                .build().inject(this);
        gmDashboardPresenter.attachView(this);
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
        buyerDataViewHolder.bindData(getBuyerGraph);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gm_statistic_dashboard, container, false);
        initViews(view);
        initNumberFormatter();
        dataTransactionViewHolder.setDataTransactionChartConfig(new DataTransactionChartConfig(getActivity()));
        return view;
    }

    private void initViews(View rootView) {
        gmStatisticSummaryViewHolder = new GMStatisticSummaryViewHolder(rootView);
        gmStatisticGrossViewHolder = new GMStatisticGrossViewHolder(rootView);
        TitleCardView popularProductCardView = (TitleCardView) rootView.findViewById(R.id.popular_product_card_view);
        popularProductViewHolder = new PopularProductViewHolder(popularProductCardView);
        TitleCardView transactionDataCardView = (TitleCardView) rootView.findViewById(R.id.transaction_data_card_view);
        dataTransactionViewHolder = new DataTransactionViewHolder(transactionDataCardView, sessionHandler.isGoldMerchant(getActivity()));
        marketInsightViewHolder = new MarketInsightViewHolder(rootView, sessionHandler.isGoldMerchant(getActivity()));
        TitleCardView buyerDataCardView = (TitleCardView) rootView.findViewById(R.id.buyer_data_card_view);
        buyerDataViewHolder = new BuyerDataViewHolder(buyerDataCardView);

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
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                resetToLoading();
                gmDashboardPresenter.fetchData(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());
            }
        });
        snackbarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
    }

    private void initNumberFormatter() {
        KMNumbers.overrideSuffixes(1000L, "Rb");
        KMNumbers.overrideSuffixes(1000000L, "jt");
    }

    @Override
    public void loadData() {
        super.loadData();
        resetToLoading();
        gmDashboardPresenter.fetchData(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());
    }

    private void resetToLoading() {
        gmStatisticSummaryViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticGrossViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        popularProductViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        dataTransactionViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        buyerDataViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        marketInsightViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
    }

    @Override
    public void onGetShopCategoryEmpty() {
        marketInsightViewHolder.bindNoShopCategory();
        hideSnackBarRetry();
    }

    @Override
    public void onSuccessTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph) {
        gmStatisticGrossViewHolder.setData(getActivity(), getTransactionGraph);
        gmStatisticGrossViewHolder.setViewState(LoadingStateView.VIEW_CONTENT);
        dataTransactionViewHolder.bindData(getTransactionGraph.gmTransactionGraphViewModel.totalTransactionModel);
        hideSnackBarRetry();
    }

    @Override
    public void onSuccessProductnGraph(GetProductGraph getProductGraph) {
        gmStatisticSummaryViewHolder.setData(getProductGraph);
        gmStatisticSummaryViewHolder.setViewState(LoadingStateView.VIEW_CONTENT);
        hideSnackBarRetry();
    }

    @Override
    public void onSuccessPopularProduct(GetPopularProduct getPopularProduct) {
        popularProductViewHolder.bindData(getPopularProduct);
        hideSnackBarRetry();
    }

    @Override
    public void onSuccessGetKeyword(List<GetKeyword> getKeywords) {
        marketInsightViewHolder.bindData(getKeywords);
        hideSnackBarRetry();
    }

    @Override
    public void onSuccessGetCategory(String categoryName) {
        marketInsightViewHolder.bindCategory(categoryName);
        hideSnackBarRetry();
    }

    @Override
    public void onComplete() {
        // tracking below : https://phab.tokopedia.com/T18496
        onLoadGMStatTracking();
    }

    @Override
    public void onError(Throwable e) {
        showSnackbarRetry();
    }

    public void showSnackbarRetry() {
        if (!snackbarRetry.isShown()) {
            snackbarRetry.showRetrySnackbar();
        }
    }

    private void hideSnackBarRetry() {
        if (snackbarRetry.isShown()) {
            snackbarRetry.hideRetrySnackbar();
        }
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