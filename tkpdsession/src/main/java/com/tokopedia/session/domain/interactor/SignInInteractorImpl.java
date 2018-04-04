package com.tokopedia.session.domain.interactor;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.GsonBuilder;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.session.model.AccountsModel;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.model.ErrorModel;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.session.model.TokenModel;
import com.tokopedia.core.util.SessionHandler;

import org.parceler.Parcels;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by stevenfredian on 1/19/17.
 */
@Deprecated
public class SignInInteractorImpl implements SignInInteractor{

    private SessionHandler sessionHandler;
    private Context context;

    public static SignInInteractorImpl createInstance(Context context) {
        SignInInteractorImpl interactor = new SignInInteractorImpl();
        interactor.sessionHandler = new SessionHandler(context);
        interactor.context = context;
        return interactor;
    }

    public void handleAccounts(final AccountsParameter data, final SignInListener listener) {
        Observable.just(data)
                .flatMap(new Func1<AccountsParameter, Observable<AccountsParameter>>() {
                    @Override
                    public Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        return getObservableAccountsToken(accountsParameter);
                    }
                })

                .flatMap(new Func1<AccountsParameter, Observable<AccountsParameter>>() {
                    @Override
                    public Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if(accountsParameter.getErrorModel()==null) {
                            TokenModel tokenModel = accountsParameter.getTokenModel();
                            sessionHandler.setToken(tokenModel.getAccessToken(),
                                    tokenModel.getTokenType()
                            );
                        }
                        return Observable.just(accountsParameter);
                    }
                })

                .flatMap(new Func1<AccountsParameter, Observable<AccountsParameter>>() {
                    @Override
                    public Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if(accountsParameter.getErrorModel()==null) {
                            return getObservableAccountsInfo(accountsParameter);
                        }else {
                            return Observable.just(accountsParameter);
                        }
                    }
                })

                .flatMap(new Func1<AccountsParameter, Observable<AccountsParameter>>() {
                    @Override
                    public Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if(accountsParameter.getInfoModel() != null
                                && accountsParameter.getInfoModel().isCreatedPassword()
                                && accountsParameter.getErrorModel()==null) {
                            return getObservableMakeLogin(accountsParameter);
                        }
                        else {
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
                        Log.d("steven flatmap", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        String messageError;
                        Log.d("steven flatmap", "onError " + e.getMessage());
                        if(e instanceof SocketTimeoutException){
                            messageError = "Terjadi kesalahan koneksi, silahkan coba lagi";
                        }else{
                            messageError = "Silahkan coba lagi";
                        }
                        listener.onError(messageError);
                    }

                    @Override
                    public void onNext(AccountsParameter accountsParameter) {
                        Log.d("steven flatmap", "onNext");
                        // make login
                        if(accountsParameter.getInfoModel()!=null && accountsParameter.getInfoModel().isCreatedPassword()) {
                            SecurityModel securityModel = accountsParameter.getSecurityModel();
                            if (securityModel != null) {
                                sessionHandler.setTempPhoneNumber(accountsParameter.getInfoModel().getPhone());
                                sessionHandler.setTempLoginName(accountsParameter.getInfoModel().getName());
                                sessionHandler.setTempLoginSession(Integer.toString(securityModel.getUser_id()));
                                listener.moveToSecurityQuestion(securityModel);
                            } else {
                                Log.d("steven", "berhasil make login");
                                AccountsModel accountsModel = accountsParameter.getAccountsModel();
                                setLoginSession(accountsModel);
                                SessionHandler.setPhoneNumber(accountsParameter.getInfoModel().getPhone());
                                SessionHandler.setGoldMerchant(context, accountsModel.getShopIsGold());
                                listener.onSuccess(accountsModel);

                            }
                        }
                        //showing error
                        else if(accountsParameter.getErrorModel()!=null){
                            listener.onError(accountsParameter.getErrorModel().getErrorDescription());
                        }
                        // need create password
                        else{
                            sessionHandler.setTempLoginSession(String.valueOf(accountsParameter.getInfoModel().getUserId()));
                            listener.moveToCreatePassword(accountsParameter.getInfoModel());
                        }
                    }
                });
    }

    private Observable<AccountsParameter> getObservableAccountsToken(AccountsParameter accountsParameter) {
        Bundle bundle = new Bundle();
        Map<String, String> params = new HashMap<>();
        Parcelable parcelable = accountsParameter.getParcelable();

        params.put(GRANT_TYPE, accountsParameter.getGrantType());

        switch (accountsParameter.getGrantType()){
            case GRANT_PASSWORD:
                params.put(USER_NAME, accountsParameter.getEmail());
                params.put(PASSWORD, accountsParameter.getPassword());
                if(accountsParameter.getPasswordType()!=null
                        && accountsParameter.getPasswordType().equals(ACTIVATION_CODE)){
                    params.put(USER_NAME, " ");
                    params.put(PASSWORD_TYPE, accountsParameter.getPasswordType());
                    params.put(ATTEMPT, "1");
                }
                break;
            case GRANT_SDK:
                params.put(SOCIAL_TYPE, String.valueOf(accountsParameter.getSocialType()));
                if (parcelable instanceof GoogleSignInAccount) {
                    GoogleSignInAccount account = (GoogleSignInAccount) parcelable;
                    params.put(SOCIAL_ID, account.getId());
                    params.put(EMAIL_ACCOUNTS, account.getEmail());
                    params.put(PICTURE_ACCOUNTS, String.valueOf(account.getPhotoUrl()));
                    params.put(FULL_NAME, account.getDisplayName());
                } else if(Parcels.unwrap(parcelable) instanceof LoginFacebookViewModel){
                    LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(parcelable);
                    params.put(SOCIAL_ID, loginFacebookViewModel.getFbId());
                    params.put(EMAIL_ACCOUNTS, loginFacebookViewModel.getEmail());
                    params.put(FULL_NAME, loginFacebookViewModel.getFullName());
                    params.put(BIRTHDATE, loginFacebookViewModel.getBirthday());
                    params.put(GENDER_ACCOUNTS, loginFacebookViewModel.getGender());
                }
                break;
            case GRANT_WEBVIEW:
                params.put(CODE, accountsParameter.getCode());
                params.put(REDIRECT_URI, accountsParameter.getRedirectUri());
                break;
            default:
                throw new RuntimeException("Invalid Observable to get Token");
        }

        AccountsService accountService = new AccountsService(bundle);
        Observable<Response<String>> observable = accountService.getApi()
                .getTokenOld(AuthUtil
                        .generateParams(context, params));
        return Observable.zip(Observable.just(accountsParameter), observable, new Func2<AccountsParameter, Response<String>, AccountsParameter>() {
            @Override
            public AccountsParameter call(AccountsParameter accountsParameter, Response<String> stringResponse) {
                String response = String.valueOf(stringResponse.body());
                ErrorModel errorModel = new GsonBuilder().create().fromJson(response, ErrorModel.class);
                if (errorModel.getError() == null) {
                    TokenModel model = new GsonBuilder().create().fromJson(response, TokenModel.class);
                    accountsParameter.setTokenModel(model);
                }else{
                    accountsParameter.setErrorModel(errorModel);
                }
                return accountsParameter;
            }
        });
    }

    private Observable<AccountsParameter> getObservableAccountsInfo(AccountsParameter accountsParameter) {
        TokenModel tokenModel = accountsParameter.getTokenModel();
        String authKey = String.format("%s %s", tokenModel.getTokenType(), tokenModel.getAccessToken());
        Bundle bundle = new Bundle();
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        AccountsService accountService = new AccountsService(bundle);
        Observable<Response<String>> observable = accountService.getApi()
                .getInfo(AuthUtil.generateParams(context, new HashMap<String, String>()));
        return Observable.zip(Observable.just(accountsParameter), observable, new Func2<AccountsParameter, Response<String>, AccountsParameter>() {
            @Override
            public AccountsParameter call(AccountsParameter accountsParameter, Response<String> stringResponse) {
                String response = String.valueOf(stringResponse.body());
                ErrorModel errorModel = new GsonBuilder().create().fromJson(response, ErrorModel.class);
                if(errorModel.getError()==null){
                    InfoModel infoModel = new GsonBuilder().create().fromJson(response, InfoModel.class);
                    accountsParameter.setInfoModel(infoModel);
                }else{
                    accountsParameter.setErrorModel(errorModel);
                }
                return accountsParameter;
            }
        });
    }

    private Observable<AccountsParameter> getObservableMakeLogin(AccountsParameter accountsParameter) {
        Map<String, String> params = new HashMap<>();
        params = AuthUtil.generateParams(context, params);
        params.put(UUID_KEY, accountsParameter.getUUID());
        params.put(USER_ID, String.valueOf(accountsParameter.getInfoModel().getUserId()));
        params = MapNulRemover.removeNull(params);
        TokenModel tokenModel = accountsParameter.getTokenModel();
        String authKey = String.format("%s %s", tokenModel.getTokenType(), tokenModel.getAccessToken());
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
                    if (!response.isError()) {
                        SecurityModel securityModel = response.convertDataObj(SecurityModel.class);
                        if (securityModel.getIs_login().equals("false")) {
                            accountsParameter.setSecurityModel(securityModel);
                            accountsParameter.setMoveSecurity(true);
                            accountsParameter.setActivationResent(false);
                        } else {
                            AccountsModel accountsModel = response.convertToObj(AccountsModel.class);
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


    private void setLoginSession(AccountsModel accountsModel){
        sessionHandler.setLoginSession(Boolean.parseBoolean(accountsModel.getIsLogin()),
                String.valueOf(accountsModel.getUserId()),
                accountsModel.getFullName(), String.valueOf(accountsModel.getShopId()),
                accountsModel.getMsisdnIsVerifiedBoolean(), "");
    }
}
