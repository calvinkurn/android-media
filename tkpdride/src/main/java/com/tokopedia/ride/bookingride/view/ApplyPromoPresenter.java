package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.ApplyPromoUseCase;
import com.tokopedia.ride.common.ride.domain.model.ApplyPromo;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/25/17.
 */

public class ApplyPromoPresenter extends BaseDaggerPresenter<ApplyPromoContract.View>
        implements ApplyPromoContract.Presenter {
    private ApplyPromoUseCase applyPromoUseCase;

    public ApplyPromoPresenter(ApplyPromoUseCase applyPromoUseCase) {
        this.applyPromoUseCase = applyPromoUseCase;
    }

    @Override
    public void actionApplyPromo() {
        getView().showApplyPromoLoading();
        getView().hideApplyPromoLayout();
        this.applyPromoUseCase.execute(getView().getApplyPromoParams(), new Subscriber<ApplyPromo>() {
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
            public void onNext(ApplyPromo applyPromo) {
                if (isViewAttached()) {
                    getView().showApplyPromoLayout();
                    // TODO : change with response
                    if (true) {
                        getView().hideApplyPromoLoading();
                        getView().onSuccessApplyPromo(applyPromo);
                    } else {
                        getView().hideApplyPromoLoading();
                        getView().onFailedApplyPromo(applyPromo);
                    }
                }
            }
        });
    }
}
