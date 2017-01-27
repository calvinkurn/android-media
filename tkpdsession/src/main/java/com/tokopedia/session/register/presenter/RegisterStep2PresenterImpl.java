package com.tokopedia.session.register.presenter;

import com.tokopedia.session.register.fragment.RegisterStep2Fragment;
import com.tokopedia.session.register.viewlistener.RegisterStep2ViewListener;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterStep2PresenterImpl implements RegisterStep2Presenter {

    private final RegisterStep2ViewListener viewListener;

    public RegisterStep2PresenterImpl(RegisterStep2ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void finishRegister() {
        viewListener.showLoadingProgress();
        if(isValidForm()){

        }

    }

    private boolean isValidForm() {

        return false;
    }
}
