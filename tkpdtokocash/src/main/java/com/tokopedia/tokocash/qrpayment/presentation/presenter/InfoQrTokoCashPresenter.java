package com.tokopedia.tokocash.qrpayment.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.network.exception.WalletException;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.contract.InfoQrTokoCashContract;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;

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
                if (e instanceof WalletException) {
                    getView().showErrorGetInfo(e.getMessage());
                } else {
                    getView().showErrorNetwork(e);
                }
            }

            @Override
            public void onNext(InfoQrTokoCash infoQrTokoCash) {
                getView().hideProgressDialog();
                if (infoQrTokoCash != null) {
                    getView().directPageToPayment(infoQrTokoCash);
                }
            }
        });
    }

    @Override
    public void onDestroyPresenter() {
        if (getInfoQrTokoCashUseCase != null) getInfoQrTokoCashUseCase.unsubscribe();
    }
}