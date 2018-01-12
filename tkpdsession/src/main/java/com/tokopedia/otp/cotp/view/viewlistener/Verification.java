package com.tokopedia.otp.cotp.view.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;

/**
 * @author by nisie on 11/30/17.
 */

public interface Verification {
    interface View extends CustomerView {
        void onSuccessGetOTP();

        void onSuccessVerifyOTP();

        void onErrorGetOTP(String errorMessage);

        void onErrorVerifyOtp(String errorMessage);

        void showLoadingProgress();

        void dismissLoadingProgress();

        boolean isCountdownFinished();

        void dropKeyboard();

    }

    interface Presenter extends CustomerPresenter<View> {

        void requestOTP(VerificationViewModel viewModel, VerificationPassModel passModel);

        void verifyOtp(VerificationPassModel phoneNumber, String otpCode);
    }
}
