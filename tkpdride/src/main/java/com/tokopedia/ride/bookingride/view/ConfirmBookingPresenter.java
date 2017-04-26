package com.tokopedia.ride.bookingride.view;

import android.content.Intent;
import android.net.Uri;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.drawer.interactor.NetworkInteractor;
import com.tokopedia.core.drawer.interactor.NetworkInteractorImpl;
import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/22/17.
 */

public class ConfirmBookingPresenter extends BaseDaggerPresenter<ConfirmBookingContract.View>
        implements ConfirmBookingContract.Presenter {
    private GetFareEstimateUseCase getFareEstimateUseCase;
    private NetworkInteractor networkInteractor;

    public ConfirmBookingPresenter(GetFareEstimateUseCase getFareEstimateUseCase) {
        this.getFareEstimateUseCase = getFareEstimateUseCase;
        this.networkInteractor = new NetworkInteractorImpl();
    }

    @Override
    public void initialize() {
        actionGetFareAndEstimate();
    }

    @Override
    public void actionGetFareAndEstimate() {
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
                getView().renderFareEstimate(fareEstimate.getFare().getFareId(), fareEstimate.getFare().getDisplay(), fareEstimate.getFare().getValue());
            }
        });
    }

    @Override
    public void detachView() {
        getFareEstimateUseCase.unsubscribe();
        networkInteractor.unsubscribe();
        super.detachView();
    }
}
