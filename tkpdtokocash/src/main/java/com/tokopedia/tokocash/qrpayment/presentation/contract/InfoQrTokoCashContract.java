package com.tokopedia.tokocash.qrpayment.presentation.contract;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.usecase.RequestParams;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public interface InfoQrTokoCashContract {

    interface View extends CustomerView {

        RequestParams getInfoTokoCashParam();

        void showErrorGetInfo();

        void showErrorNetwork(String message);

        void directPageToPayment(InfoQrTokoCash infoQrTokoCash);

        void showProgressDialog();

        void hideProgressDialog();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getInfoQeTokoCash();

        void onDestroyPresenter();
    }
}
