package com.tokopedia.gm.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.R;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.statistic.di.component.DaggerGMStatisticTransactionComponent;
import com.tokopedia.gm.statistic.di.module.GMStatisticModule;
import com.tokopedia.gm.statistic.view.activity.GMStatisticTransactionTableActivity;
import com.tokopedia.gm.statistic.view.holder.GMTopAdsAmountViewHolder;
import com.tokopedia.gm.statistic.view.holder.GMTransactionGraphViewHolder;
import com.tokopedia.gm.statistic.view.holder.GMUnFinishedTransactionViewHolder;
import com.tokopedia.gm.statistic.view.listener.GMStatisticTransactionView;
import com.tokopedia.gm.statistic.view.model.GMGraphViewModel;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.gm.statistic.view.presenter.GMStatisticTransactionPresenter;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.seller.common.widget.LabelView;

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
    private GMUnFinishedTransactionViewHolder finishedTransactionViewHolder;
    private GMTopAdsAmountViewHolder gmTopAdsAmountViewHolder;

    public static Fragment createInstance() {
        return new GMStatisticTransactionFragment();
    }

    @Override
    protected void initInjector() {
        DaggerGMStatisticTransactionComponent
                .builder()
                .gMComponent(getComponent(GMComponent.class))
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
        finishedTransactionViewHolder = new GMUnFinishedTransactionViewHolder(view);

        LabelView gmStatisticProductListText = (LabelView) view.findViewById(R.id.gm_statistic_label_sold_product_list_view);
        gmStatisticProductListText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventClickGMStatProductSoldTransaction();

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
        presenter.loadDataWithDate(getGmModuleRouter(), datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());
    }

    private void loadingState(int state) {
        gmTransactionGraphViewHolder.setViewState(state);
        gmTopAdsAmountViewHolder.setViewState(state);
        finishedTransactionViewHolder.setViewState(state);
    }

    @Override
    public void onSuccessLoadTransactionGraph(GMTransactionGraphMergeModel mergeModel) {
        gmTransactionGraphViewHolder.bind(mergeModel.gmTransactionGraphViewModel, isCompareDate());
        finishedTransactionViewHolder.bind(mergeModel.GMUnFinishedTransactionViewModel);
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
        presenter.loadDataWithDate(getGmModuleRouter(), getStartDate(), getEndDate());
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
        GMModuleRouter gmModuleRouter = getGmModuleRouter();
        if (gmModuleRouter != null) {
            gmModuleRouter.goToTopAdsDashboard(getActivity());
        }
    }

    @Override
    public boolean isAllowToCompareDate() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == DatePickerConstant.REQUEST_CODE_DATE && intent != null) {
            if (isCompareDate()) {
                UnifyTracking.eventClickGMStatDatePickerWCompareTransaction();
            } else {
                UnifyTracking.eventClickGMStatDatePickerWOCompareTransaction();
            }
        }
    }

    private GMModuleRouter getGmModuleRouter() {
        if (getActivity().getApplication() instanceof GMModuleRouter) {
            return (GMModuleRouter) getActivity().getApplication();
        }
        return null;
    }
}