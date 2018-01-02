package com.tokopedia.otp.securityquestion.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.domain.interactor.ValidateOtpLoginUseCase;
import com.tokopedia.otp.domain.interactor.ValidateOtpUseCase;
import com.tokopedia.otp.securityquestion.domain.interactor.GetSecurityQuestionFormUseCase;
import com.tokopedia.otp.securityquestion.view.listener.SecurityQuestion;
import com.tokopedia.otp.securityquestion.view.subscriber.GetSecurityQuestionFormSubscriber;
import com.tokopedia.otp.securityquestion.view.subscriber.RequestOTPSecurityQuestionSubscriber;
import com.tokopedia.otp.securityquestion.view.subscriber.ValidateOtpLoginSubscriber;
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
    private final RequestOtpUseCase requestOtpUseCase;
    private final ValidateOtpLoginUseCase validateOtpLoginUseCase;
    private final SessionHandler sessionHandler;

    private SecurityQuestion.View viewListener;

    @Inject
    public SecurityQuestionPresenter(SessionHandler sessionHandler,
                                     GetSecurityQuestionFormUseCase
                                             getSecurityQuestionFormUseCase,
                                     RequestOtpUseCase requestOtpUseCase,
                                     ValidateOtpLoginUseCase validateOtpLoginUseCase) {
        this.getSecurityQuestionFormUseCase = getSecurityQuestionFormUseCase;
        this.requestOtpUseCase = requestOtpUseCase;
        this.validateOtpLoginUseCase = validateOtpLoginUseCase;
        this.sessionHandler = sessionHandler;
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
        requestOtpUseCase.unsubscribe();
        validateOtpLoginUseCase.unsubscribe();
    }

    @Override
    public void validateOTP(String otp) {
        viewListener.resetError();

        if (isValid(otp)) {
            viewListener.showLoadingProgress();
            validateOtpLoginUseCase.execute(ValidateOtpLoginUseCase.getParam(
                    ValidateOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                    otp,
                    sessionHandler.getTempLoginSession(MainApplication.getAppContext())),
                    new ValidateOtpLoginSubscriber(viewListener));
        } else {
            viewListener.showInvalidOtp();
        }
    }

    @Override
    public void processTrueCaller(Intent data) {
        if (data != null && data.getStringExtra(PHONE) != null) {
            viewListener.onSuccessGetTrueCallerData();
        } else if (data != null && data.getStringExtra(ERROR) != null) {
            viewListener.onErrorVerifyTrueCaller(data.getStringExtra(ERROR));
        }
    }

    @Override
    public void getQuestionForm(SecurityDomain securityDomain) {
        viewListener.showLoadingFull();
        getSecurityQuestionFormUseCase.execute(getSecurityQuestionFormUseCase.getParam(
                securityDomain.getUserCheckSecurity1(),
                securityDomain.getUserCheckSecurity2()
        ), new GetSecurityQuestionFormSubscriber(viewListener));
    }

    @Override
    public void checkTrueCaller(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(URI_TRUECALLER, PackageManager.GET_ACTIVITIES);
            viewListener.showTrueCaller();
        } catch (PackageManager.NameNotFoundException e) {
            viewListener.removeTrueCaller();

        }
    }

    @Override
    public void requestOTPWithSMS() {
        viewListener.showLoadingProgress();
        viewListener.disableOtpButton();
        requestOtpUseCase.execute(
                RequestOtpUseCase.getParamBeforeLogin(
                        RequestOtpUseCase.MODE_SMS,
                        "",
                        RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                        sessionHandler.getTempLoginSession(MainApplication.getAppContext())),
                new RequestOTPSecurityQuestionSubscriber(viewListener));
    }

    @Override
    public void requestOTPWithPhoneCall() {
        viewListener.showLoadingProgress();
        viewListener.disableOtpButton();
        requestOtpUseCase.execute(
                RequestOtpUseCase.getParamBeforeLogin(
                        RequestOtpUseCase.MODE_CALL,
                        "",
                        RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                        sessionHandler.getTempLoginSession(MainApplication.getAppContext())),
                new RequestOTPSecurityQuestionSubscriber(viewListener));
    }

    @Override
    public void requestOTPWithEmail(String email) {
//        viewListener.showLoadingProgress();
//        viewListener.disableOtpButton();
//        requestOtpUseCase.execute(
//                RequestOtpUseCase.getParamEmailBeforeLogin(
//                        RequestOtpUseCase.MODE_EMAIL,
//                        email,
//                        RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
//                        sessionHandler.getTempLoginSession(MainApplication.getAppContext())),
//                new RequestOTPSecurityQuestionSubscriber(viewListener));
    }

    private boolean isValid(String otp) {
        return otp.trim().length() != 0 && otp.trim().length() == 6;
    }
}
