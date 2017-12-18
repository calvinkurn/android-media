package com.tokopedia.session.register.view.viewlistener;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
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

        void onErrorGetFacebookCredential(String errorMessage);

        void onSuccessGetFacebookCredential(AccessToken accessToken);

        void dismissProgressBar();

        void onErrorRegisterSosmed(String errorMessage);

        void onSuccessLogin();

        void onGoToCreatePasswordPage(GetUserInfoDomainData userInfoDomainData);

        void clearToken();

        void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName, String email, String phone);

        void onGoToPhoneVerification();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getProvider();

        void registerWebview(FragmentActivity activity, Intent data);

        void getFacebookCredential(Fragment fragment, CallbackManager callbackManager);

        void registerFacebook(AccessToken accessToken);

        void clearToken();

        void registerGoogle(LoginGoogleModel model);
    }
}
