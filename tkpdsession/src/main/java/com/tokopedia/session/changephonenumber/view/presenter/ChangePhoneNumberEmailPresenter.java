package com.tokopedia.session.changephonenumber.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.changephonenumber.domain.interactor.SendEmailUseCase;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailFragmentListener;
import com.tokopedia.session.changephonenumber.view.subscriber.SendEmailSubscriber;

import javax.inject.Inject;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberEmailPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberEmailFragmentListener.View>
        implements ChangePhoneNumberEmailFragmentListener.Presenter {

    private final SendEmailUseCase sendEmailUseCase;
    private ChangePhoneNumberEmailFragmentListener.View view;

    @Inject
    public ChangePhoneNumberEmailPresenter(SendEmailUseCase sendEmailUseCase) {
        this.sendEmailUseCase = sendEmailUseCase;
    }

    @Override
    public void attachView(ChangePhoneNumberEmailFragmentListener.View view) {
        this.view = view;
        super.attachView(view);
    }

    @Override
    public void initView() {
        sendEmail();
    }

    @Override
    public void sendEmail() {
        view.showLoading();
        sendEmailUseCase.execute(RequestParams.create(),
                new SendEmailSubscriber(view));
    }
}
