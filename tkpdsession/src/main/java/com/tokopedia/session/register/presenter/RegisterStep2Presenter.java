package com.tokopedia.session.register.presenter;

import com.tokopedia.session.register.model.RegisterViewModel;

/**
 * Created by nisie on 1/27/17.
 */
public interface RegisterStep2Presenter {
    void finishRegister();

    RegisterViewModel getViewModel();

    void calculateDateTime();
}
