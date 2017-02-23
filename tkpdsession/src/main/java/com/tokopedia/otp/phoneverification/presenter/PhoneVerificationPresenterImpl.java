package com.tokopedia.otp.phoneverification.presenter;

import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationFragment;
import com.tokopedia.otp.phoneverification.listener.PhoneVerificationFragmentView;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationPresenterImpl implements PhoneVerificationPresenter {

    private final PhoneVerificationFragmentView viewListener;

    public PhoneVerificationPresenterImpl(PhoneVerificationFragmentView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void verifyOtp() {

    }

    @Override
    public void requestOtp() {
        viewListener.onSuccessRequestOtp();
    }

    @Override
    public void requestOtpWithCall() {

    }
}
