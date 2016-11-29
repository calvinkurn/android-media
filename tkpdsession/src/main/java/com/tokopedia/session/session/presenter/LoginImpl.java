package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnNewPermissionsListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.nishikino.Nishikino;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.presenter.Login;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.session.session.interactor.LoginInteractor;
import com.tokopedia.session.session.interactor.LoginInteractorImpl;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.session.session.model.LoginEmailModel;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.session.session.model.LoginModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by m.normansyah on 04/11/2015.
 * Modified by m.normansyah on 17-11-2015, change to GSON
 * Modified by m.normansyah on 21-11-2015, move all download or upload to the internet
 */
public class LoginImpl implements Login {
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

    private SimpleFacebook simpleFacebook;

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
    public void sendGTMScreen(Context context) {
        Nishikino.init(context).startAnalytics().sendScreen(AppScreen.SCREEN_LOGIN);
    }

    @Override
    public void sendGTMRegisterThrougLogin() {
        Nishikino.init(mContext).startAnalytics()
                .sendButtonClick(
                        AppEventTracking.Event.REGISTER_LOGIN,
                        AppEventTracking.Category.LOGIN,
                        AppEventTracking.Action.REGISTER,
                        AppEventTracking.EventLabel.REGISTER);
    }
    @Override
    public void sendCTAAction() {
        Nishikino.init(mContext).startAnalytics()
                .sendButtonClick(
                        AppEventTracking.Event.LOGIN_CLICK,
                        AppEventTracking.Category.LOGIN,
                        AppEventTracking.Action.CLICK,
                        AppEventTracking.EventLabel.CTA);
    }
    @Override
    public void sendGTMLoginError(String label) {
        Nishikino.init(mContext).startAnalytics()
                .sendButtonClick(
                        AppEventTracking.Event.LOGIN_ERROR,
                        AppEventTracking.Category.LOGIN,
                        AppEventTracking.Action.LOGIN_ERROR,
                        label);
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
                getToken(action,(LoginViewModel)data[0]);
                break;
            case LoginModel.GoogleType:
                LoginGoogleModel loginGoogleModel = (LoginGoogleModel) data[0];
                loginGoogleModel.setUuid(getUUID());

                bundle = new Bundle();
                bundle.putParcelable(DownloadService.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(loginGoogleModel));
                bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);

                ((SessionView) mContext).sendDataFromInternet(DownloadService.LOGIN_GOOGLE, bundle);
                break;
            case LoginModel.FacebookType:
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
    public void loginFacebook() {
        simpleFacebook = simpleFacebook.getInstance();
        Log.d("steven isLogin?", String.valueOf(simpleFacebook.isLogin()));
        if (simpleFacebook.isLogin()) {
            simpleFacebook.logout(new OnLogoutListener() {
                @Override
                public void onLogout() {
                    Log.d("steven logout", "you are logged out");
                }
            });
        }
        Permission[] permissions = new Permission[]{
                Permission.EMAIL,
        };

        OnNewPermissionsListener onNewPermissionsListener = new OnNewPermissionsListener() {
            @Override
            public void onSuccess(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                Log.d("steven permissions succ", acceptedPermissions.toString());
                askToLogin();
            }

            @Override
            public void onCancel() {
                Log.d("steven permissions canc", "you are out");
                loginView.showProgress(false);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.d("steven permissions excn", throwable.toString());
                loginView.showError(mContext.getString(R.string.msg_network_error));
            }

            @Override
            public void onFail(String reason) {
                Log.d("steven permissions fail", reason);
            }
        };

        simpleFacebook.requestNewPermissions(permissions, onNewPermissionsListener);
    }

    private void askToLogin() {
        simpleFacebook.login(new OnLoginListener() {
            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                Profile.Properties properties = new Profile.Properties.Builder()
                        .add(Profile.Properties.ID)
                        .add(Profile.Properties.FIRST_NAME)
                        .add(Profile.Properties.GENDER)
                        .add(Profile.Properties.EMAIL)
                        .build();
                simpleFacebook.getProfile(properties, new OnProfileListener() {
                    @Override
                    public void onComplete(Profile response) {
                        Log.e(TAG, messageTAG + " start login to facebook !!");
                        super.onComplete(response);
                        LoginFacebookViewModel loginFacebookViewModel = new LoginFacebookViewModel();
                        loginFacebookViewModel.setFullName(response.getFirstName());// 10
                        loginFacebookViewModel.setGender(response.getGender());// 7
                        setBirthday(loginFacebookViewModel, response.getBirthday());// 2
                        loginFacebookViewModel.setFbToken(simpleFacebook.getAccessToken().getToken());// 6
                        loginFacebookViewModel.setFbId(response.getId());// 8
                        loginFacebookViewModel.setEmail(response.getEmail());// 5
                        loginFacebookViewModel.setEducation(response.getEducation() + "");// 4
                        loginFacebookViewModel.setInterest(response.getRelationshipStatus());// 9
                        loginFacebookViewModel.setWork(response.getWork() + "");

                        sendDataFromInternet(LoginModel.FacebookType, loginFacebookViewModel);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        super.onException(throwable);
                        Log.e(TAG, messageTAG + " login facebook : " + throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        super.onFail(reason);
                        Log.e(TAG, messageTAG + " login facebook : " + reason);
                    }
                });
            }

            @Override
            public void onCancel() {
                loginView.showProgress(false);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e(TAG, messageTAG + " login facebook : " + throwable.getLocalizedMessage());
                loginView.showError(mContext.getString(R.string.msg_network_error));
            }

            @Override
            public void onFail(String s) {
                Log.e(TAG, messageTAG + " login facebook : " + s);
            }
        });
    }


    private void setBirthday(LoginFacebookViewModel loginFacebookViewModel, String birthday) {
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(birthday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date != null) loginFacebookViewModel.setBirthday(outputFormat.format(date));
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
                loginView.showProgress(false);
                // if need to move to security
                if (data.getBoolean(DownloadService.LOGIN_MOVE_SECURITY, false)) {// move to security
                    SecurityModel loginSecurityModel = data.getParcelable(DownloadService.LOGIN_SECURITY_QUESTION_DATA);
                    loginView.moveToFragmentSecurityQuestion(
                            loginSecurityModel.getSecurity().getUser_check_security_1(),
                            loginSecurityModel.getSecurity().getUser_check_security_2(),
                            loginSecurityModel.getUser_id());
                } else if (sessionHandler.isV4Login()) {// go back to home
                    loginView.destroyActivity();
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

        ((SessionView) mContext).sendDataFromInternet(DownloadService.LOGIN_ACCOUNTS_TOKEN, bundle);
    }
}
