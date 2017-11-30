package com.tokopedia.otp.centralizedotp.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.otp.centralizedotp.viewlistener.Verification;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationPresenter extends BaseDaggerPresenter<Verification.View> implements
        Verification.Presenter {

    @Inject
    public VerificationPresenter() {

    }

    public void attachView(Verification.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void requestOTP(String phoneNumber) {
        getView().onSuccessGetOTP();
    }
}
