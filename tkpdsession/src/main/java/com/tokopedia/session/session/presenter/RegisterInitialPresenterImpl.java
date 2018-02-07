package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
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
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.core.session.model.FacebookModel;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.FacebookContainer;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.fragment.RegisterInitialFragment;
import com.tokopedia.session.session.interactor.RegisterInteractor;
import com.tokopedia.session.session.interactor.RegisterInteractorImpl;
import com.tokopedia.session.session.model.LoginModel;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.util.List;

import static com.tokopedia.session.session.presenter.Login.LOGIN_UUID_KEY;
import static com.tokopedia.session.session.presenter.Login.PROVIDER_LIST;

/**
 * Created by stevenfredian on 10/18/16.
 */

public class RegisterInitialPresenterImpl extends RegisterInitialPresenter {

    RegisterInitialView view;
    LocalCacheHandler loginUuid;
    LocalCacheHandler providerListCache;
    String PROVIDER_CACHE_KEY = "provider_cache_register";
    String messageTAG = "Register Init";
    RegisterInteractor interactor;
    String UUID_KEY = "uuid";
    String DEFAULT_UUID_VALUE = "";

    public RegisterInitialPresenterImpl(RegisterInitialFragment view) {
        super(view);
        this.view = view;
        interactor = RegisterInteractorImpl.createInstance();
        loginUuid = new LocalCacheHandler(view.getActivity(), LOGIN_UUID_KEY);
        providerListCache = new LocalCacheHandler(view.getActivity(), PROVIDER_LIST);
    }

    @Override
    public String getMessageTAG() {
        return null;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return null;
    }

