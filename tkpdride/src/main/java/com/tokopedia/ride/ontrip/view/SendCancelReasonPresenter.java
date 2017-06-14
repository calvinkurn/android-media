package com.tokopedia.ride.ontrip.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.ontrip.domain.GetCancelReasonsUseCase;

import java.util.List;

import rx.Subscriber;

/**
 * Created by alvarisi on 6/14/17.
 */

public class SendCancelReasonPresenter extends BaseDaggerPresenter<SendCancelReasonContract.View> implements SendCancelReasonContract.Presenter {
    private GetCancelReasonsUseCase getCancelReasonsUseCase;
    public SendCancelReasonPresenter(GetCancelReasonsUseCase getCancelReasonsUseCase) {
        this.getCancelReasonsUseCase = getCancelReasonsUseCase;
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
                if (isViewAttached()){
                    getView().hideLoading();
                }
            }

            @Override
            public void onNext(List<String> reasons) {
                if (isViewAttached()){
                    getView().showMainLayout();
                    getView().hideLoading();
                    getView().renderReasons(reasons);
                }
            }
        });
    }

    @Override
    public void submitReasons() {

    }


}
