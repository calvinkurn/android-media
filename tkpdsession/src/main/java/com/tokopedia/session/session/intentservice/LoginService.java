package com.tokopedia.session.session.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.Nishikino;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.model.AccountsModel;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.model.ErrorModel;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.session.model.TokenModel;
import com.tokopedia.core.util.EncoderDecoder;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.activation.view.viewmodel.LoginTokenViewModel;
import com.tokopedia.session.session.model.LoginEmailModel;
import com.tokopedia.session.session.model.LoginThirdModel;
import com.tokopedia.session.session.presenter.Login;
import com.tokopedia.session.session.subscriber.AccountSubscriber;
import com.tokopedia.session.session.subscriber.BaseAccountSubscriber;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class LoginService extends IntentService implements DownloadServiceConstant {

    public static final String TAG = "LoginService";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public static final String EXTRA_PARCELABLE = "EXTRA_PARCELABLE";
    public static final String ACCOUNTS = "accounts";

    private ResultReceiver receiver;
    private SessionHandler sessionHandler;

    static String emailV2;
    static String passwordV2;
    static int loginType;

    public int typeAccess;

    public LoginService() {
        super("LoginService");
    }

    public static void startLogin(Context context, LoginResultReceiver receiver, Bundle bundle, int type) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, LoginService.class);
        boolean isNeedLogin = bundle.getBoolean(IS_NEED_LOGIN, false);
        if (receiver != null)
            intent.putExtra(DownloadService.RECEIVER, receiver);

        if (bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0) != 0) {
            intent.putExtra(AppEventTracking.GTMKey.ACCOUNTS_TYPE,
                    bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0));
        }

        intent.putExtra(TYPE, type);
        intent.putExtra(IS_NEED_LOGIN, isNeedLogin);

        LocalCacheHandler loginUuid = new LocalCacheHandler(context, Login.LOGIN_UUID_KEY);
        String uuid = loginUuid.getString(Login.UUID_KEY, Login.DEFAULT_UUID_VALUE);
        intent.putExtra(Login.UUID_KEY, uuid);

        switch (type) {
            case LOGIN_ACCOUNTS_TOKEN:
                LoginViewModel model = Parcels.unwrap(bundle.getParcelable(LOGIN_VIEW_MODEL_KEY));
                intent.putExtra(LOGIN_VIEW_MODEL_KEY, Parcels.wrap(model));
                break;
            case LOGIN_UNIQUE_CODE:
                LoginTokenViewModel tokenModel = bundle.getParcelable(LOGIN_TOKEN_MODEL_KEY);
                intent.putExtra(LOGIN_TOKEN_MODEL_KEY, tokenModel);
                break;
            case REGISTER_GOOGLE:
            case LOGIN_GOOGLE:
                LoginGoogleModel loginGoogleModel = Parcels.unwrap(bundle.getParcelable(LOGIN_GOOGLE_MODEL_KEY));
                intent.putExtra(LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(loginGoogleModel));
                break;
            case LOGIN_FACEBOOK:
            case REGISTER_FACEBOOK:
                LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(bundle.getParcelable(LOGIN_FACEBOOK_MODEL_KEY));
                intent.putExtra(LOGIN_FACEBOOK_MODEL_KEY, Parcels.wrap(loginFacebookViewModel));
                break;
            case LOGIN_WEBVIEW:
                intent.putExtra(Login.CODE, bundle.getString("code"));
                intent.putExtra(Login.REDIRECT_URI, bundle.getString("server") + bundle.getString("path"));
                break;
            case LOGIN_ACCOUNTS_INFO:
                intent.putExtra(EXTRA_PARCELABLE, bundle.getParcelable(EXTRA_TYPE));
                intent.putExtra(TOKEN_BUNDLE, bundle.getParcelable(TOKEN_BUNDLE));
                break;
            case MAKE_LOGIN:
                intent.putExtra(Login.UUID_KEY, bundle.getString(Login.UUID_KEY));
                intent.putExtra(PROFILE_BUNDLE, bundle.getParcelable(PROFILE_BUNDLE));
                break;
            default:
                break;
        }
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        receiver = intent.getParcelableExtra(RECEIVER);
        int type = intent.getIntExtra(TYPE, INVALID_TYPE);
        if (intent.getIntExtra(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0) != 0) {
            typeAccess = intent.getIntExtra(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0);
        } else {
            typeAccess = type;
        }
        sessionHandler = new SessionHandler(getApplicationContext());

        Bundle running = new Bundle();
        Bundle bundle = new Bundle();
        AccountsParameter data = new AccountsParameter();
        AccountsService accountsService;

        running.putInt(TYPE, type);
        running.putBoolean(LOGIN_SHOW_DIALOG, true);
        receiver.send(STATUS_RUNNING, running);

        switch (type) {
            case LOGIN_ACCOUNTS_TOKEN:

                LoginViewModel xxx = Parcels.unwrap(intent.getParcelableExtra(LOGIN_VIEW_MODEL_KEY));
                emailV2 = xxx.getUsername();
                passwordV2 = xxx.getPassword();
                loginType = LOGIN_EMAIL;

                data.setEmail(xxx.getUsername());
                data.setPassword(xxx.getPassword());
                data.setLoginType(LOGIN_EMAIL);
                data.setGrantType(Login.GRANT_PASSWORD);
                data.setUUID(xxx.getUuid());
                handleAccounts(data);
                break;
            case LOGIN_UNIQUE_CODE:

                LoginTokenViewModel tokenViewModel = intent.getParcelableExtra(LOGIN_TOKEN_MODEL_KEY);

                TokenModel tokenModel = new TokenModel();
                tokenModel.setAccessToken(tokenViewModel.getAccessToken());
                tokenModel.setTokenType(tokenViewModel.getTokenType());
                tokenModel.setExpiresIn(Integer.parseInt(tokenViewModel.getExpiresIn()));

                data.setTokenModel(tokenModel);
                handleAccounts(data);
                break;

            case REGISTER_GOOGLE:
            case LOGIN_GOOGLE:

                LoginGoogleModel loginGoogleModel = Parcels.unwrap(intent.getParcelableExtra(LOGIN_GOOGLE_MODEL_KEY));

                loginType = LOGIN_GOOGLE;

                sendAuthenticateGTMEvent(loginGoogleModel);

                data.setGrantType(Login.GRANT_SDK);
                data.setSocialType(Login.GooglePlusType);
                data.setParcelable(Parcels.wrap(loginGoogleModel));
                data.setUUID(intent.getStringExtra(Login.UUID_KEY));
                handleAccounts(data);
                break;


            case LOGIN_FACEBOOK:
            case REGISTER_FACEBOOK:

                LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(intent.getParcelableExtra(LOGIN_FACEBOOK_MODEL_KEY));

                loginType = LOGIN_FACEBOOK;

                sendAuthenticateGTMEvent(loginFacebookViewModel);

                data.setGrantType(Login.GRANT_SDK);
                data.setSocialType(Login.FacebookType);
                data.setParcelable(Parcels.wrap(loginFacebookViewModel));
                data.setUUID(intent.getStringExtra(Login.UUID_KEY));
                handleAccounts(data);
                break;

            case LOGIN_WEBVIEW:

                loginType = LOGIN_WEBVIEW;

                data.setCode(intent.getStringExtra(Login.CODE));
                data.setRedirectUri("https://" + intent.getStringExtra(Login.REDIRECT_URI));
                data.setGrantType(Login.GRANT_WEBVIEW);
                data.setUUID(intent.getStringExtra(Login.UUID_KEY));
                handleAccounts(data);
                break;

            case LOGIN_ACCOUNTS_INFO:

                Parcelable parcelable = intent.getParcelableExtra(EXTRA_PARCELABLE);

                String authKey = sessionHandler.getAccessToken(this);
                authKey = sessionHandler.getTokenType(this) + " " + authKey;

                bundle.putString(AccountsService.AUTH_KEY, authKey);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().getInfo(AuthUtil.generateParams(getApplicationContext(), new HashMap<String, String>()))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new AccountSubscriber(type, receiver, sessionHandler, parcelable));
                break;

            case MAKE_LOGIN:

                Map<String, String> params = new HashMap<>();
                params = AuthUtil.generateParams(this, params);
                params.put(Login.UUID_KEY, intent.getStringExtra(Login.UUID_KEY));
                params.put(Login.USER_ID, SessionHandler.getTempLoginSession(this));

                authKey = sessionHandler.getAccessToken(this);
                authKey = sessionHandler.getTokenType(this) + " " + authKey;

                bundle.putString(AccountsService.AUTH_KEY, authKey);
                bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().makeLogin(params)
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(getApplicationContext(), type, receiver, sessionHandler));
                break;
        }
    }

    private class Subscriber extends BaseAccountSubscriber {

        public Subscriber(Context context, int type, ResultReceiver receiver, SessionHandler sessionHandler) {
            super(context, type, receiver, sessionHandler);
        }

        public void onSuccessResponse(JSONObject jsonObject) {
            Bundle result;
            switch (type) {
                case MAKE_LOGIN:
                    result = new Bundle();
                    result.putInt(TYPE, type);
                    result.putInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, typeAccess);
                    SecurityModel securityModel = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), SecurityModel.class);
                    if (securityModel.getIs_login().equals(false) || securityModel.getIs_login().equals("false")) {
                        // save user id
                        sessionHandler.setTempLoginSession(Integer.toString(securityModel.getUser_id()));
                        //[START] Save Security Question
                        result.putParcelable(LOGIN_SECURITY_QUESTION_DATA, securityModel);
                        result.putBoolean(LOGIN_MOVE_SECURITY, true);
                        result.putBoolean(LOGIN_ACTIVATION_RESENT, false);
                    } else {
                        AccountsModel accountsModel = new GsonBuilder()
                                .create().fromJson(jsonObject.toString(), AccountsModel.class);
                        setLoginSession(accountsModel);
                        sessionHandler.setPhoneNumber(sessionHandler.getTempPhoneNumber(context));
                        sessionHandler.setGoldMerchant(context, accountsModel.getShopIsGold());
                        result.putBoolean(LOGIN_MOVE_SECURITY, false);
                        result.putBoolean(LOGIN_ACTIVATION_RESENT, false);
                        result.putInt(VALIDATION_OF_DEVICE_ID, accountsModel.getIsRegisterDevice());
                        result.putString(AppEventTracking.USER_ID_KEY, accountsModel.getUserId() + "");
                        result.putString(AppEventTracking.FULLNAME_KEY, accountsModel.getFullName());
                    }
                    receiver.send(DownloadService.STATUS_FINISHED, result);

                    break;
            }
        }
    }

    private void handleAccounts(final AccountsParameter data) {
        Observable.just(data)
                .flatMap(new Func1<AccountsParameter, Observable<AccountsParameter>>() {
                    @Override
                    public Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if (accountsParameter.getTokenModel() == null)
                            return getObservableAccountsToken(accountsParameter);
                        else
                            return Observable.just(accountsParameter);
                    }
                })

                .flatMap(new Func1<AccountsParameter, Observable<AccountsParameter>>() {
                    @Override
                    public Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if (accountsParameter.getErrorModel() == null) {
                            TokenModel tokenModel = accountsParameter.getTokenModel();
                            sessionHandler.setToken(tokenModel.getAccessToken(),
                                    tokenModel.getTokenType(), EncoderDecoder.Encrypt(tokenModel.getRefreshToken(), SessionHandler.getRefreshTokenIV(getApplicationContext()))
                            );
                        }
                        return Observable.just(accountsParameter);
                    }
                })

                .flatMap(new Func1<AccountsParameter, Observable<AccountsParameter>>() {
                    @Override
                    public Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if (accountsParameter.getErrorModel() == null) {
                            return getObservableAccountsInfo(accountsParameter);
                        } else {
                            return Observable.just(accountsParameter);
                        }
                    }
                })
                .doOnNext(new Action1<AccountsParameter>() {
                    @Override
                    public void call(AccountsParameter accountsParameter) {
                        if (accountsParameter.getErrorModel() == null)
                            sessionHandler.setWalletRefreshToken(
                                    accountsParameter.getInfoModel().getWalletRefreshToken()
                            );
                    }
                })
                .flatMap(new Func1<AccountsParameter, Observable<AccountsParameter>>() {
                    @Override
                    public Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if (accountsParameter.getInfoModel() != null
                                && accountsParameter.getInfoModel().isCreatedPassword()
                                && accountsParameter.getErrorModel() == null) {
                            return getObservableMakeLogin(accountsParameter);
                        } else {
                            return Observable.just(accountsParameter);
                        }
                    }
                })

                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<AccountsParameter>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Bundle result = new Bundle();
                        result.putInt(TYPE, DownloadService.LOGIN_ACCOUNTS_TOKEN);
                        if (e instanceof SocketTimeoutException) {
                            result.putString(MESSAGE_ERROR_FLAG, "Terjadi kesalahan koneksi, silahkan coba lagi");
                        } else {
                            result.putString(MESSAGE_ERROR_FLAG, "Silahkan coba lagi");
                        }
                        receiver.send(DownloadService.STATUS_ERROR, result);
                    }

                    @Override
                    public void onNext(AccountsParameter accountsParameter) {
                        Bundle result = new Bundle();
                        result.putInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, typeAccess);
                        // make login
                        if (accountsParameter.getInfoModel() != null && accountsParameter.getInfoModel().isCreatedPassword()) {
                            result.putInt(TYPE, DownloadService.MAKE_LOGIN);
                            SecurityModel securityModel = accountsParameter.getSecurityModel();
                            if (securityModel != null) {
                                sessionHandler.setTempPhoneNumber(accountsParameter.getInfoModel().getPhone());
                                sessionHandler.setTempLoginName(accountsParameter.getInfoModel().getName());
                                sessionHandler.setTempLoginSession(Integer.toString(securityModel.getUser_id()));
                                result.putParcelable(LOGIN_SECURITY_QUESTION_DATA, securityModel);
                                result.putParcelable(ACCOUNTS, accountsParameter);
                            } else {
                                AccountsModel accountsModel = accountsParameter.getAccountsModel();
                                setLoginSession(accountsModel);
                                SessionHandler.setPhoneNumber(accountsParameter.getInfoModel().getPhone());
                                result.putString(AppEventTracking.USER_ID_KEY, accountsModel.getUserId() + "");
                                result.putString(AppEventTracking.FULLNAME_KEY, accountsModel.getFullName());
                                result.putString(AppEventTracking.EMAIL_KEY, accountsParameter.getEmail());
                                result.putInt(VALIDATION_OF_DEVICE_ID, accountsModel.getIsRegisterDevice());
                                result.putParcelable(ACCOUNTS, accountsParameter);
                                SessionHandler.setGoldMerchant(getApplicationContext(), accountsModel.getShopIsGold());
                            }

                            result.putBoolean(LOGIN_MOVE_SECURITY, accountsParameter.isMoveSecurity());
                            result.putBoolean(LOGIN_ACTIVATION_RESENT, accountsParameter.isActivationResent());
                            receiver.send(DownloadService.STATUS_FINISHED, result);
                        }
                        //showing error
                        else if (accountsParameter.getErrorModel() != null) {
                            result.putInt(TYPE, typeAccess);
                            result.putString(MESSAGE_ERROR_FLAG, accountsParameter.getErrorModel().getErrorDescription());
                            receiver.send(DownloadService.STATUS_ERROR, result);
                        }
                        // need create password
                        else {
                            result.putInt(TYPE, DownloadService.LOGIN_ACCOUNTS_INFO);
                            result.putBoolean(DO_LOGIN, true);
                            result.putParcelable(INFO_BUNDLE, accountsParameter.getInfoModel());
                            result.putParcelable(EXTRA_TYPE, accountsParameter.getParcelable());
                            sessionHandler.setTempLoginSession(String.valueOf(accountsParameter.getInfoModel().getUserId()));
                            receiver.send(DownloadService.STATUS_FINISHED, result);
                        }
                    }
                });
    }

    public Observable<AccountsParameter> getObservableAccountsToken(AccountsParameter accountsParameter) {
        Bundle bundle = new Bundle();
        final Map<String, String> params = new HashMap<>();
        Parcelable parcelable = accountsParameter.getParcelable();

        params.put(Login.GRANT_TYPE, accountsParameter.getGrantType());

        switch (accountsParameter.getGrantType()) {
            case Login.GRANT_PASSWORD:
                params.put(Login.USER_NAME, accountsParameter.getEmail());
                params.put(Login.PASSWORD, accountsParameter.getPassword());
                break;
            case Login.GRANT_SDK:
                params.put(Login.SOCIAL_TYPE, String.valueOf(accountsParameter.getSocialType()));
                if (Parcels.unwrap(parcelable) instanceof LoginFacebookViewModel) {
                    LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(parcelable);
                    accountsParameter.setEmail(loginFacebookViewModel.getEmail());
                    params.put(Login.ACCESS_TOKEN, loginFacebookViewModel.getFbToken());
                } else if (Parcels.unwrap(parcelable) instanceof LoginGoogleModel) {
                    LoginGoogleModel loginGoogleModel = Parcels.unwrap(parcelable);
                    accountsParameter.setEmail(loginGoogleModel.getEmail());
                    params.put(Login.ACCESS_TOKEN, loginGoogleModel.getAccessToken());
                }
                break;
            case Login.GRANT_WEBVIEW:
                params.put(Login.CODE, accountsParameter.getCode());
                params.put(Login.REDIRECT_URI, accountsParameter.getRedirectUri());
                break;
            default:
                throw new RuntimeException("Invalid Observable to get Token");
        }

        bundle.putBoolean(AccountsService.IS_BASIC, true);
        AccountsService accountService = new AccountsService(bundle);
        Observable<Response<String>> observable = accountService.getApi()
                .getTokenOld(AuthUtil
                        .generateParams(getApplicationContext(), params));
        return Observable.zip(Observable.just(accountsParameter), observable, new Func2<AccountsParameter, Response<String>, AccountsParameter>() {
            @Override
            public AccountsParameter call(AccountsParameter accountsParameter, Response<String> stringResponse) {
                String response = String.valueOf(stringResponse.body());
                ErrorModel errorModel = new GsonBuilder().create().fromJson(response, ErrorModel.class);
                if (errorModel.getError() == null) {
                    TokenModel model = new GsonBuilder().create().fromJson(response, TokenModel.class);
                    accountsParameter.setTokenModel(model);
                } else {
                    accountsParameter.setErrorModel(errorModel);
                    try {
                        GoogleAuthUtil.clearToken(getApplicationContext(), params.get(Login.ACCESS_TOKEN));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return accountsParameter;
            }
        });
    }

    public Observable<AccountsParameter> getObservableAccountsInfo(AccountsParameter accountsParameter) {
        TokenModel tokenModel = accountsParameter.getTokenModel();
        String authKey = tokenModel.getTokenType() + " " + tokenModel.getAccessToken();
        Bundle bundle = new Bundle();
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        AccountsService accountService = new AccountsService(bundle);
        Observable<Response<String>> observable = accountService.getApi()
                .getInfo(AuthUtil.generateParams(getApplicationContext(), new HashMap<String, String>()));
        return Observable.zip(Observable.just(accountsParameter), observable, new Func2<AccountsParameter, Response<String>, AccountsParameter>() {
            @Override
            public AccountsParameter call(AccountsParameter accountsParameter, Response<String> stringResponse) {
                String response = String.valueOf(stringResponse.body());
                ErrorModel errorModel = new GsonBuilder().create().fromJson(response, ErrorModel.class);
                if (errorModel.getError() == null) {
                    InfoModel infoModel = new GsonBuilder().create().fromJson(response, InfoModel.class);
                    accountsParameter.setInfoModel(infoModel);
                } else {
                    accountsParameter.setErrorModel(errorModel);
                }
                return accountsParameter;
            }
        });
    }

    public Observable<AccountsParameter> getObservableMakeLogin(AccountsParameter accountsParameter) {
        Map<String, String> params = new HashMap<>();
        params = AuthUtil.generateParams(getApplicationContext(), params);
        params.put(Login.UUID_KEY, accountsParameter.getUUID());
        params.put(Login.USER_ID, String.valueOf(accountsParameter.getInfoModel().getUserId()));
        params = MapNulRemover.removeNull(params);
        TokenModel tokenModel = accountsParameter.getTokenModel();
        String authKey = tokenModel.getTokenType() + " " + SessionHandler.getAccessToken();
        Bundle bundle = new Bundle();
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);
        AccountsService accountService = new AccountsService(bundle);
        Observable<Response<TkpdResponse>> observable = accountService.getApi()
                .makeLogin(params);
        return Observable.zip(Observable.just(accountsParameter), observable, new Func2<AccountsParameter, Response<TkpdResponse>, AccountsParameter>() {
            @Override
            public AccountsParameter call(AccountsParameter accountsParameter, Response<TkpdResponse> responseData) {
                if (responseData.isSuccessful()) {
                    TkpdResponse response = responseData.body();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.getStringData());
                    } catch (JSONException je) {
                        Log.e(TAG, je.getLocalizedMessage());
                    }
                    if (!response.isError()) {
                        SecurityModel securityModel = new GsonBuilder().create()
                                .fromJson(jsonObject.toString(), SecurityModel.class);
                        if (securityModel.getIs_login().equals(false) || securityModel.getIs_login().equals("false")) {
                            //[START] Save Security Question
                            accountsParameter.setSecurityModel(securityModel);
                            accountsParameter.setMoveSecurity(true);
                            accountsParameter.setActivationResent(false);
                        } else {
                            AccountsModel accountsModel = new GsonBuilder()
                                    .create().fromJson(jsonObject.toString(), AccountsModel.class);
                            accountsParameter.setMoveSecurity(false);
                            accountsParameter.setActivationResent(false);
                            accountsParameter.setAccountsModel(accountsModel);
                        }
                    } else {
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setErrorDescription(response.getErrorMessages().toString());
                        accountsParameter.setErrorModel(errorModel);
                    }

                } else {
                    throw new RuntimeException(String.valueOf(responseData.code()));
                }
                return accountsParameter;
            }
        });
    }

    private void setLoginSession(AccountsModel accountsModel) {
        sessionHandler.setLoginSession(Boolean.parseBoolean(accountsModel.getIsLogin()),
                accountsModel.getUserId() + "",
                accountsModel.getFullName(), accountsModel.getShopId() + "",
                accountsModel.getMsisdnIsVerifiedBoolean());
    }

    private void sendAuthenticateGTMEvent(@NonNull Object modelObject) {

        Authenticated authEvent = new Authenticated();

        if (modelObject instanceof LoginEmailModel) {
            authEvent.setUserFullName(((LoginEmailModel) modelObject).getFullName());
            authEvent.setUserID(((LoginEmailModel) modelObject).getUserID());
            authEvent.setUserMSISNVer(((LoginEmailModel) modelObject).getMsisdnIsVerified());
            authEvent.setShopID(((LoginEmailModel) modelObject).getShopId());
            authEvent.setUserSeller(((LoginEmailModel) modelObject).getShopId() == 0 ? 0 : 1);

        } else if (modelObject instanceof LoginThirdModel) {
            authEvent.setUserFullName(((LoginThirdModel) modelObject).getFullName());
            authEvent.setUserID(((LoginThirdModel) modelObject).getUserID());
            authEvent.setUserMSISNVer(((LoginThirdModel) modelObject).getMsisdnIsVerified());
            authEvent.setShopID(((LoginThirdModel) modelObject).getShopId());
            authEvent.setUserSeller(((LoginThirdModel) modelObject).getShopId().equals("0") ? 0 : 1);

        } else {

        }


        if (getApplicationContext() != null) {
            Nishikino.init(getApplicationContext()).startAnalytics()
                    .eventAuthenticate(authEvent)
                    .sendScreen(Authenticated.KEY_CD_NAME);
        }

    }
}
