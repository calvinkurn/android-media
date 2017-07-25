package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
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
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionPresenter;
import com.tokopedia.seller.lib.widget.LabelView;

import javax.inject.Inject;

/**
 * @author normansyahputa on 7/6/17.
 */

public class GMStatisticTransactionFragment extends GMStatisticBaseDatePickerFragment implements GMStatisticTransactionView {
    public static final String TAG = "GMStatisticTransactionF";

    @Inject
    GMStatisticTransactionPresenter presenter;

    private LabelView gmStatisticProductListText;

    private GMTransactionGraphViewHolder gmTransactionGraphViewHolder;
    private GMTopAdsAmountViewHolder gmTopAdsAmountViewHolder;
    private UnFinishedTransactionViewHolder finishedTransactionViewHolder;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gmTopAdsAmountViewHolder = new GMTopAdsAmountViewHolder(getActivity());
        gmTransactionGraphViewHolder = new GMTransactionGraphViewHolder(getActivity());
        finishedTransactionViewHolder = new UnFinishedTransactionViewHolder(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gm_statistic_transaction, container, false);
        presenter.attachView(this);
        gmTopAdsAmountViewHolder.initView(view);
        gmTransactionGraphViewHolder.initView(view);
        finishedTransactionViewHolder.initView(view);

        gmStatisticProductListText = (LabelView) view.findViewById(R.id.gm_statistic_label_sold_product_list_view);
        gmStatisticProductListText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.startTransactionProductList();
                }
            }
        });
        return view;
    }

    @Override
    public void startTransactionProductList(long startDate, long endDate) {
        Intent intent = new Intent(getActivity(), GMStatisticTransactionTableActivity.class);
        startActivity(intent);
    }

    @Override
    public void loadData() {
        super.loadData();
        loadingState(LoadingStateView.VIEW_LOADING);
        presenter.loadDataWithDate(datePickerViewModel.getStartDate(), datePickerViewModel.getEndDate());
    }

    private void loadingState(int state) {
        gmTransactionGraphViewHolder.setLoadingState(state);
        gmTopAdsAmountViewHolder.setLoadingState(state);
        finishedTransactionViewHolder.setLoadingState(state);
    }

    @Override
    public void revealData(GMTransactionGraphMergeModel mergeModel) {
        gmTransactionGraphViewHolder.bind(mergeWithIsCompare(mergeModel.gmTransactionGraphViewModel));
        finishedTransactionViewHolder.bind(mergeModel.unFinishedTransactionViewModel);
    }

    private GMTransactionGraphViewModel mergeWithIsCompare(GMTransactionGraphViewModel viewModel) {
        viewModel.grossRevenueModel.isCompare = datePickerViewModel.isCompareDate();
        viewModel.totalTransactionModel.isCompare = datePickerViewModel.isCompareDate();
        viewModel.successTransactionModel.isCompare = datePickerViewModel.isCompareDate();
        viewModel.netRevenueModel.isCompare = datePickerViewModel.isCompareDate();
        viewModel.rejectedAmountModel.isCompare = datePickerViewModel.isCompareDate();
        viewModel.shippingCostModel.isCompare = datePickerViewModel.isCompareDate();
        viewModel.rejectTransactionModel.isCompare = datePickerViewModel.isCompareDate();
        return viewModel;
    }

    @Override
    public void bindTopAdsNoData(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewHolder.bindNoData(setStaticText(gmTopAdsAmountViewModel));
    }

    @Override
    public void bindTopAds(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewHolder.bind(setStaticText(gmTopAdsAmountViewModel));
    }

    @Override
    public void bindNoTopAdsCredit(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewHolder.bindNoTopAdsCredit(setStaticText(gmTopAdsAmountViewModel));
    }

    @Override
    public void bindTopAdsCreditNotUsed(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewHolder.bindTopAdsCreditNotUsed(setStaticText(gmTopAdsAmountViewModel));
    }

    private GMGraphViewModel setStaticText(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewModel.title = getString(R.string.gold_merchant_top_ads_amount_title_text);
        gmTopAdsAmountViewModel.subtitle = getString(R.string.gold_merchant_top_ads_amount_subtitle_text);

        return gmTopAdsAmountViewModel;
    }

    @Override
    public boolean isComparedDate() {
        return true;
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
}