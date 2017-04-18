package com.tokopedia.session.activation.view.presenter;

import com.tokopedia.session.activation.data.ChangeEmailPass;

/**
 * Created by nisie on 4/18/17.
 */

public interface ChangeEmailPresenter {

    ChangeEmailPass getPass();

    void changeEmail();

    void unsubscribeObservable();
}
