package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMStatisticDashboardComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMStatisticGrossViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMStatisticProductViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMStatisticSummaryViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMStatisticTransactionViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GmStatisticBuyerViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GmStatisticMarketInsightViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.listener.GMStatisticDashboardView;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMDashboardPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 * created by norman 02/01/2017
 */
public class GMStatisticDashboardFragment extends GMStatisticBaseDatePickerFragment
        implements GMStatisticDashboardView {

    @Inject
    GMDashboardPresenter gmDashboardPresenter;

    private NestedScrollView nestedScrollView;

    private GMStatisticSummaryViewHolder gmStatisticSummaryViewHolder;
    private GMStatisticGrossViewHolder gmStatisticGrossViewHolder;
    private GMStatisticProductViewHolder gmStatisticProductViewHolder;
    private GMStatisticTransactionViewHolder gmStatisticTransactionViewHolder;
    private GmStatisticBuyerViewHolder gmStatisticBuyerViewHolder;
    private GmStatisticMarketInsightViewHolder gmStatisticMarketInsightViewHolder;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gm_statistic_dashboard, container, false);
        gmStatisticSummaryViewHolder = new GMStatisticSummaryViewHolder(view);
        gmStatisticGrossViewHolder = new GMStatisticGrossViewHolder(view);
        gmStatisticProductViewHolder = new GMStatisticProductViewHolder(view);
        gmStatisticTransactionViewHolder = new GMStatisticTransactionViewHolder(view);
        gmStatisticMarketInsightViewHolder = new GmStatisticMarketInsightViewHolder(view);
        gmStatisticBuyerViewHolder = new GmStatisticBuyerViewHolder(view);

        // analytic below : https://phab.tokopedia.com/T18496
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.content_gmstat);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() + nestedScrollView.getPaddingBottom()
                        - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    UnifyTracking.eventScrollGMStat();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        disableDateLabelView();
    }

    @Override
    public void loadDataByDate(DatePickerViewModel datePickerViewModel) {
        loadDataByDate();
    }

    private void loadDataByDate(){
        resetToLoading();
        gmDashboardPresenter.fetchData(getStartDate(), getEndDate());
    }

    @Override
    public boolean isAllowToCompareDate() {
        return false;
    }

    private void resetToLoading() {
        gmStatisticSummaryViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticGrossViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticProductViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticTransactionViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticBuyerViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmStatisticMarketInsightViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
    }

    @Override
    public void onSuccessLoadTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph, boolean isGoldMerchant) {
        gmStatisticGrossViewHolder.setData(getTransactionGraph);
        gmStatisticTransactionViewHolder.bindData(getTransactionGraph.gmTransactionGraphViewModel.totalTransactionModel,
                isGoldMerchant);
    }

    @Override
    public void onErrorLoadTransactionGraph(Throwable t) {
        gmStatisticGrossViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        gmStatisticTransactionViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onSuccessLoadProductGraph(GetProductGraph getProductGraph) {
        gmStatisticSummaryViewHolder.setData(getProductGraph);
        UnifyTracking.eventLoadGMStat();
    }

    @Override
    public void onErrorLoadProductGraph(Throwable t) {
        gmStatisticSummaryViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onSuccessLoadPopularProduct(GetPopularProduct getPopularProduct) {
        gmStatisticProductViewHolder.bindData(getPopularProduct);
    }

    @Override
    public void onErrorLoadPopularProduct(Throwable t) {
        gmStatisticProductViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onSuccessLoadBuyerGraph(GetBuyerGraph getBuyerGraph) {
        gmStatisticBuyerViewHolder.bindData(getBuyerGraph);
    }

    @Override
    public void onErrorLoadBuyerGraph(Throwable t) {
        gmStatisticBuyerViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onGetShopCategoryEmpty(boolean goldMerchant) {
        gmStatisticMarketInsightViewHolder.bindNoShopCategory(goldMerchant);
    }

    @Override
    public void onSuccessGetKeyword(List<GetKeyword> getKeywords, boolean isGoldMerchant) {
        gmStatisticMarketInsightViewHolder.bindData(getKeywords, isGoldMerchant);
    }

    @Override
    public void onSuccessGetCategory(String categoryName) {
        gmStatisticMarketInsightViewHolder.bindCategory(categoryName);
    }

    @Override
    public void onErrorLoadMarketInsight(Throwable t) {
        gmStatisticMarketInsightViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showSnackbarRetry();
    }

    @Override
    public void onErrorLoadShopInfo(Throwable t) {
        showSnackbarRetry();
    }

    @Override
    public void onSuccessLoadShopInfo(boolean isGoldMerchant) {
        if (isGoldMerchant) {
            enableDateLabelView();
        } else {
            disableDateLabelView();
        }
    }

    private void showSnackbarRetry() {
        if (snackbarRetry == null) {
            snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    loadDataByDate();
                }
            });
            snackbarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
        }
        //!important, the delay will help the snackbar re-show after it is being hidden.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    snackbarRetry.showRetrySnackbar();
                }
            }
        },700);
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