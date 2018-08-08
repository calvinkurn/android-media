package com.tokopedia.tkpdpdp.estimasiongkir.presentation;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.tkpdpdp.estimasiongkir.GetRateEstimationUseCase;
import com.tokopedia.tkpdpdp.estimasiongkir.RatesModel;
import com.tokopedia.tkpdpdp.estimasiongkir.listener.RatesEstimationDetailView;

import javax.inject.Inject;

import rx.Subscriber;

public class RatesEstimationDetailPresenter extends BaseDaggerPresenter<RatesEstimationDetailView> {

    private GetRateEstimationUseCase useCase;
    private UserSession userSession;

    @Inject
    public RatesEstimationDetailPresenter(GetRateEstimationUseCase useCase, UserSession userSession) {
        this.useCase = useCase;
        this.userSession = userSession;
    }

    @Override
    public void detachView() {
        useCase.unsubscribe();
        super.detachView();
    }

    public void getCostEstimation(String rawQuery, String productId) {
        useCase.execute(GetRateEstimationUseCase.createRequestParams(rawQuery, productId, userSession.getUserId()),
                new Subscriber<RatesModel>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        if (isViewAttached()){
                            getView().onErrorLoadRateEstimaion(throwable);
                        }
                    }

                    @Override
                    public void onNext(RatesModel ratesModel) {
                        if (isViewAttached()) {
                            getView().onSuccesLoadRateEstimaion(ratesModel);
                        }
                    }
                });
    }
}
