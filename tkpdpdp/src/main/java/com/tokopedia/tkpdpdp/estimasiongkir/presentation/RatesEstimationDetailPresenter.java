package com.tokopedia.tkpdpdp.estimasiongkir.presentation;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdpdp.estimasiongkir.GetRateEstimationUseCase;
import com.tokopedia.tkpdpdp.estimasiongkir.listener.RatesEstimationDetailView;

import javax.inject.Inject;

public class RatesEstimationDetailPresenter extends BaseDaggerPresenter<RatesEstimationDetailView> {

    private GetRateEstimationUseCase useCase;

    @Inject
    public RatesEstimationDetailPresenter(GetRateEstimationUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void detachView() {
        useCase.unsubscribe();
        super.detachView();
    }
}
