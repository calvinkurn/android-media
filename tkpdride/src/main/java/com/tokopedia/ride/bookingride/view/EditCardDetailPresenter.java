package com.tokopedia.ride.bookingride.view;

import android.os.Bundle;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.RequestApiUseCase;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Set;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Vishal on 2nd Nov, 2017.
 */

public class EditCardDetailPresenter extends BaseDaggerPresenter<EditCardDetailContract.View>
        implements EditCardDetailContract.Presenter {

    private RequestApiUseCase deleteCreditCardUseCase;

    @Inject
    public EditCardDetailPresenter(RequestApiUseCase deleteCreditCardUseCase) {
        this.deleteCreditCardUseCase = deleteCreditCardUseCase;
    }

    @Override
    public void deleteCard(PaymentMethodViewModel paymentMethodViewModel) {
        getView().showProgress();
        deleteCreditCardUseCase.setUrl(paymentMethodViewModel.getDeleteUrl());
        RequestParams params = RequestParams.create();

        //create params from bundle
        Bundle bundle = paymentMethodViewModel.getDeleteBody();
        Set<String> set = bundle.keySet();
        for (String key : set) {
            params.putString(key, bundle.getString(key));
        }

        deleteCreditCardUseCase.execute(params, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                if (isViewAttached()) {
                    getView().hideProgress();
                    String error;
                    if (e instanceof UnknownHostException) {
                        error = getView().getActivity().getString(R.string.error_no_connection);
                    } else if (e instanceof SocketTimeoutException) {
                        error = getView().getActivity().getString(R.string.error_timeout);
                    } else {
                        error = getView().getActivity().getString(R.string.error_default);
                    }
                    getView().showErrorMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached() && getView().getActivity() != null) {
                    getView().hideProgress();
                    getView().closeActivity();
                }
            }
        });

        //ScroogePGUtil.openScroogePage(getView().getActivity(), paymentMethodViewModel.getDeleteUrl(), true, paymentMethodViewModel.getDeleteBody());
    }
}
