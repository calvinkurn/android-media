package com.tokopedia.session.changephonenumber.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.changephonenumber.domain.interactor.SendEmailUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.ValidateEmailCodeUseCase;
import com.tokopedia.session.changephonenumber.view.listener
        .ChangePhoneNumberEmailVerificationFragmentListener;
import com.tokopedia.session.changephonenumber.view.subscriber.SendEmailSubscriber;
import com.tokopedia.session.changephonenumber.view.subscriber.ValidateEmailCodeSubscriber;

import javax.inject.Inject;

import static com.tokopedia.session.changephonenumber.domain.interactor.ValidateEmailCodeUseCase
        .getValidateEmailCodeParam;

/**
 * Created by milhamj on 03/01/18.
 */

public class ChangePhoneNumberEmailVerificationPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberEmailVerificationFragmentListener.View>
        implements ChangePhoneNumberEmailVerificationFragmentListener.Presenter {

    private final SendEmailUseCase sendEmailUseCase;
    private final ValidateEmailCodeUseCase validateEmailCodeUseCase;
    private ChangePhoneNumberEmailVerificationFragmentListener.View view;

    @Inject
    public ChangePhoneNumberEmailVerificationPresenter(SendEmailUseCase sendEmailUseCase,
                                                       ValidateEmailCodeUseCase
                                                               validateEmailCodeUseCase) {
        this.sendEmailUseCase = sendEmailUseCase;
        this.validateEmailCodeUseCase = validateEmailCodeUseCase;
    }

    @Override
    public void attachView(ChangePhoneNumberEmailVerificationFragmentListener.View view) {
        this.view = view;
        super.attachView(view);
    }

    @Override
    public void initView() {
        sendEmail();
    }

    @Override
    public void sendEmail() {
        view.dropKeyboard();
        view.showLoading();
        sendEmailUseCase.execute(
                SendEmailUseCase.getSendEmailParam(),
                new SendEmailSubscriber(view)
        );
    }

    @Override
    public void validateOtp(String otpCode) {
        view.dropKeyboard();
        view.showLoading();
        validateEmailCodeUseCase.execute(
                ValidateEmailCodeUseCase.getValidateEmailCodeParam(otpCode),
                new ValidateEmailCodeSubscriber(view)
        );
    }
}
