package com.tokopedia.session.register.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.register.view.viewlistener.CreatePassword;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordModel;

import javax.inject.Inject;

/**
 * @author by nisie on 10/13/17.
 */

public class CreatePasswordPresenter extends BaseDaggerPresenter<CreatePassword.View>
        implements CreatePassword.Presenter {

    private CreatePassword.View viewListener;


    @Inject
    public CreatePasswordPresenter(){

    }

    @Override
    public void attachView(CreatePassword.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void createPassword(CreatePasswordModel model) {
        if(isValid(model)){

        }
    }

    private boolean isValid(CreatePasswordModel model) {
        return false;
    }
}
