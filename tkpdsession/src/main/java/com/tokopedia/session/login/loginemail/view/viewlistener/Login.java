package com.tokopedia.session.login.loginemail.view.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.session.login.loginemail.domain.model.LoginEmailDomain;
import com.tokopedia.session.register.view.viewmodel.DiscoverItemViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 12/18/17.
 */

public interface Login {

    interface View extends CustomerView {

        void resetError();

        void showLoadingLogin();

        void showErrorPassword(int resId);

        void showErrorEmail(int resId);

        void dismissLoadingLogin();

        void goToSecurityQuestion(LoginEmailDomain loginResult);

        void onSuccessLogin();

        void onErrorLogin(String errorMessage);

        void setAutoCompleteAdapter(ArrayList<String> listId);

        void showLoadingDiscover();

        void dismissLoadingDiscover();

        void onErrorDiscoverLogin(String errorMessage);

        void onSuccessDiscoverLogin(ArrayList<DiscoverItemViewModel> providers);
    }

    interface Presenter extends CustomerPresenter<View> {

        void login(String email, String password);

        void saveLoginEmail(String email);

        ArrayList<String> getLoginIdList();

        void discoverLogin();

        void loginWebview();

        void loginGoogle();

        void loginFacebook();
    }
}
