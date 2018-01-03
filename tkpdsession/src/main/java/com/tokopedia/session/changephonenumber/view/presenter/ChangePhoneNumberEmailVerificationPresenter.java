package com.tokopedia.session.changephonenumber.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailFragmentListener;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailVerificationFragmentListener;

/**
 * Created by milhamj on 03/01/18.
 */

public class ChangePhoneNumberEmailVerificationPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberEmailVerificationFragmentListener.View>
        implements ChangePhoneNumberEmailVerificationFragmentListener.Presenter {

    private ChangePhoneNumberEmailVerificationFragmentListener.View view;

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

    }
}
