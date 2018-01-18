package com.tokopedia.session.domain.interactor;

import com.tokopedia.core.session.model.AccountsModel;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.SecurityModel;

/**
 * Created by stevenfredian on 1/27/17.
 */
@Deprecated
public interface SignInInteractor {

    String UUID_KEY = "uuid";

    //------ACCOUNTS------
    String USER_NAME = "username";
    String PASSWORD = "password";
    String PASSWORD_TYPE = "password_type";
    String USER_ID = "user_id";
    String GRANT_TYPE = "grant_type";
    String SOCIAL_ID = "social_id";
    String SOCIAL_TYPE = "social_type";
    String EMAIL_ACCOUNTS = "email";
    String PICTURE_ACCOUNTS = "picture";
    String FULL_NAME = "full_name";
    String BIRTHDATE = "birthdate";
    String GENDER_ACCOUNTS = "gender";
    String CODE = "code";
    String REDIRECT_URI = "redirect_uri";
    String ACTIVATION_CODE = "activation_code";
    String ATTEMPT = "attempt";

    final String GRANT_PASSWORD = "password";
    final String GRANT_SDK = "extension";
    final String GRANT_WEBVIEW = "authorization_code";

    void handleAccounts(AccountsParameter parameter, SignInListener signInListener);

    interface SignInListener{
        void onSuccess(AccountsModel result);

        void onError(String error);

        void moveToSecurityQuestion(SecurityModel securityModel);

        void moveToCreatePassword(InfoModel infoModel);
    }
}
