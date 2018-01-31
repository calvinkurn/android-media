package com.tokopedia.session.login.loginemail.view.viewlistener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
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

        void onSuccessLogin();

        void onErrorLogin(String errorMessage);

        void setAutoCompleteAdapter(ArrayList<String> listId);

        void showLoadingDiscover();

        void dismissLoadingDiscover();

        void onErrorDiscoverLogin(String errorMessage);

        void onSuccessDiscoverLogin(ArrayList<DiscoverItemViewModel> providers);

        GetFacebookCredentialSubscriber.GetFacebookCredentialListener getFacebookCredentialListener();

        void onGoToCreatePasswordPage(GetUserInfoDomainData getUserInfoDomainData);

        void onGoToPhoneVerification();

        void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName, String email, String phone);

        void setSmartLock();

        void resetToken();

        void onErrorLogin(String errorMessage, int codeError);

        void onGoToActivationPage(String email);

        void onSuccessLoginEmail();

        void onSuccessLoginSosmed(String loginMethod);

        Context getContext();

        void disableArrow();

        void enableArrow();
    }

    interface Presenter extends CustomerPresenter<View> {

        void login(String email, String password);

        void saveLoginEmail(String email);

        ArrayList<String> getLoginIdList();

        void discoverLogin();

        void loginWebview(Intent data);

        void loginGoogle(String accessToken, String email);

        void getFacebookCredential(Fragment fragment, CallbackManager callbackManager);

        void loginFacebook(AccessToken accessToken, String email);

        void resetToken();
    }
}
