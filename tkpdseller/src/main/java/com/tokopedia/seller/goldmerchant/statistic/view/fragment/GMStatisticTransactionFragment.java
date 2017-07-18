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
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMTopAdsAmountViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMTransactionGraphViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMDateRangeDateViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.UnFinishedTransactionViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionPresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTableView;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionView;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.topads.dashboard.data.model.data.DataDeposit;
import com.tokopedia.seller.topads.dashboard.data.model.response.DataResponse;
import com.tokopedia.seller.topads.dashboard.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.dashboard.domain.interactor.ListenerInteractor;

import java.util.HashMap;

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

    private String[] monthNamesAbrev;

    private GMTopAdsAmountViewHelper gmTopAdsAmountViewHelper;
    private LabelView gmStatisticProductListText;
    private GMTransactionGraphViewHelper gmTransactionGraphViewHelper;
    private UnFinishedTransactionViewHolder unFinishedTransactionViewHolder;

    private GMDateRangeDateViewModel gmDateRangeDateViewModel;
    private GMDateRangeDateViewModel previousGmDateRangeDateViewModel;
    private DataDeposit data;

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
        monthNamesAbrev = getContext().getResources().getStringArray(R.array.lib_date_picker_month_entries);

        gmTopAdsAmountViewHelper = new GMTopAdsAmountViewHelper(getActivity());
        gmTransactionGraphViewHelper = new GMTransactionGraphViewHelper(getActivity());
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
                Intent intent = new Intent(GMStatisticTransactionFragment.this.getActivity(), GMStatisticTransactionTableActivity.class);
                intent.putExtra(GMStatisticTransactionTableView.START_DATE, gmDateRangeDateViewModel.getStartDate().getModel1());
                intent.putExtra(GMStatisticTransactionTableView.END_DATE, gmDateRangeDateViewModel.getEndDate().getModel1());
                startActivity(intent);
            }
        });
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

    protected void fetchTopAdsDeposit(final GMGraphViewModel gmTopAdsAmountViewModel) {
        HashMap<String, String> param = new HashMap<>();
        param.put("shop_id", sessionHandler.getShopID());
        dashboardTopadsInteractor.getDashboardResponse(param, new ListenerInteractor<DataResponse<DataDeposit>>() {
            @Override
            public void onSuccess(DataResponse<DataDeposit> dataDepositDataResponse) {
                DataDeposit data = dataDepositDataResponse.getData();
                if (data.isAdUsage()) {
                    if (isNoAdsData(gmTopAdsAmountViewModel)) {
                        gmTopAdsAmountViewHelper.bindNoData(gmTopAdsAmountViewModel);
                    } else {
                        gmTopAdsAmountViewHelper.bind(gmTopAdsAmountViewModel);
                    }
                } else {
                    if (gmTopAdsAmountViewModel.amount == 0) {
                        gmTopAdsAmountViewHelper.bindNoTopAdsCredit(gmTopAdsAmountViewModel);
                    } else {
                        gmTopAdsAmountViewHelper.bindTopAdsCreditNotUsed(gmTopAdsAmountViewModel);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    private boolean isNoAdsData(GMGraphViewModel data) {
        boolean isAllZero = true;
        for (int i = 0; i < data.values.size(); i++) {
            isAllZero = isAllZero && data.values.get(i) == 0;
        }
        return isAllZero;
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
        fetchTopAdsDeposit(mergeModel.gmTopAdsAmountViewModel);
        unFinishedTransactionViewHolder.bind(mergeModel.unFinishedTransactionViewModel);
    }
}
