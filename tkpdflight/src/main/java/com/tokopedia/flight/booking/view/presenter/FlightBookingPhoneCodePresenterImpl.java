package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.domain.FlightBookingGetPhoneCodeUseCase;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingPhoneCodePresenterImpl extends BaseDaggerPresenter<FlightBookingPhoneCodeView>
        implements FlightBookingPhoneCodePresenter {

    private final FlightBookingGetPhoneCodeUseCase flightBookingGetPhoneCodeUseCase;

    @Inject
    public FlightBookingPhoneCodePresenterImpl(FlightBookingGetPhoneCodeUseCase flightBookingGetPhoneCodeUseCase) {
        this.flightBookingGetPhoneCodeUseCase = flightBookingGetPhoneCodeUseCase;
    }

    @Override
    public void getPhoneCodeList() {
        flightBookingGetPhoneCodeUseCase.execute(RequestParams.create(), getSubscriberPhoneCode());
    }

    @Override
    public void getPhoneCodeList(String text) {
        flightBookingGetPhoneCodeUseCase.execute(flightBookingGetPhoneCodeUseCase.createRequest(text), getSubscriberPhoneCode());
    }

    @Override
    public void onDestroyView() {
        detachView();
    }

    public Subscriber<List<FlightBookingPhoneCodeViewModel>> getSubscriberPhoneCode() {
        return new Subscriber<List<FlightBookingPhoneCodeViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<FlightBookingPhoneCodeViewModel> flightBookingPhoneCodeViewModels) {
                if (isViewAttached()) {
                    getView().renderList(flightBookingPhoneCodeViewModels);
                }
            }
        };
    }
}
