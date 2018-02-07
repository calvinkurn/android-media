package com.tokopedia.session.session.interactor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.session.model.LoginProviderModel;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by stevenfredian on 6/21/16.]
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class RegisterInteractorImpl implements RegisterInteractor {
    CompositeSubscription compositeSubscription;
    AccountsService accountsService;

    public static RegisterInteractor createInstance() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        RegisterInteractorImpl facade = new RegisterInteractorImpl();
        facade.compositeSubscription = new CompositeSubscription();
        facade.accountsService = new AccountsService(bundle);
        return facade;
    }

    @Override
    public void downloadProvider(final Context context, final RegisterInteractor.DiscoverLoginListener listener) {
        if (compositeSubscription.isUnsubscribed()) {
            this.compositeSubscription = new CompositeSubscription();
        }

        Observable<Response<TkpdResponse>> observable = accountsService.getApi()
                .discoverRegister();

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Register Provider", e.toString());
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
                            listener.onError(context.getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(context.getString(R.string.default_request_error_timeout));
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(context.getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(context.getString(R.string.default_request_error_bad_request));
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(context.getString(R.string.default_request_error_forbidden_auth));
                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void validateEmail(final Context context, Map<String, String> params, final ValidateEmailListener listener) {
        if (compositeSubscription.isUnsubscribed()) {
            this.compositeSubscription = new CompositeSubscription();
        }
        Observable<Response<TkpdResponse>> observable = accountsService.getApi()
                .validateEmail(MapNulRemover.removeNull(MapNulRemover.removeNull(params)));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("validating email", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        boolean isActive = "1".equals(result.optString("email_status"));
                        listener.onSuccess(isActive);
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(context.getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(context.getString(R.string.default_request_error_timeout));
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(context.getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(context.getString(R.string.default_request_error_bad_request));
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(context.getString(R.string.default_request_error_forbidden_auth));
                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
