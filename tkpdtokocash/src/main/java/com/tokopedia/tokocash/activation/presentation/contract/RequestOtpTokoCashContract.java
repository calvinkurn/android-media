package com.tokopedia.tokocash.activation.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public interface RequestOtpTokoCashContract {

    interface View extends CustomerView {
        void onSuccessRequestOtpWallet();

        void onSuccessLinkWalletToTokoCash();

        void onErrorOTPWallet(Throwable e);

        void onErrorNetwork(Throwable e);

        void showProgressDialog();
    }

    interface Presenter extends CustomerPresenter<View> {
        void requestOTPWallet();

        void linkWalletToTokoCash(String otp);

        void onDestroyView();

        String getUserPhoneNumber();

        void setMsisdnUserVerified(boolean verified);
    }
}
