package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
    public void initialize() {
        actionGetFareAndEstimate(false);
        getView().renderInitialView();
    }

    @Override
    public void actionGetFareAndEstimate(boolean showProgress) {
        getView().showProgress();
        getView().hidePromoLayout();
        if (showProgress) {
            getView().hideConfirmLayout();
        }

        RequestParams requestParams = getView().getParam();
        getFareEstimateUseCase.execute(requestParams, new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    if (e instanceof UnknownHostException) {
                        getView().showToastMessage(getView().getActivity().getString(R.string.error_no_connection));
                    } else if (e instanceof SocketTimeoutException) {
                        getView().showToastMessage(getView().getActivity().getString(R.string.error_timeout));
                    } else {
                        getView().showToastMessage(getView().getActivity().getString(R.string.error_default));
                    }
                    getView().goToProductList();
                }
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                if (isViewAttached()) {
                    getView().hideProgress();
                    getView().showConfirmLayout();
                    getView().showPromoLayout();

                    float surgeMultiplier = 0;
                    String display = "";
                    String surgeConfirmationHref = null;
                    if (fareEstimate.getEstimate() != null) {
                        surgeMultiplier = fareEstimate.getEstimate().getSurgeMultiplier();
                        display = fareEstimate.getEstimate().getDisplay();
                        surgeConfirmationHref = fareEstimate.getEstimate().getSurgeConfirmationHref();
                    } else {
                        display = fareEstimate.getFare().getDisplay();
                    }

                    getView().renderFareEstimate(fareEstimate.getFare().getFareId(), display, fareEstimate.getFare().getValue(), surgeMultiplier, surgeConfirmationHref, fareEstimate.getCode(), fareEstimate.getMessageSuccess());
                    getView().setViewListener();
                }

            }
        });
    }

    @Override
    public void detachView() {
        getFareEstimateUseCase.unsubscribe();
        super.detachView();
    }
}
