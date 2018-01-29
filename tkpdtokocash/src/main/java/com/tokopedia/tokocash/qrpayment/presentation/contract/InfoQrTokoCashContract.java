package com.tokopedia.tokocash.qrpayment.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.usecase.RequestParams;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public interface InfoQrTokoCashContract {

    interface View extends CustomerView {

        RequestParams getInfoTokoCashParam();

        void showErrorGetInfo(String message);

        void showErrorNetwork(Throwable e);

        void directPageToPayment(InfoQrTokoCash infoQrTokoCash);

        void showProgressDialog();

        void hideProgressDialog();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getInfoQeTokoCash();

        void onDestroyPresenter();
    }
}
