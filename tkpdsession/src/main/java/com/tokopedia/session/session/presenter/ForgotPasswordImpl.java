package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.session.forgotpassword.api.ForgotPasswordApi;
import com.tokopedia.core.session.model.ForgotPasswordModel;
import com.tokopedia.session.session.model.ForgotPasswordViewModel;
import com.tokopedia.core.session.model.network.ForgotPasswordData;

import org.parceler.Parcels;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by m.normansyah on 18/11/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ForgotPasswordImpl implements ForgotPassword {
    ForgotPasswordView forgotPasswordView;
    Context mContext;
    ForgotPasswordViewModel forgotPasswordViewModel;
    ForgotPasswordModel model;
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public ForgotPasswordImpl(ForgotPasswordView view) {
        forgotPasswordView = view;
    }

    @Override
    public void initDataInstances(Context context) {
        mContext = context;
        if (!isAfterRotate()) {
            forgotPasswordViewModel = new ForgotPasswordViewModel();
        }
    }

    @Override
    public void initData(Context context) {
        if (isAfterRotate()) {
            forgotPasswordView.setTextEmailSend(forgotPasswordViewModel.getEmail());
        }
    }

    @Override
    public void subscribe() {
        RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void unSubscribe() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    public void resetPassword(String email) {
        if (email == null || email.length() <= 0)
            return;

        forgotPasswordViewModel.setEmail(email);
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        ((SessionView) mContext).sendDataFromInternet(DownloadService.RESET_PASSWORD, bundle);
    }

    @Override
    public void setData(int type, Bundle data) {
        switch (type) {
            case DownloadService.RESET_PASSWORD:
                forgotPasswordView.dismissProgressDialog();
                forgotPasswordView.displayFrontView(false);
                forgotPasswordView.displaySuccessView(true);
                forgotPasswordView.setTextEmailSend(forgotPasswordViewModel.getEmail());
                break;
            default:
                break;
        }
    }

    @Override
    public void setError(int type, String text) {
        ((SessionView) mContext).showError(text);
        forgotPasswordView.dismissProgressDialog();
    }

    @Override
    public void sendRequest(Object... data) {
        if (data == null || data.length <= 0)
            return;

        forgotPasswordView.showProgressDialog();

        String email = (String) data[0];
        forgotPasswordViewModel.setEmail(email);

        final NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, mContext,
                TkpdBaseURL.User.URL_GENERAL_ACTION + TkpdBaseURL.User.PATH_RESET_PASSWORD)
                .setIdentity()
                .addParam("email", email)
                .compileAllParam()
                .finish();

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .header(NetworkCalculator.CONTENT_MD5,
                                NetworkCalculator.getContentMd5(networkCalculator))
                        .header(NetworkCalculator.DATE,
                                NetworkCalculator.getDate(networkCalculator))
                        .header(NetworkCalculator.AUTHORIZATION,
                                NetworkCalculator.getAuthorization(networkCalculator))
                        .addHeader(NetworkCalculator.X_METHOD,
                                NetworkCalculator.getxMethod(networkCalculator))
                        .build();
                return chain.proceed(newRequest);
            }
        };

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add the interceptor to OkHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.interceptors().add(interceptor);
        client.interceptors().add(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DeveloperOptions.getWsV4Domain(mContext))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client.build())
                .build();

        ForgotPasswordApi forgotPasswordApi = retrofit.create(ForgotPasswordApi.class);

        compositeSubscription.add(
                forgotPasswordApi.createForgotPassword(
                        NetworkCalculator.getUserId(mContext),
                        NetworkCalculator.getDeviceId(mContext),
                        NetworkCalculator.getHash(networkCalculator),
                        NetworkCalculator.getDeviceTime(networkCalculator),
                        email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ForgotPasswordData>() {
                            @Override
                            public void onCompleted() {
                                forgotPasswordView.dismissProgressDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                forgotPasswordView.dismissProgressDialog();
                                forgotPasswordView.displayFrontView(false);
                                forgotPasswordView.displaySuccessView(true);
                                forgotPasswordView.setTextEmailSend(forgotPasswordViewModel.getEmail());
                            }

                            @Override
                            public void onNext(ForgotPasswordData forgotPasswordData) {
                                if (forgotPasswordData.getForgotPasswordModel() != null
                                        && forgotPasswordData.getForgotPasswordModel().getIsSuccess() != 0) {
                                    forgotPasswordView.displayFrontView(false);
                                    forgotPasswordView.displaySuccessView(true);
                                    forgotPasswordView.setTextEmailSend(forgotPasswordViewModel.getEmail());
                                } else {
                                    forgotPasswordView.moveToRegister(forgotPasswordViewModel.getEmail());
                                }
                            }
                        })
        );
    }

    @Override
    public boolean isAfterRotate() {
        return forgotPasswordViewModel != null;
    }

    @Override
    public void saveDataBeforeRotate(Bundle outState) {
        outState.putParcelable(FORGOT_PASSWORD_VIEW, Parcels.wrap(forgotPasswordViewModel));
//        outState.putParcelable(FORGOT_PASSWORD, Parcels.wrap(model));

        boolean isLoading = forgotPasswordView.checkIsLoading();
        ;
        outState.putBoolean(FORGOT_PASSWORD_SHOW_DIALOG, isLoading);
    }

    @Override
    public void fetchDataAfterRotate(Bundle inState) {
        if (inState != null) {
//            model = Parcels.unwrap(inState.getParcelable(FORGOT_PASSWORD));
            forgotPasswordViewModel = Parcels.unwrap(inState.getParcelable(FORGOT_PASSWORD_VIEW));
            forgotPasswordView.setIsProgressDialog(inState.getBoolean(FORGOT_PASSWORD_SHOW_DIALOG, false));
        }
    }
}
