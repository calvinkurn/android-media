package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetPayPendingDataUseCase;
import com.tokopedia.ride.common.ride.domain.model.PayPending;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Vishal on 14th Nov, 2017.
 */

public class PayPendingFarePresenter extends BaseDaggerPresenter<PayPendingFareContract.View> implements PayPendingFareContract.Presenter {
    private GetPayPendingDataUseCase getPayPendingDataUseCase;

    @Inject
    public PayPendingFarePresenter(GetPayPendingDataUseCase getPayPendingDataUseCase) {
        this.getPayPendingDataUseCase = getPayPendingDataUseCase;
    }


    @Override
    public void onDestroy() {
        detachView();
        getPayPendingDataUseCase.unsubscribe();
    }

    @Override
    public void payPendingFare() {
        if (getView() != null) {
            getView().showProgressbar();
        }
        getPayPendingDataUseCase.execute(RequestParams.EMPTY, new Subscriber<PayPending>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) {
                    return;
                }


                String error;
                if (e instanceof UnknownHostException) {
                    error = getView().getActivity().getString(R.string.error_no_connection);
                } else if (e instanceof SocketTimeoutException) {
                    error = getView().getActivity().getString(R.string.error_timeout);
                } else {
                    error = getView().getActivity().getString(R.string.error_default);
                }

                getView().hideProgressbar();
                getView().showErrorMessage(error);
            }

            @Override
            public void onNext(PayPending payPending) {
                if (getView() != null) {
                    getView().hideProgressbar();
                    getView().opeScroogePage(payPending.getUrl(), payPending.getPostData());
                }
            }
        });
    }
}
