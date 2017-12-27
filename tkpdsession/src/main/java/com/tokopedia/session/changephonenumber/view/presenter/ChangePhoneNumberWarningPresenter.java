package com.tokopedia.session.changephonenumber.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;

/**
 * Created by milhamj on 18/12/17.
 */

public class ChangePhoneNumberWarningPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberWarningFragmentListener.View>
        implements ChangePhoneNumberWarningFragmentListener.Presenter {

    ChangePhoneNumberWarningFragmentListener.View view;

    @Override
    public void attachView(ChangePhoneNumberWarningFragmentListener.View view) {
        this.view = view;
        super.attachView(view);
    }

    @Override
    public void initView() {

    }
}
