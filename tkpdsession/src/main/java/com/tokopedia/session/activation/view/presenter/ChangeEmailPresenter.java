package com.tokopedia.session.activation.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;

/**
 * Created by nisie on 4/18/17.
 */

public interface ChangeEmailPresenter {

    void changeEmail(RequestParams changeEmailParam);

    void unsubscribeObservable();

    RequestParams getChangeEmailParam(String oldEmail, String newEmail, String password);
}
