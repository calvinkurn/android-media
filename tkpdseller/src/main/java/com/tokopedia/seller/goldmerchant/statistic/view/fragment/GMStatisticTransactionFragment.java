package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.view.activity.GMStatisticTransactionTableActivity;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMTopAdsAmountViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.GMTransactionGraphViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.UnFinishedTransactionViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionPresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTableView;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionView;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.topads.dashboard.domain.interactor.DashboardTopadsInteractor;

import javax.inject.Inject;

/**
 * @author normansyahputa on 7/6/17.
 */

public class GMStatisticTransactionFragment extends GMStatisticBaseDatePickerFragment implements GMStatisticTransactionView {
    public static final String TAG = "GMStatisticTransactionF";

    @Inject
    GMStatisticTransactionPresenter presenter;

    @Inject
    SessionHandler sessionHandler;

    @Inject
    DashboardTopadsInteractor dashboardTopadsInteractor;

    private View rootView;

    private GMTopAdsAmountViewHolder gmTopAdsAmountViewHelper;
    private LabelView gmStatisticProductListText;
    private GMTransactionGraphViewHolder gmTransactionGraphViewHelper;
    private UnFinishedTransactionViewHolder unFinishedTransactionViewHolder;

    public static Fragment createInstance() {
        return new GMStatisticTransactionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gm_statistic_transaction, container, false);
        initVar();
        initView();
        loadData();
        return rootView;
    }

    private void initVar() {

        gmTopAdsAmountViewHelper = new GMTopAdsAmountViewHolder(getActivity());
        gmTransactionGraphViewHelper = new GMTransactionGraphViewHolder(getActivity());
        unFinishedTransactionViewHolder = new UnFinishedTransactionViewHolder(getActivity());
    }

    private void initView() {
        if (rootView == null)
            return;

        presenter.attachView(this);
        gmTopAdsAmountViewHelper.initView(rootView);
        gmTransactionGraphViewHelper.initView(rootView);
        unFinishedTransactionViewHolder.initView(rootView);

        gmStatisticProductListText = (LabelView) rootView.findViewById(R.id.gm_statistic_label_sold_product_list_view);
        gmStatisticProductListText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.startTransactionProductList();
                }
            }
        });
    }

    @Override
    public void startTransactionProductList(long startDate, long endDate) {
        Intent intent = new Intent(GMStatisticTransactionFragment.this.getActivity(), GMStatisticTransactionTableActivity.class);
        intent.putExtra(GMStatisticTransactionTableView.START_DATE, startDate);
        intent.putExtra(GMStatisticTransactionTableView.END_DATE, endDate);
        startActivity(intent);
    }

    @Override
    protected void loadData() {
        presenter.loadDataWithoutDate();
    }

    @Override
    protected Intent getDatePickerIntent() {
        return null;
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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void revealData(GMTransactionGraphMergeModel mergeModel) {
        gmTransactionGraphViewHelper.bind(mergeModel.gmTransactionGraphViewModel);
        unFinishedTransactionViewHolder.bind(mergeModel.unFinishedTransactionViewModel);
    }

    @Override
    public void bindTopAdsNoData(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewHelper.bindNoData(gmTopAdsAmountViewModel);
    }

    @Override
    public void bindTopAds(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewHelper.bind(gmTopAdsAmountViewModel);
    }

    @Override
    public void bindNoTopAdsCredit(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewHelper.bindNoTopAdsCredit(gmTopAdsAmountViewModel);
    }

    @Override
    public void bindTopAdsCreditNotUsed(GMGraphViewModel gmTopAdsAmountViewModel) {
        gmTopAdsAmountViewHelper.bindTopAdsCreditNotUsed(gmTopAdsAmountViewModel);
    }
}
