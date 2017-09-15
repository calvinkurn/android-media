package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import rx.Subscriber;

/**
 * @author normansyahputa on 7/18/17.
 */
public class GMStatisticTransactionPresenterImpl extends GMStatisticTransactionPresenter {
    private GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase;
    private SessionHandler sessionHandler;

    public GMStatisticTransactionPresenterImpl(
            GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase,
            SessionHandler sessionHandler) {
        super();
        this.gmStatGetTransactionGraphUseCase = gmStatGetTransactionGraphUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void loadDataWithDate(final SellerModuleRouter sellerModuleRouter, long startDate, long endDate) {
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

            private void fetchTopAdsDeposit(final GMGraphViewModel gmTopAdsAmountViewModel) {
                DataDeposit dataDeposit = sellerModuleRouter.getDataDeposit(sessionHandler.getShopID());
                if (dataDeposit.isAdUsage()) {
                    getView().bindTopAds(gmTopAdsAmountViewModel);
                } else {
                    getView().bindTopAdsCreditNotUsed(gmTopAdsAmountViewModel, dataDeposit);
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        gmStatGetTransactionGraphUseCase.unsubscribe();
    }
}