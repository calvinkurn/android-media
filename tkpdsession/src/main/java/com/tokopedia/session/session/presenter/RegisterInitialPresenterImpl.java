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
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.session.session.fragment.RegisterInitialFragment;
import com.tokopedia.session.session.interactor.LoginInteractor;
import com.tokopedia.session.session.interactor.LoginInteractorImpl;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.session.session.model.LoginModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.model.RegisterViewModel;

import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.tokopedia.core.session.presenter.Login.LOGIN_UUID_KEY;
import static com.tokopedia.core.session.presenter.Login.PROVIDER_LIST;

/**
 * Created by stevenfredian on 10/18/16.
 */

public class RegisterInitialPresenterImpl extends RegisterInitialPresenter {

    RegisterInitialView view;
    SimpleFacebook simpleFacebook;
    LocalCacheHandler loginUuid;
    LocalCacheHandler providerListCache;
    String PROVIDER_CACHE_KEY = "provider_cache";
    String messageTAG = "Register Init";
    LoginInteractor interactor;
    String UUID_KEY = "uuid";
    String DEFAULT_UUID_VALUE = "";

    public RegisterInitialPresenterImpl(RegisterInitialFragment view) {
        super(view);
        this.view = view;
        interactor = LoginInteractorImpl.createInstance(this);
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
        if(view.checkHasNoProvider()){
            view.addProgressBar();
            List<LoginProviderModel.ProvidersBean> providerList= loadProvider();
            if(providerList == null || providerListCache.isExpired()){
                downloadProviderLogin(context);
            }else {
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

    private List<LoginProviderModel.ProvidersBean> loadProvider(){
        String cache = providerListCache.getString(PROVIDER_CACHE_KEY);
        Type type = new TypeToken<List<LoginProviderModel.ProvidersBean>>(){}.getType();
        return new GsonBuilder().create().fromJson(cache, type);
    }

    @Override
    public void saveProvider(List<LoginProviderModel.ProvidersBean> listProvider) {
        String cache = new GsonBuilder().create().toJson(loadProvider());
        String listProviderString = new GsonBuilder().create().toJson(listProvider);
        if(!cache.equals(listProviderString)){
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
        switch (type){
            case DownloadService.LOGIN_ACCOUNTS_INFO:
                data.putString(UUID_KEY, getUUID());
                InfoModel infoModel = data.getParcelable(DownloadServiceConstant.INFO_BUNDLE);
                Parcelable parcelable = data.getParcelable(DownloadServiceConstant.EXTRA_TYPE);

                if (infoModel.isCreatedPassword()) {
                    ((SessionView) context).sendDataFromInternet(DownloadService.MAKE_LOGIN, data);
                } else {
                    CreatePasswordModel createPasswordModel = new CreatePasswordModel();
                    createPasswordModel = setModelFromParcelable(createPasswordModel,parcelable,infoModel);
                    data.putBoolean(DownloadServiceConstant.LOGIN_MOVE_REGISTER_THIRD, true);
                    data.putParcelable(DownloadServiceConstant.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(createPasswordModel));
                    ((SessionView) context).moveToRegisterPassPhone(createPasswordModel, infoModel.getCreatePasswordList(), data);
                }
                break;

            case DownloadServiceConstant.MAKE_LOGIN:
                view.showProgress(false);
                view.finishActivity();
                break;
        }
    }

    @Override
    public void unSubscribeFacade() {
        interactor.unSubscribe();
    }

    private CreatePasswordModel setModelFromParcelable(CreatePasswordModel createPasswordModel, Parcelable parcelable, InfoModel infoModel) {
        if (Parcels.unwrap(parcelable) instanceof LoginGoogleModel) {
            LoginGoogleModel loginGoogleModel = Parcels.unwrap(parcelable);
            if (loginGoogleModel.getFullName() != null) {
                createPasswordModel.setFullName(loginGoogleModel.getFullName());
            }
            if (loginGoogleModel.getGender().contains("male")) {
                createPasswordModel.setGender(RegisterViewModel.GENDER_MALE + "");
            } else {
                createPasswordModel.setGender(RegisterViewModel.GENDER_FEMALE + "");
            }
            if (loginGoogleModel.getBirthday() != null) {
                createPasswordModel.setDateText(loginGoogleModel.getBirthday());
            }
            if (loginGoogleModel.getEmail() != null) {
                createPasswordModel.setEmail(loginGoogleModel.getEmail());
            }
        }else if(Parcels.unwrap(parcelable) instanceof LoginFacebookViewModel){
            LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(parcelable);
            if (loginFacebookViewModel.getFullName() != null) {
                createPasswordModel.setFullName(loginFacebookViewModel.getFullName());
            }
            if (loginFacebookViewModel.getGender().contains("male")) {
                createPasswordModel.setGender(RegisterViewModel.GENDER_MALE + "");
            } else {
                createPasswordModel.setGender(RegisterViewModel.GENDER_FEMALE + "");
            }
            if (loginFacebookViewModel.getBirthday() != null) {
                createPasswordModel.setDateText(loginFacebookViewModel.getBirthday());
            }
            if (loginFacebookViewModel.getEmail() != null) {
                createPasswordModel.setEmail(loginFacebookViewModel.getEmail());
            }
        }else{
            createPasswordModel.setFullName(infoModel.getName());
            createPasswordModel.setEmail(infoModel.getEmail());
        }
        return createPasswordModel;
    }

    @Override
    public void downloadProviderLogin(Context context) {
        interactor.downloadProvider(context, new LoginInteractor.DiscoverLoginListener() {
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
    public void loginFacebook(final Context context) {
        simpleFacebook = simpleFacebook.getInstance();
        Log.d("steven isLogin?", String.valueOf(simpleFacebook.isLogin()));
        if(simpleFacebook.isLogin()){
            simpleFacebook.logout(new OnLogoutListener() {
                @Override
                public void onLogout() {
                    Log.d("steven logout", "you are logged out");
                }
            });
        }

        Permission[] permissions = new Permission[] {
                Permission.EMAIL,
        };

        OnNewPermissionsListener onNewPermissionsListener = new OnNewPermissionsListener() {
            @Override
            public void onSuccess(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                Log.d("steven permissions succ", acceptedPermissions.toString());
                askToLogin(context);
            }

            @Override
            public void onCancel() {
                Log.d("steven permissions canc", "you are out");
                view.showProgress(false);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.d("steven permissions excn", throwable.toString());
                view.showError(context.getString(R.string.msg_network_error));
                view.showProgress(false);
            }

            @Override
            public void onFail(String reason) {
                Log.d("steven permissions fail", reason);
                view.showProgress(false);
            }
        };

        simpleFacebook.requestNewPermissions(permissions, onNewPermissionsListener);
    }


    private void askToLogin(final Context context) {
        simpleFacebook.login(new OnLoginListener() {
            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                Profile.Properties properties = new Profile.Properties.Builder()
                        .add(Profile.Properties.ID)
                        .add(Profile.Properties.FIRST_NAME)
                        .add(Profile.Properties.GENDER)
                        .add(Profile.Properties.EMAIL)
                        .add(Profile.Properties.WORK)
                        .add(Profile.Properties.BIRTHDAY)
                        .add(Profile.Properties.NAME)
                        .build();
                simpleFacebook.getProfile(properties, new OnProfileListener() {
                    @Override
                    public void onComplete(Profile response) {
                        Log.e(messageTAG ,  " start login to facebook !!");
                        super.onComplete(response);
                        LoginFacebookViewModel loginFacebookViewModel = new LoginFacebookViewModel();
                        loginFacebookViewModel.setFullName(response.getName());// 10
                        loginFacebookViewModel.setGender(response.getGender());// 7
                        setBirthday(loginFacebookViewModel,response.getBirthday());// 2
                        loginFacebookViewModel.setFbToken(simpleFacebook.getAccessToken().getToken());// 6
                        loginFacebookViewModel.setFbId(response.getId());// 8
                        loginFacebookViewModel.setEmail(response.getEmail());// 5
                        loginFacebookViewModel.setEducation(response.getEducation() + "");// 4
                        loginFacebookViewModel.setInterest(response.getRelationshipStatus());// 9
                        loginFacebookViewModel.setWork(response.getWork() + "");
                        Log.e(messageTAG ,  " end login Facebook : " + loginFacebookViewModel);

                        if(context!=null&&context instanceof SessionView){
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(DownloadService.LOGIN_FACEBOOK_MODEL_KEY, Parcels.wrap(loginFacebookViewModel));
                            bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);


                            ((SessionView)context).sendDataFromInternet(DownloadService.REGISTER_FACEBOOK
                                    , bundle);
                        }
                        // dismiss progress
                        view.showProgress(false);

                    }

                    @Override
                    public void onException(Throwable throwable) {
                        super.onException(throwable);
                        Log.e(messageTAG , " login facebook : "+throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        super.onFail(reason);
                        Log.e(messageTAG ,  " login facebook : " + reason);
                    }
                });
            }

            @Override
            public void onCancel() {
                view.showProgress(false);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e(messageTAG , " login facebook "+ throwable.getLocalizedMessage());
                view.showError(context.getString(R.string.msg_network_error));
                view.showProgress(false);
            }

            @Override
            public void onFail(String s) {
                Log.e(messageTAG , " login facebook "+ s);
//                Toast.makeText(context, context.getString(R.string.message_verification_timeout), Toast.LENGTH_LONG).show();
                view.showProgress(false);
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
        if(date!=null) loginFacebookViewModel.setBirthday(outputFormat.format(date));
    }


    @Override
    public void startLoginWithGoogle(Context context,String type, LoginGoogleModel loginGoogleModel) {
        if(type != null && type.equals(LoginModel.GoogleType) && loginGoogleModel != null){
            // dismiss progress
            view.showProgress(false);
            RegisterViewModel registerViewModel = new RegisterViewModel();

            // update data and UI
//            if(registerViewModel!=null){
            if(loginGoogleModel.getFullName()!=null){
//                    registerView.updateData(RegisterView.NAME, loginGoogleModel.getFullName());
                registerViewModel.setmName(loginGoogleModel.getFullName());
            }
            if(loginGoogleModel.getGender().contains("male")){
//                    registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_MALE);
                registerViewModel.setmGender(RegisterViewModel.GENDER_MALE);
            }else{
//                    registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_FEMALE);
                registerViewModel.setmGender(RegisterViewModel.GENDER_FEMALE);
            }
            if(loginGoogleModel.getBirthday()!=null){
                Log.d(messageTAG, " need to verify birthday : "+loginGoogleModel.getBirthday());
                registerViewModel.setDateText(loginGoogleModel.getBirthday());
            }
            if(loginGoogleModel.getEmail()!=null){
                registerViewModel.setmEmail(loginGoogleModel.getEmail());
            }
            loginGoogleModel.setUuid(getUUID());

            Bundle bundle = new Bundle();
            bundle.putParcelable(DownloadService.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(loginGoogleModel));
            bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);

            ((SessionView)context).sendDataFromInternet(DownloadService.REGISTER_GOOGLE, bundle);
        }
    }

    @Override
    public void loginWebView(Context context, Bundle data) {
        Bundle bundle = data;
        bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);

        ((SessionView)context).sendDataFromInternet(DownloadService.LOGIN_WEBVIEW, bundle);
    }

    public String getUUID() {
        return loginUuid.getString(UUID_KEY, DEFAULT_UUID_VALUE);
    }
}
