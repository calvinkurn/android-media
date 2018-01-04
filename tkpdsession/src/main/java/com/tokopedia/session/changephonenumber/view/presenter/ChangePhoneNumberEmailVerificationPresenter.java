package com.tokopedia.session.changephonenumber.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.changephonenumber.domain.interactor.SendEmailUseCase;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailFragmentListener;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailVerificationFragmentListener;
import com.tokopedia.session.changephonenumber.view.subscriber.SendEmailSubscriber;

import javax.inject.Inject;

/**
 * Created by milhamj on 03/01/18.
 */

public class ChangePhoneNumberEmailVerificationPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberEmailVerificationFragmentListener.View>
        implements ChangePhoneNumberEmailVerificationFragmentListener.Presenter {

    private final SendEmailUseCase sendEmailUseCase;
    private ChangePhoneNumberEmailVerificationFragmentListener.View view;

    @Inject
    public ChangePhoneNumberEmailVerificationPresenter(SendEmailUseCase sendEmailUseCase) {
        this.sendEmailUseCase = sendEmailUseCase;
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
        sendEmailUseCase.execute(SendEmailUseCase.getSendEmailParam(),
                new SendEmailSubscriber(view));
    }
}
