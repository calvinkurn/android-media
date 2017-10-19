package com.tokopedia.otp.securityquestion.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.otp.securityquestion.SecurityQuestion;
import com.tokopedia.otp.securityquestion.domain.interactor.GetSecurityQuestionFormUseCase;
import com.tokopedia.otp.securityquestion.view.subscriber.GetSecurityQuestionFormSubscriber;
import com.tokopedia.session.data.viewmodel.SecurityDomain;

import javax.inject.Inject;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionPresenter extends BaseDaggerPresenter<SecurityQuestion.View>
        implements SecurityQuestion.Presenter {

    private static final String PHONE = "phone";
    private static final String ERROR = "error";
    private static final String URI_TRUECALLER = "com.truecaller";

    private final GetSecurityQuestionFormUseCase getSecurityQuestionFormUseCase;

    private SecurityQuestion.View viewListener;

    @Inject
    public SecurityQuestionPresenter(GetSecurityQuestionFormUseCase getSecurityQuestionFormUseCase) {
        this.getSecurityQuestionFormUseCase = getSecurityQuestionFormUseCase;
    }

    @Override
    public void attachView(SecurityQuestion.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getSecurityQuestionFormUseCase.unsubscribe();
    }

    @Override
    public void validateOTP(String otp) {
        viewListener.resetError();

        if (isValid(otp)) {

        } else {
            viewListener.showInvalidOtp();
        }
    }

    @Override
    public void requestOTPWithPhoneCall() {

    }

    @Override
    public void processTrueCaller(Intent data) {
        if (data != null && data.getStringExtra(PHONE) != null) {
            UnifyTracking.eventClickTruecallerConfirm();
            validateOTPWithTrueCaller(data.getStringExtra(PHONE));
        } else if (data != null && data.getStringExtra(ERROR) != null) {
            viewListener.onErrorVerifyTrueCaller(data.getStringExtra(ERROR));
        }
    }

    @Override
    public void getQuestionForm(SecurityDomain securityDomain) {
        getSecurityQuestionFormUseCase.execute(getSecurityQuestionFormUseCase.getParam(
                securityDomain.getUserCheckSecurity1(),
                securityDomain.getUserCheckSecurity2()
        ), new GetSecurityQuestionFormSubscriber(viewListener));
    }

    @Override
    public void checkTrueCaller(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(URI_TRUECALLER, PackageManager.GET_ACTIVITIES);
            viewListener.showTrueCaller();
        } catch (PackageManager.NameNotFoundException e) {
            viewListener.removeTrueCaller();

        }
    }

    @Override
    public void requestOTPWithSMS() {

    }

    @Override
    public void requestOTPWithEmail() {

    }

    private void validateOTPWithTrueCaller(String phoneNumber) {

    }

    private boolean isValid(String otp) {
        return otp.trim().length() != 0 && otp.trim().length() == 6;
    }
}
