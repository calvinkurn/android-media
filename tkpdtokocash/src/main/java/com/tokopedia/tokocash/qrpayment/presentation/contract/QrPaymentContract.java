package com.tokopedia.tokocash.qrpayment.presentation.contract;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;
import com.tokopedia.usecase.RequestParams;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public interface QrPaymentContract {

    interface View extends CustomerView {

        RequestParams getRequestParams();

        void directToSuccessPayment(QrPaymentTokoCash qrPaymentTokoCash);

        void directToFailedPayment();

        void renderBalanceTokoCash(BalanceTokoCash balanceTokoCash);

        void showErrorBalanceTokoCash(String message);
    }

    interface Presenter extends CustomerPresenter<View> {

        void postQrPayment();

        void getBalanceTokoCash();

        void onDestroyPresenter();

    }
}
