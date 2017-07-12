package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.GoldMerchantDateUtils;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionGraphType;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatApi;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticUtil;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMTopAdsAmountViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMTransactionGraphViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMDateRangeDateViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewWithPreviousModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphViewModel;
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

public class GMStatisticTransactionFragment extends BaseDaggerFragment {
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

    public static Fragment createInstance() {
        return new GMStatisticTransactionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gm_statistic_transaction, container, false);
        initVar();
        initView();
        return rootView;
    }

    private void initVar() {
        monthNamesAbrev = rootView.getResources().getStringArray(R.array.lib_date_picker_month_entries);

        gmTopAdsAmountViewHelper = new GMTopAdsAmountViewHelper(getActivity());
        gmTransactionGraphViewHelper = new GMTransactionGraphViewHelper(getActivity());
    }

    private void initView() {
        if (rootView == null)
            return;

        gmTopAdsAmountViewHelper.initView(rootView);
        gmTransactionGraphViewHelper.initView(rootView);

        gmStatisticProductListText = (LabelView) rootView.findViewById(R.id.gm_statistic_label_sold_product_list_view);
        gmStatisticProductListText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        Pair<Long, String> startDateString = GoldMerchantDateUtils.getDateStringWithoutYear(dateGraph,
                                GMStatisticTransactionFragment.this.monthNamesAbrev,
                                0);
                        Pair<Long, String> endDateString = GoldMerchantDateUtils.getDateString(dateGraph,
                                GMStatisticTransactionFragment.this.monthNamesAbrev,
                                dateGraph.size() - 1);
                        if (startDateString.getModel2() == null || endDateString.getModel2() == null)
                            return;

                        // create object for range date.
                        GMDateRangeDateViewModel gmDateRangeDateViewModel
                                = new GMDateRangeDateViewModel();
                        gmDateRangeDateViewModel.setStartDate(startDateString);
                        gmDateRangeDateViewModel.setEndDate(endDateString);


                        GMTransactionGraphViewModel gmTransactionGraphViewModel
                                = new GMTransactionGraphViewModel();

                        for (@GMTransactionGraphType int i = 0; i < 7; i++) {
                            GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel
                                    = new GMGraphViewWithPreviousModel();
                            gmGraphViewWithPreviousModel.dates = dateGraph;
                            gmGraphViewWithPreviousModel.dateRangeModel = gmDateRangeDateViewModel;
                            fillGMTransactionGraphViewModel(i, gmGraphViewWithPreviousModel, getTransactionGraph);
                            putGMTransactionGraphViewModel(i, gmGraphViewWithPreviousModel, gmTransactionGraphViewModel);
                        }

                        gmTransactionGraphViewHelper.bind(gmTransactionGraphViewModel);

                        processTopAdsAmount(getTransactionGraph, dateGraph);
                    }
                });

        gmStatApi.getTransactionTable(sessionHandler.getShopID(), new HashMap<String, String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<GetTransactionTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Response<GetTransactionTable> stringResponse) {
                        Log.e(TAG, stringResponse.body().toString());
                    }
                });
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

    private void fillGMTransactionGraphViewModel(
            @GMTransactionGraphType int gmTransactionType, GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel, GetTransactionGraph getTransactionGraph) {
        switch (gmTransactionType) {
            case GMTransactionGraphType.GROSS_REVENUE:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getGrossGraph();
                break;
            case GMTransactionGraphType.NET_REVENUE:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getNetGraph();
                break;
            case GMTransactionGraphType.REJECT_TRANS:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getRejectedTransGraph();
                break;
            case GMTransactionGraphType.REJECTED_AMOUNT:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getRejectedAmtGraph();
                break;
            case GMTransactionGraphType.SHIPPING_COST:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getShippingGraph();
                break;
            case GMTransactionGraphType.SUCCESS_TRANS:
                gmGraphViewWithPreviousModel.values = getTransactionGraph.getSuccessTransGraph();
                break;
            case GMTransactionGraphType.TOTAL_TRANSACTION:
                gmGraphViewWithPreviousModel.values = GMStatisticUtil.sumTwoGraph(
                        getTransactionGraph.getSuccessTransGraph(),
                        getTransactionGraph.getRejectedTransGraph());
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

        // TODO get topads percentage maybe cpc, waiting for pcp
        gmTopAdsAmountViewModel.percentage = 1f;

        gmTopAdsAmountViewModel.amount = getTransactionGraph.getCpcProduct() + getTransactionGraph.getCpcShop();

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
