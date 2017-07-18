package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.topads.dashboard.domain.interactor.DashboardTopadsInteractor;

import rx.Subscriber;

/**
 * @author normansyahputa on 7/18/17.
 *
 *
 */
public class GMStatisticTransactionPresenterImpl extends GMStatisticTransactionPresenter {
    private GMStatGetTransactionGraphUseCase useCase;
    private DashboardTopadsInteractor topadsUseCase;

    public GMStatisticTransactionPresenterImpl(GMStatGetTransactionGraphUseCase useCase, DashboardTopadsInteractor topadsUseCase) {
        super();
        this.useCase = useCase;
        this.topadsUseCase = topadsUseCase;
    }

    @Override
    public void loadDataWithoutDate() {
        useCase.execute(GMStatGetTransactionGraphUseCase.createRequestParam(-1, -1), new Subscriber<GMTransactionGraphMergeModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GMTransactionGraphMergeModel mergeModel) {
                revealData(mergeModel);
            }
        });
    }

    @Override
    public void loadDataWithDate(long startDate, long endDate) {
        useCase.execute(GMStatGetTransactionGraphUseCase.createRequestParam(startDate, endDate), new Subscriber<GMTransactionGraphMergeModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GMTransactionGraphMergeModel gmTransactionGraphViewModel) {
                revealData(gmTransactionGraphViewModel);
            }
        });
    }

    protected void revealData(GMTransactionGraphMergeModel mergeModel) {
        if (isViewAttached()) {
            getView().revealData(mergeModel);
        }
    }
}
