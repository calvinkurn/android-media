package com.tokopedia.gm.statistic.domain.mapper;

import com.tokopedia.gm.statistic.constant.GMStatConstant;
import com.tokopedia.seller.common.williamchart.util.GoldMerchantDateUtils;
import com.tokopedia.gm.statistic.constant.GMTransactionGraphType;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.common.williamchart.util.GMStatisticUtil;
import com.tokopedia.gm.statistic.view.model.GMDateRangeDateViewModel;
import com.tokopedia.gm.statistic.view.model.GMGraphViewModel;
import com.tokopedia.gm.statistic.view.model.GMGraphViewWithPreviousModel;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphViewModel;
import com.tokopedia.gm.statistic.view.model.GMUnFinishedTransactionViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author normansyahputa on 7/18/17.
 */

public class GMTransactionStatDomainMapper implements Func1<GetTransactionGraph, GMTransactionGraphMergeModel> {

    private static final String TAG = "GMTransactionStatDomain";

    @Inject
    public GMTransactionStatDomainMapper() {
    }

    protected GMDateRangeDateViewModel getGmDateRangeDateViewModel2(List<Integer> dateGraph) {
        GMDateRangeDateViewModel gmDateRangeDateViewModel =
                new GMDateRangeDateViewModel();
        gmDateRangeDateViewModel.setStartDate(GoldMerchantDateUtils.getDateWithYear(dateGraph.get(0)));
        gmDateRangeDateViewModel.setEndDate(GoldMerchantDateUtils.getDateWithYear(dateGraph.get(dateGraph.size() - 1)));

        return gmDateRangeDateViewModel;
    }

    public GMTransactionGraphViewModel from(GetTransactionGraph getTransactionGraph) {

        // get range from network
        List<Integer> dateGraph = getTransactionGraph.getDateGraph();

        GMDateRangeDateViewModel gmDateRangeDateViewModel = getGmDateRangeDateViewModel2(dateGraph);
        GMDateRangeDateViewModel previousGmDateRangeDateViewModel = getGmDateRangeDateViewModel2(getTransactionGraph.getPDateGraph());

        GMTransactionGraphViewModel gmTransactionGraphViewModel
                = new GMTransactionGraphViewModel();

        for (@GMTransactionGraphType int i = 0; i < 7; i++) {
            GMGraphViewWithPreviousModel gmGraphViewWithPreviousModel
                    = new GMGraphViewWithPreviousModel();
            gmGraphViewWithPreviousModel.dates = dateGraph;
            gmGraphViewWithPreviousModel.pDates = getTransactionGraph.getPDateGraph();
            gmGraphViewWithPreviousModel.dateRangeModel = gmDateRangeDateViewModel;
            gmGraphViewWithPreviousModel.pDateRangeModel = previousGmDateRangeDateViewModel;
            fillGMTransactionGraphViewModel(i, gmGraphViewWithPreviousModel, getTransactionGraph);
            fillPreviousGMTransactionGraphViewModel(i, gmGraphViewWithPreviousModel, getTransactionGraph);
            putGMTransactionGraphViewModel(i, gmGraphViewWithPreviousModel, gmTransactionGraphViewModel);
        }

        return gmTransactionGraphViewModel;
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

    @Override
    public GMTransactionGraphMergeModel call(GetTransactionGraph getTransactionGraph) {
        GMTransactionGraphMergeModel gmTransactionGraphMergeModel = new GMTransactionGraphMergeModel();
        gmTransactionGraphMergeModel.gmTransactionGraphViewModel = from(getTransactionGraph);
        gmTransactionGraphMergeModel.gmTopAdsAmountViewModel = processTopAdsAmount(getTransactionGraph, getTransactionGraph.getDateGraph());
        gmTransactionGraphMergeModel.GMUnFinishedTransactionViewModel = processUnfinishedTransaction(getTransactionGraph);
        return gmTransactionGraphMergeModel;
    }

    private GMGraphViewModel processTopAdsAmount(GetTransactionGraph getTransactionGraph, List<Integer> dateGraph) {
        GMGraphViewModel gmTopAdsAmountViewModel
                = new GMGraphViewModel();
        gmTopAdsAmountViewModel.dates = dateGraph;
        gmTopAdsAmountViewModel.values = joinAdsGraph(getTransactionGraph.getAdsPGraph(), getTransactionGraph.getAdsSGraph());

        gmTopAdsAmountViewModel.amount = getTransactionGraph.getCpcProduct() + getTransactionGraph.getCpcShop();
        long previousAmount = getTransactionGraph.getpCpcProduct() + getTransactionGraph.getpCpcShop();
        try {
            gmTopAdsAmountViewModel.percentage = ( gmTopAdsAmountViewModel.amount - previousAmount) / previousAmount;
        } catch (Exception e) {
            gmTopAdsAmountViewModel.percentage = GMStatConstant.NO_DATA_AVAILABLE;
        }

        return gmTopAdsAmountViewModel;
    }

    private List<Integer> joinAdsGraph(List<Integer> adsPGraph, List<Integer> adsSGraph) {
        return GMStatisticUtil.sumTwoGraph(adsPGraph, adsSGraph);
    }

    private GMUnFinishedTransactionViewModel processUnfinishedTransaction(GetTransactionGraph getTransactionGraph) {
        return new GMUnFinishedTransactionViewModel(
                getTransactionGraph.getOnholdCount(),
                getTransactionGraph.getResoCount(),
                getTransactionGraph.getOnholdAmt()
        );
    }
}
