package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.GMStatConstant;
import com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionGraphType;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatApi;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.activity.GMStatisticTransactionTableActivity;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMTopAdsAmountViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMTransactionGraphViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMDateRangeDateViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewWithPreviousModel;
import com.tokopedia.seller.goldmerchant.statistic.view.holder.UnFinishedTransactionViewHolder;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.UnFinishedTransactionViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTableView;
import com.tokopedia.seller.lib.widget.LabelView;

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

public class GMStatisticTransactionFragment extends GMStatisticBaseDatePickerFragment {
    public static final String TAG = "GMStatisticTransactionF";

    @Inject
    ImageHandler imageHandler;

    @Inject
    GMStatApi gmStatApi;

    @Inject
    SessionHandler sessionHandler;

    private View rootView;

    private String[] monthNamesAbrev;

    private GMTopAdsAmountViewHelper gmTopAdsAmountViewHelper;
    private LabelView gmStatisticProductListText;
    private GMTransactionGraphViewHelper gmTransactionGraphViewHelper;
    private UnFinishedTransactionViewHolder unFinishedTransactionViewHolder;

    private GMDateRangeDateViewModel gmDateRangeDateViewModel;
    private GMDateRangeDateViewModel previousGmDateRangeDateViewModel;

    public static Fragment createInstance() {
        return new GMStatisticTransactionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gm_statistic_transaction, container, false);
        dateLabelView = (DateLabelView) rootView.findViewById(R.id.date_label_view);
        initVar();
        initView();
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
    protected void initInjector() {
        DaggerGMTransactionComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .build().inject(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        gmStatApi.getTransactionGraph(sessionHandler.getShopID(), new HashMap<String, String>())
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

                        gmDateRangeDateViewModel = getGmDateRangeDateViewModel(dateGraph);
                        if (gmDateRangeDateViewModel == null) return;
                        gmDateRangeDateViewModel.dumpStartDateLong();
                        gmDateRangeDateViewModel.dumpEndDateLong();

                        previousGmDateRangeDateViewModel = getGmDateRangeDateViewModel(getTransactionGraph.getPDateGraph());

                        GMTransactionGraphViewModel gmTransactionGraphViewModel
                                = new GMTransactionGraphViewModel();

                        for (@GMTransactionGraphType int i = 0; i < 7; i++) {
                            GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel
                                    = new GMGraphViewWithPreviousModel();
                            gmGraphViewWithPreviousModel.isCompare = true;
                            gmGraphViewWithPreviousModel.dates = dateGraph;
                            gmGraphViewWithPreviousModel.pDates = getTransactionGraph.getPDateGraph();
                            gmGraphViewWithPreviousModel.dateRangeModel = gmDateRangeDateViewModel;
                            gmGraphViewWithPreviousModel.pDateRangeModel = previousGmDateRangeDateViewModel;
                            fillGMTransactionGraphViewModel(i, gmGraphViewWithPreviousModel, getTransactionGraph);
                            fillPreviousGMTransactionGraphViewModel(i, gmGraphViewWithPreviousModel, getTransactionGraph);
                            putGMTransactionGraphViewModel(i, gmGraphViewWithPreviousModel, gmTransactionGraphViewModel);
                        }

                        gmTransactionGraphViewHelper.bind(gmTransactionGraphViewModel);

                        processTopAdsAmount(getTransactionGraph, dateGraph);

                        processUnfinishedTransaction(getTransactionGraph);
                    }
                });
    }

    private void processUnfinishedTransaction(GetTransactionGraph getTransactionGraph) {
        UnFinishedTransactionViewModel unFinishedTransactionViewModel = new UnFinishedTransactionViewModel(
                getTransactionGraph.getOnholdCount(),
                getTransactionGraph.getResoCount(),
                getTransactionGraph.getOnholdAmt(),
                getString(R.string.rupiah_format_text)
        );
        unFinishedTransactionViewHolder.bind(unFinishedTransactionViewModel);
    }

    @Nullable
    protected GMDateRangeDateViewModel getGmDateRangeDateViewModel(List<Integer> dateGraph) {
        Pair<Long, String> startDateString = GoldMerchantDateUtils.getDateStringWithoutYear(dateGraph,
                GMStatisticTransactionFragment.this.monthNamesAbrev,
                0);
        Pair<Long, String> endDateString = GoldMerchantDateUtils.getDateString(dateGraph,
                GMStatisticTransactionFragment.this.monthNamesAbrev,
                dateGraph.size() - 1);
        if (startDateString.getModel2() == null || endDateString.getModel2() == null)
            return null;

        // create object for range date.
        GMDateRangeDateViewModel gmDateRangeDateViewModel
                = new GMDateRangeDateViewModel();
        gmDateRangeDateViewModel.setStartDate(startDateString);
        gmDateRangeDateViewModel.setEndDate(endDateString);
        return gmDateRangeDateViewModel;
    }

    private void putGMTransactionGraphViewModel(
            @GMTransactionGraphType int gmTransactionType, GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel, GMTransactionGraphViewModel gmTransactionGraphViewModel) {
        switch (gmTransactionType) {
            case GMTransactionGraphType.GROSS_REVENUE:
                gmTransactionGraphViewModel.grossRevenueModel = gmGraphViewWithPreviousModel;
                break;
            case GMTransactionGraphType.NET_REVENUE:
                gmTransactionGraphViewModel.netRevenueModel = gmGraphViewWithPreviousModel;
                break;
            case GMTransactionGraphType.REJECT_TRANS:
                gmTransactionGraphViewModel.rejectTransactionModel = gmGraphViewWithPreviousModel;
                break;
            case GMTransactionGraphType.REJECTED_AMOUNT:
                gmTransactionGraphViewModel.rejectedAmountModel = gmGraphViewWithPreviousModel;
                break;
            case GMTransactionGraphType.SHIPPING_COST:
                gmTransactionGraphViewModel.shippingCostModel = gmGraphViewWithPreviousModel;
                break;
            case GMTransactionGraphType.SUCCESS_TRANS:
                gmTransactionGraphViewModel.successTransactionModel = gmGraphViewWithPreviousModel;
                break;
            case GMTransactionGraphType.TOTAL_TRANSACTION:
                gmTransactionGraphViewModel.totalTransactionModel = gmGraphViewWithPreviousModel;
                break;
        }
    }

    private void fillPreviousGMTransactionGraphViewModel(
            @GMTransactionGraphType int gmTransactionType, GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel, GetTransactionGraph getTransactionGraph) {
        switch (gmTransactionType) {
            case GMTransactionGraphType.GROSS_REVENUE:
                gmGraphViewWithPreviousModel.pValues = getTransactionGraph.getPGrossGraph();
                break;
            case GMTransactionGraphType.NET_REVENUE:
                gmGraphViewWithPreviousModel.pValues = getTransactionGraph.getPNetGraph();
                break;
            case GMTransactionGraphType.REJECT_TRANS:
                gmGraphViewWithPreviousModel.pValues = getTransactionGraph.getPRejectedTransGraph();
                break;
            case GMTransactionGraphType.REJECTED_AMOUNT:
                gmGraphViewWithPreviousModel.pValues = getTransactionGraph.getPRejectedAmtGraph();
                break;
            case GMTransactionGraphType.SHIPPING_COST:
                gmGraphViewWithPreviousModel.pValues = getTransactionGraph.getPShippingGraph();
                break;
            case GMTransactionGraphType.SUCCESS_TRANS:
                gmGraphViewWithPreviousModel.pValues = getTransactionGraph.getPSuccessTransGraph();
                break;
            case GMTransactionGraphType.TOTAL_TRANSACTION:
                gmGraphViewWithPreviousModel.pValues = GMStatisticUtil.sumTwoGraph(
                        getTransactionGraph.getPSuccessTransGraph(),
                        getTransactionGraph.getPRejectedTransGraph());
                break;
        }
    }

    private void fillGMTransactionGraphViewModel(
            @GMTransactionGraphType int gmTransactionType, GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel, GetTransactionGraph getTransactionGraph) {
        switch (gmTransactionType) {
            case GMTransactionGraphType.GROSS_REVENUE:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getGrossGraph();
                gmGraphViewWithPreviousModel.percentage = getTransactionGraph.getDiffGrossRevenue();
                gmGraphViewWithPreviousModel.amount = getTransactionGraph.getGrossRevenue();
                break;
            case GMTransactionGraphType.NET_REVENUE:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getNetGraph();
                gmGraphViewWithPreviousModel.percentage = getTransactionGraph.getDiffNetRevenue();
                gmGraphViewWithPreviousModel.amount = getTransactionGraph.getNetRevenue();
                break;
            case GMTransactionGraphType.REJECT_TRANS:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getRejectedTransGraph();
                gmGraphViewWithPreviousModel.percentage = getTransactionGraph.getDiffRejectedTrans();
                gmGraphViewWithPreviousModel.amount = getTransactionGraph.getRejectedTrans();
                break;
            case GMTransactionGraphType.REJECTED_AMOUNT:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getRejectedAmtGraph();
                gmGraphViewWithPreviousModel.percentage = getTransactionGraph.getDiffRejectedAmount();
                gmGraphViewWithPreviousModel.amount = getTransactionGraph.getRejectedAmount();
                break;
            case GMTransactionGraphType.SHIPPING_COST:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getShippingGraph();
                gmGraphViewWithPreviousModel.percentage = getTransactionGraph.getDiffShippingCost();
                gmGraphViewWithPreviousModel.amount = getTransactionGraph.getShippingCost();
                break;
            case GMTransactionGraphType.SUCCESS_TRANS:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getSuccessTransGraph();
                gmGraphViewWithPreviousModel.percentage = getTransactionGraph.getDiffSuccessTrans();
                gmGraphViewWithPreviousModel.amount = getTransactionGraph.getSuccessTrans();
                break;
            case GMTransactionGraphType.TOTAL_TRANSACTION:
                gmGraphViewWithPreviousModel.values = GMStatisticUtil.sumTwoGraph(
                        getTransactionGraph.getSuccessTransGraph(),
                        getTransactionGraph.getRejectedTransGraph());
                gmGraphViewWithPreviousModel.percentage = getTransactionGraph.getDiffFinishedTrans();
                gmGraphViewWithPreviousModel.amount = getTransactionGraph.getFinishedTrans();
                break;
        }
    }

    protected void processTopAdsAmount(GetTransactionGraph getTransactionGraph, List<Integer> dateGraph) {
        GMGraphViewModel gmTopAdsAmountViewModel
                = new GMGraphViewModel();
        gmTopAdsAmountViewModel.dates = dateGraph;
        gmTopAdsAmountViewModel.values = joinAdsGraph(getTransactionGraph.getAdsPGraph(), getTransactionGraph.getAdsSGraph());
        gmTopAdsAmountViewModel.title = getString(R.string.gold_merchant_top_ads_amount_title_text);
        gmTopAdsAmountViewModel.subtitle = getString(R.string.gold_merchant_top_ads_amount_subtitle_text);

        gmTopAdsAmountViewModel.amount = getTransactionGraph.getCpcProduct() + getTransactionGraph.getCpcShop();

        long previousAmount = getTransactionGraph.getpCpcProduct() + getTransactionGraph.getpCpcShop();
        try {
            gmTopAdsAmountViewModel.percentage = gmTopAdsAmountViewModel.amount - previousAmount / gmTopAdsAmountViewModel.amount;
        } catch (Exception e) {
            Log.e(TAG, String.format("amount %s previous amount %s", Integer.toString(gmTopAdsAmountViewModel.amount), Long.toString(previousAmount)));
            gmTopAdsAmountViewModel.percentage = -GMStatConstant.NoDataAvailable * 100;
        }

        gmTopAdsAmountViewHelper.bind(
                gmTopAdsAmountViewModel
        );
    }

    private List<Integer> joinAdsGraph(List<Integer> adsPGraph, List<Integer> adsSGraph) {
        return GMStatisticUtil.sumTwoGraph(adsPGraph, adsSGraph);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
