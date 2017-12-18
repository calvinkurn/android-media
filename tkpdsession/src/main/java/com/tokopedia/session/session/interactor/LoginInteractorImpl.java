package com.tokopedia.session.session.interactor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.session.session.presenter.LoginImpl;
import com.tokopedia.session.session.presenter.OldRegisterInitialPresenterImpl;

import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by stevenfredian on 6/21/16.
 */
public class LoginInteractorImpl implements LoginInteractor {

    LoginImpl presenter;
    OldRegisterInitialPresenterImpl presenterNew;
    CompositeSubscription compositeSubscription;
    AccountsService accountsService;

    public static LoginInteractor createInstance(LoginImpl login) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        LoginInteractorImpl facade = new LoginInteractorImpl();
        facade.presenter = login;
        facade.compositeSubscription = new CompositeSubscription();
        facade.accountsService = new AccountsService(bundle);
        return facade;
    }

    @Override
    public void downloadProvider(Context context, final DiscoverLoginListener listener) {
        if (compositeSubscription.isUnsubscribed()) {
            this.compositeSubscription = new CompositeSubscription();
        }

        Observable<Response<TkpdResponse>> observable = accountsService.getApi()
                .discoverLogin(RequestParams.EMPTY.getParameters());

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Login Provider", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        LoginProviderModel loginProviderModel = new GsonBuilder().create()
                                .fromJson(result.toString(), LoginProviderModel.class);
                        listener.onSuccess(loginProviderModel);
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {

                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }

    public static LoginInteractor createInstance(OldRegisterInitialPresenterImpl presenter) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        LoginInteractorImpl facade = new LoginInteractorImpl();
        facade.presenterNew = presenter;
        facade.compositeSubscription = new CompositeSubscription();
        facade.accountsService = new AccountsService(bundle);
        return facade;
    }
}
