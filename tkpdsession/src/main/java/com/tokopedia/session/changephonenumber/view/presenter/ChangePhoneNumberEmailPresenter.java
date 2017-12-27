package com.tokopedia.session.changephonenumber.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailFragmentListener;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberEmailPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberEmailFragmentListener.View>
        implements ChangePhoneNumberEmailFragmentListener.Presenter {

    ChangePhoneNumberEmailFragmentListener.View view;

    @Override
    public void attachView(ChangePhoneNumberEmailFragmentListener.View view) {
        this.view = view;
        super.attachView(view);
    }

    @Override
    public void initView() {

    }
}
