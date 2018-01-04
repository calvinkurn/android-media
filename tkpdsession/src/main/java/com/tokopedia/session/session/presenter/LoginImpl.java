package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.core.session.model.FacebookModel;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.EncoderDecoder;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.FacebookContainer;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.activity.SmartLockActivity;
import com.tokopedia.session.session.fragment.LoginFragment;
import com.tokopedia.session.session.intentservice.LoginService;
import com.tokopedia.session.session.interactor.LoginInteractor;
import com.tokopedia.session.session.interactor.LoginInteractorImpl;
import com.tokopedia.session.session.model.LoginEmailModel;
import com.tokopedia.session.session.model.LoginModel;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.tokopedia.core.service.constant.DownloadServiceConstant.LOGIN_ACCOUNTS_TOKEN;

/**
 * Created by m.normansyah on 04/11/2015.
 * Modified by m.normansyah on 17-11-2015, change to GSON
 * Modified by m.normansyah on 21-11-2015, move all download or upload to the internet
 */
public class LoginImpl implements Login {

    private static final String REMEMBER_ACC_STATE = "REMEMBER_ACC_STATE";
    private static final String STATE = "STATE";
    private static final String REMEMBER_ACC_INFO = "REMEMBER_ACC_INFO";
    private static final String ACC_EMAIL = "ACC_EMAIL";
    private static final String ACC_PASSWORD = "ACC_PASSWORD";
    private static final String KEY_IV = "tokopedia7891234";

    LoginView loginView;
    LocalCacheHandler cache;
    LocalCacheHandler loginUuid;
    LocalCacheHandler providerListCache;
    GCMHandler gcmHandler;
    SessionHandler sessionHandler;

    boolean GoToIndex;

    LoginViewModel loginViewModel;

    LoginInteractor facade;

    Context mContext;

    ArrayList<String> LoginIdList;

    LocalCacheHandler cacheState;
    LocalCacheHandler cacheInfo;
    private int successLoginVia;

    public LoginImpl(LoginView view) {
        loginView = view;
        facade = LoginInteractorImpl.createInstance(this);
    }

    @Override
    public void fetchIntenValues(Bundle extras) {
        if (extras != null) {
            if (extras.getString("mEmail") != null)
                loginViewModel.setUsername(extras.getString("mEmail"));
            GoToIndex = extras.getBoolean("GoToIndex");
        }
    }

    @Override
    public void initLoginInstance(Context context) {
        mContext = context;

        cache = new LocalCacheHandler(context, LOGIN_CACHE_KEY);
        LoginIdList = getLastLoginIdList();
        if (!isAfterRotate()) {
            loginViewModel = new LoginViewModel();
        }

//        if(mEmail!= null)// set username as email from fetch data
//            questionFormModel.setUsername(mEmail);

        // send to analytic handlers
        ScreenTracking.screen(AppScreen.SCREEN_LOGIN);
        loginUuid = new LocalCacheHandler(mContext, LOGIN_UUID_KEY);
        providerListCache = new LocalCacheHandler(mContext, PROVIDER_LIST);
        gcmHandler = new GCMHandler(context);
        sessionHandler = new SessionHandler(context);
        cacheState = new LocalCacheHandler(mContext, REMEMBER_ACC_STATE);
        cacheInfo = new LocalCacheHandler(mContext, REMEMBER_ACC_INFO);
    }

    @Override
    public ArrayList<String> getLastLoginIdList() {
        if (cache == null)
            throw new RuntimeException(messageTAG + " cache should be initialize!!!");
        return LoginIdList = cache.getArrayListString(LOGIN_CACHE_KEY);
    }

    @Override
    public void initData() {
        loginView.setAutoCompleteAdapter(LoginIdList);
        loginView.showProgress(loginViewModel.isProgressShow());
        if (sessionHandler.isV4Login()) {
            loginView.destroyActivity();
        }
        getProvider();
    }

    public void getProvider() {
        if (loginView.checkHasNoProvider()) {
            loginView.addProgressbar();
            List<LoginProviderModel.ProvidersBean> providerList = loadProvider();
            if (providerList == null || providerListCache.isExpired()) {
                downloadProviderLogin();
            } else {
                loginView.removeProgressBar();
                loginView.showProvider(providerList);
            }
        }
    }

    @Override
    public void unSubscribe() {
        facade.unSubscribe();
    }

