package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/25/17.
 */

public class ApplyPromoPresenter extends BaseDaggerPresenter<ApplyPromoContract.View>
        implements ApplyPromoContract.Presenter {
    private GetFareEstimateUseCase getFareEstimateUseCase;

    public ApplyPromoPresenter(GetFareEstimateUseCase applyPromoUseCase) {
        this.getFareEstimateUseCase = applyPromoUseCase;
    }

    @Override
    public void actionApplyPromo() {
        getView().showApplyPromoLoading();
        getView().hideApplyPromoLayout();


        getFareEstimateUseCase.execute(getView().getParams(), new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showApplyPromoLayout();
                getView().hideApplyPromoLoading();
                getView().onFailedApplyPromo(e.getMessage());
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                if (isViewAttached()) {
                    getView().showApplyPromoLayout();
                    // TODO : change with response
                    if (true) {
                        getView().hideApplyPromoLoading();
                        //getView().onSuccessApplyPromo(fareEstimate);
                    } else {
                        getView().hideApplyPromoLoading();
                        //getView().onFailedApplyPromo(fareEstimate);
                    }
                }
            }
        });
    }
}
