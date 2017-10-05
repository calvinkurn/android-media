package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.tokopedia.core.session.model.FacebookModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.session.session.fragment.LoginFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 04/11/2015.
 */
public interface Login {
    String TAG = "MNORMANSYAH";
    String messageTAG = "Login : ";

    String LOGIN_CACHE_KEY = "LOGIN_ID";
    String LOGIN_UUID_KEY = "LOGIN_UUID";
    String USER_EMAIL = "user_email";
    String USER_PASSWORD = "user_password";
    String UUID_KEY = "uuid";
    String DEFAULT_UUID_VALUE = "";
    String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
    String GO_TO_INDEX_KEY = "login";
    String PROVIDER_LIST = "provider";
    String PROVIDER_CACHE_KEY = "provider_cache";
    String BACKGROUND_CACHE_KEY = "background_cache_key";

    int EmailType = 0;
    int FacebookType = 1;
    int GooglePlusType = 2;
    int WebViewType = 3;

    String LOGIN_VIEW_MODEL_TAG = "LoginViewModel";
    String LOGIN_PROVIDER_TAG = "LoginProvider";

    String APP_TYPE = "app_type";
    String BIRTHDAY = "birthday";
    String EMAIL = "email";
    String GENDER = "gender";
    String GOOGLE_ID = "id";
    String NAME = "name";
    String PICTURE = "picture";

    String EDUCATION = "education";
    String FACEBOOK_TOKEN = "fb_token";
    String FACEBOOK_ID = "id";
    String INTERESTS = "interests";
    String WORK = "work";

    //------ACCOUNTS------
    String USER_NAME = "username";
    String PASSWORD = "password";
    String SCOPE = "scope";
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
    String MSISDN = "msisdn";
    String ACCESS_TOKEN = "access_token";

    String GRANT_PASSWORD = "password";
    String GRANT_SDK = "extension";
    String GRANT_WEBVIEW = "authorization_code";
    String GRANT_020 = "o2o_delegate";

    //------________------


    void initLoginInstance(Context context);

    ArrayList<String> getLastLoginIdList();

    void fetchIntenValues(Bundle extras);

    void initData();

    String getUUID();

    Object parseJSON(JSONObject Result, String loginType);

    boolean addAutoCompleteData(String newText);

    /**
     *
     * @param jsonObject
     * @return true if is_login is true that go to parent index home
     * false if is_login is false then go to security question
     */
    @Deprecated
    boolean isSuccessLogin(JSONObject jsonObject);

    void saveDatabeforeRotate(Bundle outstate, Object... data);

    void fetchDataAfterRotate(Bundle instate);

    boolean isAfterRotate();

    boolean isNoHapeVerified(JSONObject jsonObject);

    boolean isNeedToReverifyNoHape(JSONObject jsonObject);

    void startLoginWithGoogle(String LoginType, Object model);

    void loginFacebook(FacebookModel facebookModel, String token);

    void sendDataFromInternet(String loginType, Object... data);

    void updateViewModel(int type, Object... data);

    void setData(int type, Bundle data);

    void getToken(String emailType, LoginViewModel model);

    void downloadProviderLogin();

    void saveProvider(List<LoginProviderModel.ProvidersBean> listProvider);

    void getProvider();

    void unSubscribe();

    void doFacebookLogin(LoginFragment fragment, CallbackManager callbackManager);

    void setRememberAccountState(Boolean state);

    void saveAccountInfo(String userEmail, String userPassword);

    Boolean getSavedAccountState();

    String getSavedAccountEmail();

    String getSavedAccountPassword();

    void clearSavedAccount();
}