    @Override
    public void initData(Context context) {
        if (view.checkHasNoProvider()) {
            view.addProgressBar();
            List<LoginProviderModel.ProvidersBean> providerList = loadProvider();
            if (providerList == null || providerListCache.isExpired()) {
                downloadProviderLogin(context);
            } else {
                view.removeProgressBar();
                view.showProvider(providerList);
            }
        }
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {

    }

    private List<LoginProviderModel.ProvidersBean> loadProvider() {
        String cache = providerListCache.getString(PROVIDER_CACHE_KEY);
        Type type = new TypeToken<List<LoginProviderModel.ProvidersBean>>() {
        }.getType();
        return new GsonBuilder().create().fromJson(cache, type);
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

    @Override
    public void storeCacheGTM(String registerType, String name) {

    }

    @Override
    public void setData(Context context, int type, Bundle data) {
        switch (type) {
            case DownloadService.LOGIN_ACCOUNTS_INFO:
                data.putString(UUID_KEY, getUUID());
                InfoModel infoModel = data.getParcelable(DownloadServiceConstant.INFO_BUNDLE);
                Parcelable parcelable = data.getParcelable(DownloadServiceConstant.EXTRA_TYPE);

                if (infoModel.isCreatedPassword()) {
                    ((SessionView) context).sendDataFromInternet(DownloadService.MAKE_LOGIN, data);
                } else {
                    CreatePasswordModel createPasswordModel = new CreatePasswordModel();
                    createPasswordModel = setModelFromParcelable(createPasswordModel, parcelable, infoModel);
                    data.putBoolean(DownloadServiceConstant.LOGIN_MOVE_REGISTER_THIRD, true);
                    data.putParcelable(DownloadServiceConstant.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(createPasswordModel));
                    ((SessionView) context).moveToRegisterPassPhone(createPasswordModel, infoModel.getCreatePasswordList(), data);
                }
                break;

            case DownloadServiceConstant.MAKE_LOGIN:
                // if need to move to security
                if (data.getBoolean(DownloadService.LOGIN_MOVE_SECURITY, false)) {// move to security
                    SecurityModel loginSecurityModel = data.getParcelable(DownloadService.LOGIN_SECURITY_QUESTION_DATA);
                    view.moveToFragmentSecurityQuestion(
                            loginSecurityModel.getSecurity().getUser_check_security_1(),
                            loginSecurityModel.getSecurity().getUser_check_security_2(),
                            loginSecurityModel.getUser_id());
                } else if (SessionHandler.isV4Login(context)) {// go back to home
                    view.finishActivity();
                }
                break;
        }
    }

    @Override
    public void unSubscribeFacade() {
        interactor.unSubscribe();
    }

    @Override
    public void doFacebookLogin(final RegisterInitialFragment fragment, final CallbackManager callbackManager) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, FacebookContainer.readPermissions);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,birthday,email");

                if (loginResult.getAccessToken().getDeclinedPermissions().size() > 0) {
                    doFacebookLogin(fragment, callbackManager);

                } else {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    FacebookModel facebookModel =
                                            new GsonBuilder().create().fromJson(String.valueOf(object), FacebookModel.class);
                                    loginFacebook(fragment.getActivity(), facebookModel, loginResult.getAccessToken().getToken());
                                }
                            });

                    request.setParameters(parameters);
                    request.executeAsync();
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
                view.showError(fragment.getActivity().getString(R.string.msg_network_error));
            }
        });
    }

    private CreatePasswordModel setModelFromParcelable(CreatePasswordModel createPasswordModel, Parcelable parcelable, InfoModel infoModel) {
        createPasswordModel.setFullName(infoModel.getName());
        createPasswordModel.setEmail(infoModel.getEmail());
        if (infoModel.getPhone() != null)
            createPasswordModel.setMsisdn(infoModel.getPhone());
        return createPasswordModel;
    }

    @Override
    public void downloadProviderLogin(Context context) {
        interactor.downloadProvider(context, new RegisterInteractor.DiscoverLoginListener() {
            @Override
            public void onSuccess(LoginProviderModel result) {
                view.removeProgressBar();
                view.showProvider(result.getProviders());
            }

            @Override
            public void onError(String s) {
                view.onMessageError(DownloadService.DISCOVER_LOGIN, s);
            }

            @Override
            public void onTimeout() {
                view.onMessageError(DownloadService.DISCOVER_LOGIN, "");
            }

            @Override
            public void onThrowable(Throwable e) {
                view.onMessageError(DownloadService.DISCOVER_LOGIN, "");
            }
        });
    }


    @Override
    public void loginFacebook(final Context context, FacebookModel facebookModel, String token) {
        LoginFacebookViewModel loginFacebookViewModel = new LoginFacebookViewModel();
        if (facebookModel != null && !TextUtils.isEmpty(facebookModel.getName()))
            loginFacebookViewModel.setFullName(facebookModel.getName());
        if (facebookModel != null && !TextUtils.isEmpty(facebookModel.getGender()))
            loginFacebookViewModel.setGender(facebookModel.getGender());
        if (facebookModel != null && !TextUtils.isEmpty(facebookModel.getBirthdayConverted()))
            loginFacebookViewModel.setBirthday(facebookModel.getBirthdayConverted());
        if (!TextUtils.isEmpty(token))
            loginFacebookViewModel.setFbToken(token);
        if (facebookModel != null && !TextUtils.isEmpty(facebookModel.getId()))
            loginFacebookViewModel.setFbId(facebookModel.getId());
        if (facebookModel != null && !TextUtils.isEmpty(facebookModel.getEmail()))
            loginFacebookViewModel.setEmail(facebookModel.getEmail());

        if (context != null && context instanceof SessionView) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DownloadService.LOGIN_FACEBOOK_MODEL_KEY, Parcels.wrap(loginFacebookViewModel));
            bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);

            view.showProgress(true);
            ((SessionView) context).sendDataFromInternet(DownloadService.REGISTER_FACEBOOK
                    , bundle);
        }
    }

    @Override
    public void startLoginWithGoogle(Context context, String type, LoginGoogleModel loginGoogleModel) {
        if (type != null && type.equals(LoginModel.GoogleType) && loginGoogleModel != null) {
            RegisterViewModel registerViewModel = new RegisterViewModel();

            // update data and UI
//            if(registerViewModel!=null){
            if (loginGoogleModel.getFullName() != null) {
//                    registerView.updateData(RegisterView.NAME, loginGoogleModel.getFullName());
                registerViewModel.setmName(loginGoogleModel.getFullName());
            }
            if (loginGoogleModel.getGender() == null || loginGoogleModel.getGender().contains("male")) {
//                    registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_MALE);
                registerViewModel.setmGender(RegisterViewModel.GENDER_MALE);
            } else {
//                    registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_FEMALE);
                registerViewModel.setmGender(RegisterViewModel.GENDER_FEMALE);
            }
            if (loginGoogleModel.getBirthday() != null) {
                Log.d(messageTAG, " need to verify birthday : " + loginGoogleModel.getBirthday());
                registerViewModel.setDateText(loginGoogleModel.getBirthday());
            }
            if (loginGoogleModel.getEmail() != null) {
                registerViewModel.setmEmail(loginGoogleModel.getEmail());
            }
            loginGoogleModel.setUuid(getUUID());

            Bundle bundle = new Bundle();
            bundle.putParcelable(DownloadService.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(loginGoogleModel));
            bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);

            view.showProgress(true);
            ((SessionView) context).sendDataFromInternet(DownloadService.REGISTER_GOOGLE, bundle);
        }
    }

    @Override
    public void loginWebView(Context context, Bundle data) {
        Bundle bundle = data;
        bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);

        view.showProgress(true);
        ((SessionView) context).sendDataFromInternet(DownloadService.LOGIN_WEBVIEW, bundle);
    }

    public String getUUID() {
        return loginUuid.getString(UUID_KEY, DEFAULT_UUID_VALUE);
    }
}
