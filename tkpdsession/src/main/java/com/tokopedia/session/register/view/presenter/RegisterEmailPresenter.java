package com.tokopedia.session.register.view.presenter;

import com.tokopedia.session.register.view.viewmodel.RegisterEmailViewModel;

/**
 * Created by nisie on 1/27/17.
 */
public interface RegisterEmailPresenter {
    void onRegisterClicked();

    void unsubscribeObservable();

    void startAction(RegisterEmailViewModel viewModel);

    boolean isCanRegister();

}
