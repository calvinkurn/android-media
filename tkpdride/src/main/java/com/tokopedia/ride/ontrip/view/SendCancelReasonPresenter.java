package com.tokopedia.ride.ontrip.view;

import android.text.TextUtils;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.GetCancelReasonsUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 6/14/17.
 */

public class SendCancelReasonPresenter extends BaseDaggerPresenter<SendCancelReasonContract.View> implements SendCancelReasonContract.Presenter {
    private GetCancelReasonsUseCase getCancelReasonsUseCase;
    private CancelRideRequestUseCase cancelRideRequestUseCase;

    @Inject
    public SendCancelReasonPresenter(GetCancelReasonsUseCase getCancelReasonsUseCase,
                                     CancelRideRequestUseCase cancelRideRequestUseCase) {
        this.getCancelReasonsUseCase = getCancelReasonsUseCase;
        this.cancelRideRequestUseCase = cancelRideRequestUseCase;
    }

    @Override
    public void initialize() {
        actionGetReasons();
    }

    @Override
    public void actionGetReasons() {
        getView().showLoading();
        getView().hideMainLayout();
        getCancelReasonsUseCase.execute(getView().getReasonsParam(), new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideLoading();
                    getView().showErrorGetReasons();
                }
            }

            @Override
            public void onNext(List<String> reasons) {
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideLoading();
                    getView().renderReasons(reasons);
                }
            }
        });
    }

    @Override
    public void submitReasons() {
        if (TextUtils.isEmpty(getView().getSelectedReason())) {
            getView().showReasonEmptyError();
            return;
        }
        getView().showLoading();
        getView().hideMainLayout();
        cancelRideRequestUseCase.execute(getView().getCancelParams(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().showMainLayout();
                    getView().showErrorCancelRequest();
                }
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    getView().onSuccessCancelRequest();
                }
            }
        });
    }
}
