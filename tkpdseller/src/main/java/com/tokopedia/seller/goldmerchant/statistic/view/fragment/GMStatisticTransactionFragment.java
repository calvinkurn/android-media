package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.view.activity.GMStatisticTransactionTableActivity;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMTopAdsAmountViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMTransactionGraphViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.UnFinishedTransactionViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.listener.GMStatisticTransactionView;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionPresenter;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.topads.dashboard.data.model.data.DataDeposit;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDashboardActivity;

import javax.inject.Inject;

/**
 * @author normansyahputa on 7/6/17.
 */

public class GMStatisticTransactionFragment extends GMStatisticBaseDatePickerFragment implements GMStatisticTransactionView,
        GMTopAdsAmountViewHolder.OnTopAdsViewHolderListener {
    public static final String TAG = "GMStatisticTransactionF";

    @Inject
    GMStatisticTransactionPresenter presenter;

    private SnackbarRetry snackbarRetry;

    private GMTransactionGraphViewHolder gmTransactionGraphViewHolder;
    private UnFinishedTransactionViewHolder finishedTransactionViewHolder;
    private GMTopAdsAmountViewHolder gmTopAdsAmountViewHolder;

    public static Fragment createInstance() {
        return new GMStatisticTransactionFragment();
    }

    @Override
    protected void initInjector() {
        DaggerGMTransactionComponent
                .builder()
                .goldMerchantComponent(getComponent(GoldMerchantComponent.class))
                .gMStatisticModule(new GMStatisticModule())
                .build().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gm_statistic_transaction, container, false);
        presenter.attachView(this);
        gmTopAdsAmountViewHolder = new GMTopAdsAmountViewHolder(view);
        gmTopAdsAmountViewHolder.setOnTopAdsViewHolderListener(this);
        gmTransactionGraphViewHolder = new GMTransactionGraphViewHolder(view);
        finishedTransactionViewHolder = new UnFinishedTransactionViewHolder(view);

        LabelView gmStatisticProductListText = (LabelView) view.findViewById(R.id.gm_statistic_label_sold_product_list_view);
        gmStatisticProductListText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GMStatisticTransactionTableActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void loadDataByDate(DatePickerViewModel datePickerViewModel) {
        super.loadDataByDate(datePickerViewModel);
        loadingState(LoadingStateView.VIEW_LOADING);
        presenter.loadDataWithDate(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());
    }

    private void loadingState(int state) {
        gmTransactionGraphViewHolder.setViewState(state);
        gmTopAdsAmountViewHolder.setViewState(state);
        finishedTransactionViewHolder.setViewState(state);
    }

    @Override
    public void onSuccessLoadTransactionGraph(GMTransactionGraphMergeModel mergeModel) {
        gmTransactionGraphViewHolder.bind(mergeModel.gmTransactionGraphViewModel, isCompareDate());
        finishedTransactionViewHolder.bind(mergeModel.unFinishedTransactionViewModel);
    }

    @Override
    public void onErrorLoadTransactionGraph(Throwable t) {
        gmTransactionGraphViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        finishedTransactionViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showRetrySnackBar();
    }

    @Override
    public void bindTopAds(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewHolder.bind(gmTopAdsAmountViewModel, isCompareDate());
    }

    @Override
    public void bindTopAdsCreditNotUsed(GMGraphViewModel gmTopAdsAmountViewModel, DataDeposit dataDeposit) {
        gmTopAdsAmountViewHolder.bindTopAdsCreditNotUsed(gmTopAdsAmountViewModel, dataDeposit);
    }

    @Override
    public void onErrorLoadTopAdsGraph(Throwable t) {
        gmTopAdsAmountViewHolder.setViewState(LoadingStateView.VIEW_ERROR);
        showRetrySnackBar();
    }

    private void showRetrySnackBar() {
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

    public void loadDataByDate() {
        resetToLoading();
        presenter.loadDataWithDate(getStartDate(), getEndDate());
    }

    private void resetToLoading(){
        gmTopAdsAmountViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        finishedTransactionViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
        gmTransactionGraphViewHolder.setViewState(LoadingStateView.VIEW_LOADING);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onManageTopAdsClicked() {
        Intent intent = new Intent(getActivity(), TopAdsDashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean isAllowToCompareDate() {
        return true;
    }
}