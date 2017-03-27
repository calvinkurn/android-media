package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/22/17.
 */

public class ConfirmBookingPresenter extends BaseDaggerPresenter<ConfirmBookingContract.View>
        implements ConfirmBookingContract.Presenter {
    private GetFareEstimateUseCase getFareEstimateUseCase;
    public ConfirmBookingPresenter(GetFareEstimateUseCase getFareEstimateUseCase) {
        this.getFareEstimateUseCase = getFareEstimateUseCase;
    }

    @Override
    public void actionChangeSeatCount() {
        RequestParams requestParams = getView().getParam();
        getFareEstimateUseCase.execute(requestParams, new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().showErrorChangeSeat(e.getMessage());
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                getView().renderFareEstimate(fareEstimate.getFare().getFareId(), fareEstimate.getFare().getDisplay());
            }
        });
    }
}