    @Override
    public void doFacebookLogin(final LoginFragment fragment, final CallbackManager callbackManager) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, FacebookContainer.readPermissions);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Set<String> declinedPermissions = loginResult.getAccessToken().getDeclinedPermissions();
                if (hasDeclinedPermission(declinedPermissions)) {
                    doFacebookLogin(fragment, callbackManager);
                } else {
                    requestProfileFacebook(loginResult);
                }
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
                Log.i(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException e) {
                if (e instanceof FacebookAuthorizationException) {
                    LoginManager.getInstance().logOut();
                }
                loginView.showError(fragment.getActivity().getString(R.string.msg_network_error));
            }
        });
    }

    @Override
    public void setRememberAccountState(Boolean state) {
        cacheState.putBoolean(STATE, state);
        cacheState.applyEditor();
    }

    @Override
    public void saveAccountInfo(String userEmail, String userPassword) {
        String encryptedPassword = EncoderDecoder.Encrypt(userPassword, KEY_IV);
        cacheInfo.putString(ACC_EMAIL, userEmail);
        cacheInfo.putString(ACC_PASSWORD, encryptedPassword);
        cacheInfo.applyEditor();
    }

    @Override
    public Boolean getSavedAccountState() {
        return cacheState.getBoolean(STATE, true);
    }

    @Override
    public String getSavedAccountEmail() {
        return cacheInfo.getString(ACC_EMAIL, "");
    }

    @Override
    public String getSavedAccountPassword() {
        String encryptedPassword = cacheInfo.getString(ACC_PASSWORD, "");
        if (encryptedPassword.equals("")) return encryptedPassword;
        String decryptedPassword = EncoderDecoder.Decrypt(encryptedPassword,
                KEY_IV);
        return decryptedPassword;
    }

    @Override
    public void clearSavedAccount() {
        LocalCacheHandler.clearCache(mContext, REMEMBER_ACC_INFO);
        LocalCacheHandler.clearCache(mContext, REMEMBER_ACC_STATE);
    }

    private void requestProfileFacebook(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        FacebookModel facebookModel =
                                new GsonBuilder().create().fromJson(String.valueOf(object), FacebookModel.class);
                        loginFacebook(facebookModel, loginResult.getAccessToken().getToken());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,birthday,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private boolean hasDeclinedPermission(Set<String> declinedPermissions) {
        return declinedPermissions.size() > 0 && FacebookContainer.readPermissions.containsAll(declinedPermissions);
    }

    public void downloadProviderLogin() {
        facade.downloadProvider(loginView.getActivity(), new LoginInteractor.DiscoverLoginListener() {
            @Override
            public void onSuccess(LoginProviderModel result) {
                loginView.removeProgressBar();
                loginView.showProvider(result.getProviders());
            }

            @Override
            public void onError(String s) {
                loginView.onMessageError(DownloadService.DISCOVER_LOGIN, s);
            }

            @Override
            public void onTimeout() {
                loginView.onMessageError(DownloadService.DISCOVER_LOGIN, "");
            }

            @Override
            public void onThrowable(Throwable e) {
                loginView.onMessageError(DownloadService.DISCOVER_LOGIN, "");
            }
        });
    }

    @Override
    public void saveProvider(List<LoginProviderModel.ProvidersBean> listProvider) {
        String cache = new GsonBuilder().create().toJson(loadProvider());
        String listProviderString = new GsonBuilder().create().toJson(listProvider);
        if (!cache.equals(listProviderString)) {
            providerListCache.putString(PROVIDER_CACHE_KEY, listProviderString);
            providerListCache.setExpire(3600);
            providerListCache.applyEditor();
        }
    }

    private List<LoginProviderModel.ProvidersBean> loadProvider() {
        String cache = providerListCache.getString(PROVIDER_CACHE_KEY);
        Type type = new TypeToken<List<LoginProviderModel.ProvidersBean>>() {
        }.getType();
        return new GsonBuilder().create().fromJson(cache, type);
    }


    public static void login(int DownServiceConstType, Context context, String action, Object... data) {
        boolean isNeedLogin = true;
        switch (action) {
            case LoginModel.EmailType:
                LoginViewModel loginViewModel = (LoginViewModel) data[0];// override the instance here
                LocalCacheHandler loginUuid = new LocalCacheHandler(context, LOGIN_UUID_KEY);
                String uuid = loginUuid.getString(UUID_KEY, DEFAULT_UUID_VALUE);
                loginViewModel.setUuid(uuid);// store uuid


                Bundle bundle = new Bundle();
                bundle.putParcelable(DownloadService.LOGIN_VIEW_MODEL_KEY, Parcels.wrap(loginViewModel));
                bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);

                ((SessionView) context).sendDataFromInternet(DownServiceConstType, bundle);
                break;
        }
    }

    @Override
    public void sendDataFromInternet(String action, Object... data) {
        boolean isNeedLogin = true;
        Bundle bundle;
        switch (action) {
            case LoginModel.EmailType:
                getToken(action, (LoginViewModel) data[0]);
                UserAuthenticationAnalytics.setActiveAuthenticationMedium(AppEventTracking.GTMCacheValue.EMAIL);
                break;
            case LoginModel.GoogleType:
                UserAuthenticationAnalytics.setActiveAuthenticationMedium(AppEventTracking.GTMCacheValue.GMAIL);
                LoginGoogleModel loginGoogleModel = (LoginGoogleModel) data[0];
                loginGoogleModel.setUuid(getUUID());

                bundle = new Bundle();
                bundle.putParcelable(DownloadService.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(loginGoogleModel));
                bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);

                ((SessionView) mContext).sendDataFromInternet(DownloadService.LOGIN_GOOGLE, bundle);
                break;
            case LoginModel.FacebookType:
                UserAuthenticationAnalytics.setActiveAuthenticationMedium(AppEventTracking.GTMCacheValue.FACEBOOK);
                LoginFacebookViewModel loginFacebookViewModel = (LoginFacebookViewModel) data[0];
                loginFacebookViewModel.setUuid(getUUID());
                bundle = new Bundle();
                bundle.putParcelable(DownloadService.LOGIN_FACEBOOK_MODEL_KEY, Parcels.wrap(loginFacebookViewModel));
                bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);

                ((SessionView) mContext).sendDataFromInternet(DownloadService.LOGIN_FACEBOOK, bundle);
                break;

            case LoginModel.WebViewType:
                bundle = (Bundle) data[0];
                bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);
                bundle.putString(UUID_KEY, getUUID());

                ((SessionView) mContext).sendDataFromInternet(DownloadService.LOGIN_WEBVIEW, bundle);
                break;

        }
    }

    @Override
    public String getUUID() {
        return loginUuid.getString(UUID_KEY, DEFAULT_UUID_VALUE);
    }

    @Override
    public Object parseJSON(JSONObject Result, String loginType) {
        throw new RuntimeException("don't use this method!!!");
    }

    @Override
    public boolean addAutoCompleteData(String newText) {
        if (!LoginIdList.contains(newText) && !checkEmptyText(newText)) {
            LoginIdList.add(newText);// add new text
            cache.putArrayListString(LOGIN_CACHE_KEY, LoginIdList);// flush to the cache
            cache.applyEditor();// commit changes
            return true;
        }
        return false;
    }

    private boolean checkEmptyText(String newText) {
        return newText.replaceAll("\\s+", "").length() == 0;
    }


    @Override
    public boolean isSuccessLogin(JSONObject jsonObject) {
        throw new RuntimeException("don't use this method!!!");
    }

    @Override
    public void saveDatabeforeRotate(Bundle outstate, Object... data) {
        loginViewModel.setUsername((String) data[0]);
        loginViewModel.setPassword((String) data[1]);
        outstate.putParcelable(LOGIN_VIEW_MODEL_TAG, Parcels.wrap(loginViewModel));
    }

    @Override
    public void fetchDataAfterRotate(Bundle instate) {
        if (instate != null) {
            loginViewModel = Parcels.unwrap(instate.getParcelable(LOGIN_VIEW_MODEL_TAG));
        }
    }

    @Override
    public boolean isAfterRotate() {
        return loginViewModel != null;
    }

    /**
     * @param jsonObject @see LoginImpl#parseJSON
     * @return true jika perlu verifikasi ulang nomor hapenya, false jika nomor hape tidak perlu verifikasi ulang
     */
    @Override
    public boolean isNeedToReverifyNoHape(JSONObject jsonObject) {
        return jsonObject.optInt("msisdn_show_dialog") != 0;
    }

    /**
     * @param jsonObject @see LoginImpl#parseJSON
     * @return true jika nomor hape telah terverifikasi, false jika nomor hape tidak terverifikasi
     */
    @Override
    public boolean isNoHapeVerified(JSONObject jsonObject) {
        return jsonObject.optInt("msisdn_is_verified") != 0;
    }

    @Override
    public void startLoginWithGoogle(String LoginType, Object model) {
        if (LoginType != LoginModel.GoogleType)
            throw new RuntimeException("this is only for google login !!!");

        sendDataFromInternet(LoginModel.GoogleType, model);
    }

    @Override
    public void loginFacebook(FacebookModel facebookModel, String token) {
        LoginFacebookViewModel loginFacebookViewModel = new LoginFacebookViewModel();
        if (facebookModel != null) {
            loginFacebookViewModel.setFullName(facebookModel.getName());
            loginFacebookViewModel.setGender(facebookModel.getGender());
            loginFacebookViewModel.setBirthday(facebookModel.getBirthdayConverted());
            loginFacebookViewModel.setFbToken(token);
            loginFacebookViewModel.setFbId(facebookModel.getId());
            loginFacebookViewModel.setEmail(facebookModel.getEmail());
        }
        sendDataFromInternet(LoginModel.FacebookType, loginFacebookViewModel);
    }

    @Override
    public void updateViewModel(int type, Object... data) {
        switch (type) {
            case LoginViewModel.ISPROGRESSSHOW:
                loginViewModel.setIsProgressShow((boolean) data[0]);

                break;
            case LoginViewModel.PASSWORD:
            case LoginViewModel.USERNAME:
                Log.d(TAG, messageTAG + " update data model current not implemented !!!");
                break;
        }
    }

    @Override
    public void setData(int type, Bundle data) {
        switch (type) {
            case DownloadServiceConstant.MAKE_LOGIN:
                // if need to move to security
                if (data.getBoolean(DownloadService.LOGIN_MOVE_SECURITY, false)) {// move to security
                    AccountsParameter modelData = data.getParcelable("accounts");
                    SecurityModel loginSecurityModel = data.getParcelable(DownloadService.LOGIN_SECURITY_QUESTION_DATA);
                    String email = "";
                    if (modelData != null) {
                        email = modelData.getEmail();
                    } else if (data.getString(AppEventTracking.EMAIL_KEY) != null) {
                        email = data.getString(AppEventTracking.EMAIL_KEY);
                    }
                    loginView.moveToFragmentSecurityQuestion(
                            loginSecurityModel.getSecurity().getUser_check_security_1(),
                            loginSecurityModel.getSecurity().getUser_check_security_2(),
                            loginSecurityModel.getUser_id(),
                            email);
                    loginView.setSmartLock(SmartLockActivity.RC_SAVE_SECURITY_QUESTION, ((AccountsParameter) data.get(LoginService.ACCOUNTS)).getEmail(), ((AccountsParameter) data.get(LoginService.ACCOUNTS)).getPassword());
                } else if (sessionHandler.isV4Login()) {// go back to home
                    loginView.triggerSaveAccount();
                    loginView.triggerClearCategoryData();
                    successLoginVia = (data.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, LOGIN_ACCOUNTS_TOKEN));
                    if (successLoginVia == LOGIN_ACCOUNTS_TOKEN) {
                        loginView.setSmartLock(SmartLockActivity.RC_SAVE, ((AccountsParameter) data.get(LoginService.ACCOUNTS)).getEmail(), ((AccountsParameter) data.get(LoginService.ACCOUNTS)).getPassword());
                    } else {
                        loginView.destroyActivity();
                    }
                } else if (data.getInt(DownloadService.VALIDATION_OF_DEVICE_ID, LoginEmailModel.INVALID_DEVICE_ID) == LoginEmailModel.INVALID_DEVICE_ID) {

                }
                break;
            case DownloadService.LOGIN_ACCOUNTS_INFO:
                String uuid = getUUID();
                data.putString(UUID_KEY, uuid);
                InfoModel infoModel = data.getParcelable(DownloadServiceConstant.INFO_BUNDLE);
                Parcelable parcelable = data.getParcelable(DownloadServiceConstant.EXTRA_TYPE);

                if (infoModel.isCreatedPassword()) {
                    ((SessionView) mContext).sendDataFromInternet(DownloadService.MAKE_LOGIN, data);
                } else {
                    CreatePasswordModel createPasswordModel = new CreatePasswordModel();
                    createPasswordModel = setModelFromParcelable(createPasswordModel, parcelable, infoModel);
                    data.putBoolean(DownloadServiceConstant.LOGIN_MOVE_REGISTER_THIRD, true);
                    data.putParcelable(DownloadServiceConstant.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(createPasswordModel));
                    ((SessionView) mContext).moveToRegisterPassPhone(createPasswordModel, infoModel.getCreatePasswordList(), data);
                }
                break;
            case DownloadServiceConstant.DISCOVER_LOGIN:
                LoginProviderModel loginProviderModel = data.getParcelable(DownloadServiceConstant.LOGIN_PROVIDER);
                loginView.removeProgressBar();
                loginView.showProvider(loginProviderModel.getProviders());
                break;
        }
    }

    private CreatePasswordModel setModelFromParcelable(CreatePasswordModel createPasswordModel, Parcelable parcelable, InfoModel infoModel) {
        createPasswordModel.setFullName(infoModel.getName());
        createPasswordModel.setEmail(infoModel.getEmail());
        if (infoModel.getPhone() != null)
            createPasswordModel.setMsisdn(infoModel.getPhone());
        return createPasswordModel;
    }

    @Override
    public void getToken(String emailType, LoginViewModel data) {
        boolean isNeedLogin = true;
        loginViewModel = data;// override the instance here
        loginViewModel.setUuid(getUUID());// store uuid

        Bundle bundle = new Bundle();
        bundle.putParcelable(DownloadService.LOGIN_VIEW_MODEL_KEY, Parcels.wrap(loginViewModel));
        bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);

        ((SessionView) mContext).sendDataFromInternet(LOGIN_ACCOUNTS_TOKEN, bundle);
    }
}
