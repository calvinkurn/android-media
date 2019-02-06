package com.tokopedia.inbox.rescenter.shipping.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.inbox.rescenter.shipping.model.ActionResponseData;
import com.tokopedia.inbox.rescenter.shipping.model.ShippingParamsPostModel;
import com.tokopedia.inbox.rescenter.shipping.model.NewUploadResCenterImageData;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;
import com.tokopedia.inbox.rescenter.utils.UploadImageResCenter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hangnadi on 12/14/16.
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractor.class.getSimpleName();

    private final InboxResCenterService inboxResCenterService;
    private final ResCenterActService resCenterActService;
    private final CompositeSubscription compositeSubscription;

    public RetrofitInteractorImpl() {
        this.inboxResCenterService = new InboxResCenterService();
        this.resCenterActService = new ResCenterActService();
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void getShippingList(@NonNull final Context context,
                                @NonNull final TKPDMapParam params,
                                @NonNull final GetKurirListener listener) {

        listener.onStart();

        Observable<Response<TkpdResponse>> observable = inboxResCenterService.getApi().getCourierList(params);

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getShippingList(context, params, listener);
                        }
                    });
                } else {
                    listener.onError(null);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(ResCenterKurir.class));
                    } else {
                        if (response.body().getErrorMessages() == null
                                && response.body().getErrorMessages().isEmpty()) {
                            listener.onError(null);
                        } else {
                            listener.onError(response.body().getErrorMessages().get(0));
                        }
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(null);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getShippingList(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(null);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(null);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(null);
                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
        );
    }
}
