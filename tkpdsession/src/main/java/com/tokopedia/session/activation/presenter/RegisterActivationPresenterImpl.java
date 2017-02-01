package com.tokopedia.session.activation.presenter;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.interactor.ActivationNetworkInteractor;
import com.tokopedia.session.activation.viewListener.RegisterActivationView;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 1/31/17.
 */

public class RegisterActivationPresenterImpl implements RegisterActivationPresenter {


    private final RegisterActivationView viewListener;
    private final ActivationNetworkInteractor networkInteractor;
    private final CompositeSubscription compositeSubscription;

    public RegisterActivationPresenterImpl(RegisterActivationView viewListener,
                                           ActivationNetworkInteractor networkInteractor,
                                           CompositeSubscription compositeSubscription) {
        this.viewListener = viewListener;
        this.networkInteractor = networkInteractor;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void resendActivation() {
            viewListener.showLoadingProgress();
            compositeSubscription.add(networkInteractor.resendActivation(getResendActivationParam())
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<TkpdResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof UnknownHostException) {
                                viewListener.onErrorResendActivation(viewListener.getString(R.string.msg_no_connection));
                            } else if (e instanceof SocketTimeoutException) {
                                viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_timeout));
                            } else if (e instanceof IOException) {
                                viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_internal_server));
                            } else {
                                viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_unknown));
                            }
                        }

                        @Override
                        public void onNext(Response<TkpdResponse> response) {
                            if (response.isSuccessful()) {
                                final TkpdResponse tkpdResponse = response.body();
                                if (!tkpdResponse.isError()) {
                                    JSONObject result = tkpdResponse.getJsonData();
                                    boolean isSuccess = "1".equals(result.optString("is_success"));
                                    if (isSuccess) {
                                        viewListener.onSuccessResendActivation(response.body().getStatusMessageJoined());
                                    } else {
                                        viewListener.onErrorResendActivation(response.body().getErrorMessageJoined());
                                    }
                                } else {
                                    viewListener.onErrorResendActivation(response.body().getErrorMessageJoined());
                                }
                            } else {
                                new ErrorHandler(new ErrorListener() {
                                    @Override
                                    public void onUnknown() {
                                        viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_unknown));
                                    }

                                    @Override
                                    public void onTimeout() {
                                        viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_timeout));
                                    }

                                    @Override
                                    public void onServerError() {
                                        viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_internal_server));
                                    }

                                    @Override
                                    public void onBadRequest() {
                                        viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_bad_request));
                                    }

                                    @Override
                                    public void onForbidden() {
                                        viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_forbidden_auth));
                                    }
                                }, response.code());
                            }

                        }
                    }));

    }

    private TKPDMapParam<String, String> getResendActivationParam() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("email", viewListener.getEmail());
        return param;
    }
}
