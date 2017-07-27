package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMDateRangeDateViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.topads.dashboard.data.model.data.DataDeposit;
import com.tokopedia.seller.topads.dashboard.data.model.response.DataResponse;
import com.tokopedia.seller.topads.dashboard.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.dashboard.domain.interactor.ListenerInteractor;

import java.util.HashMap;

import rx.Subscriber;

/**
 * @author normansyahputa on 7/18/17.
 *
 *
 */
public class GMStatisticTransactionPresenterImpl extends GMStatisticTransactionPresenter {
    private GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase;
    private DashboardTopadsInteractor dashboardTopadsInteractor;
    private SessionHandler sessionHandler;

    private GMDateRangeDateViewModel gmDateRangeDateViewModel;

    public GMStatisticTransactionPresenterImpl(
            GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase,
            DashboardTopadsInteractor dashboardTopadsInteractor,
            SessionHandler sessionHandler) {
        super();
        this.gmStatGetTransactionGraphUseCase = gmStatGetTransactionGraphUseCase;
        this.dashboardTopadsInteractor = dashboardTopadsInteractor;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void startTransactionProductList() {
        if (isViewAttached()) {
            getView().startTransactionProductList(
                    gmDateRangeDateViewModel.getStartDate(),
                    gmDateRangeDateViewModel.getEndDate()
            );
        }
    }

    @Override
    public void loadDataWithDate(long startDate, long endDate) {
        gmStatGetTransactionGraphUseCase.execute(GMStatGetTransactionGraphUseCase.createRequestParam(startDate, endDate), new Subscriber<GMTransactionGraphMergeModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GMTransactionGraphMergeModel mergeModel) {
                fetchTopAdsDeposit(mergeModel.gmTopAdsAmountViewModel);
                revealData(mergeModel);
            }
        });
    }

    private void revealData(GMTransactionGraphMergeModel mergeModel) {
        if (isViewAttached()) {
            // get necessary object, just take from transaction graph view
            gmDateRangeDateViewModel = mergeModel.gmTransactionGraphViewModel.grossRevenueModel.dateRangeModel;
            getView().revealData(mergeModel);
        }
    }

    private void fetchTopAdsDeposit(final GMGraphViewModel gmTopAdsAmountViewModel) {
        HashMap<String, String> param = new HashMap<>();
        param.put("shop_id", sessionHandler.getShopID());
        dashboardTopadsInteractor.getDashboardResponse(param, new ListenerInteractor<DataResponse<DataDeposit>>() {
            @Override
            public void onSuccess(DataResponse<DataDeposit> dataDepositDataResponse) {
                if (isViewAttached()) {
                    DataDeposit data = dataDepositDataResponse.getData();
                    if (data.isAdUsage()) {
                        if (isNoAdsData(gmTopAdsAmountViewModel)) {
                            getView().bindTopAdsNoData(gmTopAdsAmountViewModel);
                        } else {
                            getView().bindTopAds(gmTopAdsAmountViewModel);
                        }
                    } else {
                        if (gmTopAdsAmountViewModel.amount == 0) {
                            getView().bindNoTopAdsCredit(gmTopAdsAmountViewModel);
                        } else {
                            getView().bindTopAdsCreditNotUsed(gmTopAdsAmountViewModel);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().bindTopAdsNoData(gmTopAdsAmountViewModel);
                }
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
    public void detachView() {
        super.detachView();
        gmStatGetTransactionGraphUseCase.unsubscribe();
        dashboardTopadsInteractor.unSubscribe();
    }
}