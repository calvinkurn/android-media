package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.review.domain.FlightBookingReviewSubmitUseCase;
import com.tokopedia.flight.review.domain.FlightCheckVoucherCodeUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPresenter extends BaseDaggerPresenter<FlightBookingReviewContract.View> implements FlightBookingReviewContract.Presenter {

    private final FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase;
    private final FlightBookingReviewSubmitUseCase flightBookingReviewSubmitUseCase;

    @Inject
    public FlightBookingReviewPresenter(FlightCheckVoucherCodeUseCase flightCheckVoucherCodeUseCase,
                                        FlightBookingReviewSubmitUseCase flightBookingReviewSubmitUseCase) {
        this.flightCheckVoucherCodeUseCase = flightCheckVoucherCodeUseCase;
        this.flightBookingReviewSubmitUseCase = flightBookingReviewSubmitUseCase;
    }

    @Override
    public void checkVoucherCode(String voucherCode) {
        flightCheckVoucherCodeUseCase.execute(RequestParams.create(), getSubscriberCheckVoucherCode());
    }

    @Override
    public void submitData() {
        flightBookingReviewSubmitUseCase.execute(RequestParams.create(), getSubscriberSubmitData());
    }

    private Subscriber<Boolean> getSubscriberCheckVoucherCode() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorCheckVoucherCode(e);
                }
            }

            @Override
            public void onNext(Boolean isSuccess) {
                if(isSuccess) {
                    getView().onSuccessCheckVoucherCode();
                }else{
                    getView().onErrorCheckVoucherCode(new RuntimeException());
                }
            }
        };
    }

    public Subscriber<Boolean> getSubscriberSubmitData() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorSubmitData(e);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessSubmitData();
            }
        };
    }
}
