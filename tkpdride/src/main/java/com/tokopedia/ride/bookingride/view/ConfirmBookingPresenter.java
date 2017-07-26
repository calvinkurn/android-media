package com.tokopedia.ride.bookingride.view;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.InterruptConfirmationHttpException;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/22/17.
 */

public class ConfirmBookingPresenter extends BaseDaggerPresenter<ConfirmBookingContract.View>
        implements ConfirmBookingContract.Presenter {
    private GetFareEstimateUseCase getFareEstimateUseCase;

    @Inject
    public ConfirmBookingPresenter(GetFareEstimateUseCase getFareEstimateUseCase) {
        this.getFareEstimateUseCase = getFareEstimateUseCase;
    }

    @Override
    public void initialize() {
        actionGetFareAndEstimate(getView().getParam());
        getView().renderInitialView();
    }

    @Override
    public void actionGetFareAndEstimate(RequestParams showProgress) {
        getView().showProgress();
        getView().hidePromoLayout();
        getView().disableConfirmBtn();

        RequestParams requestParams = getView().getParam();
        getFareEstimateUseCase.execute(requestParams, new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideProgress();

                    if (e instanceof InterruptConfirmationHttpException) {
                        if (((InterruptConfirmationHttpException) e).getType().equalsIgnoreCase(InterruptConfirmationHttpException.TOS_CONFIRMATION_INTERRUPT)) {
                            getView().openInterruptConfirmationWebView(((InterruptConfirmationHttpException) e).getTosUrl());
                            getView().showErrorTosConfirmation(((InterruptConfirmationHttpException) e).getTosUrl());
                        } else if (((InterruptConfirmationHttpException) e).getType().equalsIgnoreCase(InterruptConfirmationHttpException.TOS_TOKOPEDIA_INTERRUPT)) {
                            getView().showErrorTosConfirmationDialog(
                                    e.getMessage(),
                                    ((InterruptConfirmationHttpException) e).getTosUrl(),
                                    ((InterruptConfirmationHttpException) e).getKey(),
                                    ((InterruptConfirmationHttpException) e).getId()
                            );
                            getView().openInterruptConfirmationDialog(
                                    ((InterruptConfirmationHttpException) e).getTosUrl(),
                                    ((InterruptConfirmationHttpException) e).getKey(),
                                    ((InterruptConfirmationHttpException) e).getId()
                            );
                        } else {
                            String message = e.getMessage();
                            getView().showToastMessage(message);
                            getView().goToProductList();
                        }
                    } else {
                        if (e instanceof UnknownHostException || e instanceof ConnectException) {
                            getView().showToastMessage(getView().getActivity().getString(R.string.error_no_connection));
                        } else if (e instanceof SocketTimeoutException) {
                            getView().showToastMessage(getView().getActivity().getString(R.string.error_timeout));
                        } else if (e instanceof UnProcessableHttpException) {
                            String message = TextUtils.isEmpty(e.getMessage()) ?
                                    getView().getActivity().getResources().getString(R.string.error_internet_not_connected) :
                                    e.getMessage();
                            getView().showToastMessage(message);
                        } else {
                            getView().showToastMessage(getView().getActivity().getString(R.string.error_default));
                        }
                        getView().goToProductList();
                    }
                }
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                if (isViewAttached()) {
                    getView().hideProgress();
                    getView().enableConfirmButton();
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
