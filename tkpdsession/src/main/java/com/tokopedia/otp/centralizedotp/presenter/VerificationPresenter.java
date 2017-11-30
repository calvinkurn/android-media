package com.tokopedia.otp.centralizedotp.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.otp.centralizedotp.VerificationActivity;
import com.tokopedia.otp.centralizedotp.viewlistener.Verification;
import com.tokopedia.otp.centralizedotp.viewmodel.VerificationViewModel;

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
    public void requestOTP(VerificationViewModel viewModel) {
        int type = viewModel.getType();
        switch (type) {
            case VerificationActivity.TYPE_SMS:
                getView().onSuccessGetOTP();
                break;
            case VerificationActivity.TYPE_PHONE_CALL:
                getView().onSuccessGetOTP();
                break;
        }
    }
}
