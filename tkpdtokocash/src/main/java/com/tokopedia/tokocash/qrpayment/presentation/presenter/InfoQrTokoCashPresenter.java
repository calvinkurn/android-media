package com.tokopedia.tokocash.qrpayment.presentation.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.tokocash.historytokocash.presentation.ServerErrorHandlerUtil;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.contract.InfoQrTokoCashContract;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class InfoQrTokoCashPresenter extends BaseDaggerPresenter<InfoQrTokoCashContract.View>
        implements InfoQrTokoCashContract.Presenter {

    private GetInfoQrTokoCashUseCase getInfoQrTokoCashUseCase;

    @Inject
    public InfoQrTokoCashPresenter(GetInfoQrTokoCashUseCase getInfoQrTokoCashUseCase) {
        this.getInfoQrTokoCashUseCase = getInfoQrTokoCashUseCase;
    }

    @Override
    public void getInfoQeTokoCash() {
        getView().showProgressDialog();
        getInfoQrTokoCashUseCase.execute(getView().getInfoTokoCashParam(), new Subscriber<InfoQrTokoCash>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                } else if (e instanceof SocketTimeoutException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof ResponseErrorException) {
                    getView().showErrorGetInfo();
                } else if (e instanceof ResponseDataNullException) {
                    getView().showErrorNetwork(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    getView().showErrorNetwork(e.getMessage());
                } else if (e instanceof ServerErrorException) {
                    ServerErrorHandlerUtil.handleError(e);
                } else {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(InfoQrTokoCash infoQrTokoCash) {
                getView().hideProgressDialog();
                if (infoQrTokoCash != null) {
                    getView().directPageToPayment(infoQrTokoCash);
                } else {
                    getView().showErrorGetInfo();
                }
            }
        });
    }

    @Override
    public void onDestroyPresenter() {
        if (getInfoQrTokoCashUseCase != null) getInfoQrTokoCashUseCase.unsubscribe();
    }
}