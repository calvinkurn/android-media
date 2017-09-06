package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.dashboard.data.model.data.DataDeposit;
import com.tokopedia.seller.common.data.response.DataResponse;
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
    public void loadDataWithDate(long startDate, long endDate) {
        gmStatGetTransactionGraphUseCase.execute(GMStatGetTransactionGraphUseCase.createRequestParam(startDate, endDate), new Subscriber<GMTransactionGraphMergeModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().onErrorLoadTransactionGraph(throwable);
                    getView().onErrorLoadTopAdsGraph(throwable);
                }
            }

            @Override
            public void onNext(GMTransactionGraphMergeModel mergeModel) {
                fetchTopAdsDeposit(mergeModel.gmTopAdsAmountViewModel);
                // get necessary object, just take from transaction graph view
                getView().onSuccessLoadTransactionGraph(mergeModel);
            }
        });
    }

    private void fetchTopAdsDeposit(final GMGraphViewModel gmTopAdsAmountViewModel) {
        HashMap<String, String> param = new HashMap<>();
        param.put(TopAdsNetworkConstant.PARAM_SHOP_ID, sessionHandler.getShopID());
        dashboardTopadsInteractor.getDashboardResponse(param, new ListenerInteractor<DataResponse<DataDeposit>>() {
            @Override
            public void onSuccess(DataResponse<DataDeposit> dataDepositDataResponse) {
                DataDeposit dataDeposit = dataDepositDataResponse.getData();
                if (dataDeposit.isAdUsage()) {
                    getView().bindTopAds(gmTopAdsAmountViewModel);
                } else {
                    getView().bindTopAdsCreditNotUsed(gmTopAdsAmountViewModel, dataDeposit);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().onErrorLoadTopAdsGraph(throwable);
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        gmStatGetTransactionGraphUseCase.unsubscribe();
        dashboardTopadsInteractor.unSubscribe();
    }
}