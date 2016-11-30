package com.tokopedia.session.forgotpassword.presenter;

import com.tokopedia.session.forgotpassword.interactor.ForgotPasswordRetrofitInteractorImpl;
import com.tokopedia.session.forgotpassword.fragment.ForgotPasswordFragment;
import com.tokopedia.session.forgotpassword.interactor.ForgotPasswordRetrofitInteractor;
import com.tokopedia.session.forgotpassword.listener.ForgotPasswordFragmentView;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordFragmentPresenterImpl implements ForgotPasswordFragmentPresenter {

    public static final int REQUEST_FORGOT_PASSWORD_CODE = 1;
    ForgotPasswordFragmentView viewListener;
    ForgotPasswordRetrofitInteractor networkInteractor;

    public ForgotPasswordFragmentPresenterImpl(ForgotPasswordFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new ForgotPasswordRetrofitInteractorImpl();
    }


    @Override
    public void initData(String o) {

    }

    @Override
    public void onDestroyView() {

    }
}
