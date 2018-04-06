package com.tokopedia.session.register.view.viewlistener;

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
 * @author by nisie on 10/10/17.
 */

public interface RegisterInitial {
    interface View extends CustomerView {

        void showLoadingDiscover();

        void onErrorDiscoverRegister(String errorMessage);

        void onSuccessDiscoverRegister(ArrayList<DiscoverItemViewModel> discoverViewModel);

        void dismissLoadingDiscover();

        void showProgressBar();

        void dismissProgressBar();

        void onErrorRegisterSosmed(String errorMessage);

        void onSuccessRegisterSosmed(String methodName);

        void onGoToCreatePasswordPage(GetUserInfoDomainData userInfoDomainData);

        void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName, String email, String phone);

        void onGoToPhoneVerification();

        GetFacebookCredentialSubscriber.GetFacebookCredentialListener getFacebookCredentialListener();

        void onForbidden();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getProvider();

        void registerWebview(Intent data);

        void getFacebookCredential(Fragment fragment, CallbackManager callbackManager);

        void registerFacebook(AccessToken accessToken);

        void registerGoogle(String model);
    }
}
