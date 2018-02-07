package com.tokopedia.tokocash.qrpayment.presentation.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.tokocash.qrpayment.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.PostQrPaymentUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.contract.QrPaymentContract;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;
import com.tokopedia.usecase.RequestParams;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public class QrPaymentPresenter extends BaseDaggerPresenter<QrPaymentContract.View>
        implements QrPaymentContract.Presenter {

    private PostQrPaymentUseCase postQrPaymentUseCase;
    private GetBalanceTokoCashUseCase getBalanceTokoCashUseCase;

    @Inject
    public QrPaymentPresenter(PostQrPaymentUseCase postQrPaymentUseCase,
                              GetBalanceTokoCashUseCase getBalanceTokoCashUseCase) {
        this.postQrPaymentUseCase = postQrPaymentUseCase;
        this.getBalanceTokoCashUseCase = getBalanceTokoCashUseCase;
    }

    @Override
    public void postQrPayment() {
        postQrPaymentUseCase.execute(getView().getRequestParams(), new Subscriber<QrPaymentTokoCash>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().directToFailedPayment();
            }

            @Override
            public void onNext(QrPaymentTokoCash qrPaymentTokoCash) {
                getView().directToSuccessPayment(qrPaymentTokoCash);
            }
        });
    }

    @Override
    public void getBalanceTokoCash() {
        getBalanceTokoCashUseCase.execute(RequestParams.EMPTY, new Subscriber<BalanceTokoCash>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    getView().showErrorBalanceTokoCash(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                } else if (e instanceof SocketTimeoutException) {
                    getView().showErrorBalanceTokoCash(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof ResponseDataNullException) {
                    getView().showErrorBalanceTokoCash(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    getView().showErrorBalanceTokoCash(e.getMessage());
                } else {
                    getView().showErrorBalanceTokoCash(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(BalanceTokoCash balanceTokoCash) {
                if (balanceTokoCash != null) {
                    getView().renderBalanceTokoCash(balanceTokoCash);
                }
            }
        });
    }

    @Override
    public void onDestroyPresenter() {
        if (getBalanceTokoCashUseCase != null) getBalanceTokoCashUseCase.unsubscribe();
        if (postQrPaymentUseCase != null) postQrPaymentUseCase.unsubscribe();
    }
}