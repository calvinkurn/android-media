package com.tokopedia.inbox.rescenter.create.interactor;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;

import java.io.IOException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 6/17/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractor.class.getSimpleName();
    private static final String ERROR_UNKNOWN = "$@1";
    private static final String ERROR_TIMEOUT = "$@2";
    private static final String ERROR_SERVER_ERROR = "$@3";
    private static final String ERROR_BAD_REQUEST = "$@4";
    private static final String ERROR_FORBIDDEN = "$@5";
    private static final String ERROR_NULL = "$@6";


    private final InboxResCenterService inboxResCenterService;
    private final CompositeSubscription compositeSubscription;

    public RetrofitInteractorImpl() {
        this.inboxResCenterService = new InboxResCenterService();
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getFormCreateResCenter(@NonNull final Context context,
                                       @NonNull final Map<String, String> params,
                                       @NonNull final FormResCenterListener listener) {

        compositeSubscription.add(
                Observable.just(params)
                        .flatMap(new Func1<Map<String, String>, Observable<CreateResCenterFormData>>() {
                            @Override
                            public Observable<CreateResCenterFormData> call(Map<String, String> stringStringMap) {
                                Observable<Response<TkpdResponse>> getFormRetrofit = inboxResCenterService.getApi()
                                        .getCreateResFormNew(AuthUtil.generateParams(context, params));

                                Observable<Response<TkpdResponse>> getProductListRetrofit = inboxResCenterService.getApi()
                                        .getResCenterProductList(AuthUtil.generateParams(context, params));

                                return Observable.zip(getFormRetrofit, getProductListRetrofit, new Func2<Response<TkpdResponse>, Response<TkpdResponse>, CreateResCenterFormData>() {
                                    @Override
                                    public CreateResCenterFormData call(Response<TkpdResponse> responseForm, Response<TkpdResponse> responseProduct) {
                                        if (responseForm.isSuccessful() && responseProduct.isSuccessful()) {
                                            if (!responseForm.body().isError() && !responseProduct.body().isError()) {
                                                CreateResCenterFormData modelData = responseForm.body().convertDataObj(CreateResCenterFormData.class);
                                                modelData.setListProd(responseProduct.body().convertDataObj(CreateResCenterFormData.class).getListProd());
                                                return modelData;
                                            } else {
                                                if (responseForm.body().isNullData() || responseForm.body().isNullData()) {
                                                    throw new RuntimeException(ERROR_NULL);
                                                } else {
                                                    if (responseForm.body().isError()) {
                                                        throw new RuntimeException(responseForm.body().getErrorMessages().get(0));
                                                    } else {
                                                        throw new RuntimeException(responseProduct.body().getErrorMessages().get(0));
                                                    }
                                                }
                                            }
                                        } else {
                                            if (!responseForm.isSuccessful()) {
                                                createCustomErrorHandler(responseForm.code());
                                            } else {
                                                createCustomErrorHandler(responseProduct.code());
                                            }
                                        }
                                        return null;
                                    }
                                });
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<CreateResCenterFormData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.toString());
                                if (e instanceof IOException) {
                                    listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                                        @Override
                                        public void onRetryClicked() {
                                            getFormCreateResCenter(context, params, listener);
                                        }
                                    });
                                } else if (e instanceof RuntimeException) {
                                    switch (e.getMessage()) {
                                        case ERROR_UNKNOWN:
                                            listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                                                @Override
                                                public void onRetryClicked() {
                                                    getFormCreateResCenter(context, params, listener);
                                                }
                                            });
                                            break;
                                        case ERROR_TIMEOUT:
                                            listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                                                @Override
                                                public void onRetryClicked() {
                                                    getFormCreateResCenter(context, params, listener);
                                                }
                                            });
                                            break;
                                        case ERROR_SERVER_ERROR:
                                            listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                                                @Override
                                                public void onRetryClicked() {
                                                    getFormCreateResCenter(context, params, listener);
                                                }
                                            });
                                            break;
                                        case ERROR_BAD_REQUEST:
                                            listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                                                @Override
                                                public void onRetryClicked() {
                                                    getFormCreateResCenter(context, params, listener);
                                                }
                                            });
                                            break;
                                        case ERROR_FORBIDDEN:
                                            listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                                                @Override
                                                public void onRetryClicked() {
                                                    getFormCreateResCenter(context, params, listener);
                                                }
                                            });
                                            break;
                                        case ERROR_NULL:
                                            listener.onNullData();
                                            break;
                                        default:
                                            listener.onError(e.getMessage(), null);
                                            break;
                                    }
                                } else {
                                    listener.onNullData();
                                }
                            }

                            @Override
                            public void onNext(CreateResCenterFormData data) {
                                listener.onSuccess(data);
                            }
                        })
        );
    }

    private void createCustomErrorHandler(int code) {
        new ErrorHandler(new ErrorListener() {
            @Override
            public void onUnknown() {
                throw new RuntimeException(ERROR_UNKNOWN);
            }

            @Override
            public void onTimeout() {
                throw new RuntimeException(ERROR_TIMEOUT);
            }

            @Override
            public void onServerError() {
                throw new RuntimeException(ERROR_SERVER_ERROR);
            }

            @Override
            public void onBadRequest() {
                throw new RuntimeException(ERROR_BAD_REQUEST);
            }

            @Override
            public void onForbidden() {
                throw new RuntimeException(ERROR_FORBIDDEN);
            }
        }, code);
    }

    @Override
    public void getSolution(@NonNull final Context context,
                            @NonNull final Map<String, String> params,
                            @NonNull final FormSolutionListener listener) {

        Observable<Response<TkpdResponse>> observable = inboxResCenterService.getApi()
                .getSolutionList(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getSolution(context, params, listener);
                        }
                    });
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        Log.d(TAG, "onNext: " + response.body());
                        CreateResCenterFormData data = response.body().convertDataObj(CreateResCenterFormData.class);
                        listener.onSuccess(data.getListSolution());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getSolution(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
