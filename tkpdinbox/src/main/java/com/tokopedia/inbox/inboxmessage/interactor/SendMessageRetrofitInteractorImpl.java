package com.tokopedia.inbox.inboxmessage.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nisie on 5/26/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class SendMessageRetrofitInteractorImpl implements SendMessageRetrofitInteractor {

    private static final String TAG = SendMessageRetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";

    private final CompositeSubscription compositeSubscription;
    private KunyitService kunyitService;

    public SendMessageRetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.kunyitService = new KunyitService();

    }

    @Override
    public void sendMessage(@NonNull final Context context, @NonNull Map<String, String> params, @NonNull final SendMessageListener listener) {
//        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
//                .sendMessageWithWebsocket(AuthUtil.generateParams(context, params));
//
//        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e(TAG, e.toString());
//                if (e instanceof UnknownHostException) {
//                    listener.onNoNetworkConnection();
//                } else if (e instanceof SocketTimeoutException) {
//                    listener.onTimeout();
//                } else {
//                    listener.onError("Terjadi Kesalahan, " +
//                            "Mohon ulangi beberapa saat lagi");
//                }
//            }
//
//            @Override
//            public void onNext(Response<TkpdResponse> response) {
//                if (response.isSuccessful()) {
//                    if (!response.body().isError()) {
//                        listener.onSuccess();
//                    } else {
//                        if (response.body().isNullData()) listener.onNullData();
//                        else listener.onError(response.body().getErrorMessages().get(0));
//                    }
//                } else {
//                    new ErrorHandler(new ErrorListener() {
//                        @Override
//                        public void onUnknown() {
//                            listener.onError("Terjadi Kesalahan, " +
//                                    "Mohon ulangi beberapa saat lagi");
//                        }
//
//                        @Override
//                        public void onTimeout() {
//                            listener.onTimeout();
//                        }
//
//                        @Override
//                        public void onServerError() {
//                            listener.onError("Terjadi Kesalahan, " +
//                                    "Mohon ulangi beberapa saat lagi");
//                        }
//
//                        @Override
//                        public void onBadRequest() {
//                            listener.onError("Terjadi Kesalahan, " +
//                                    "Mohon ulangi beberapa saat lagi");
//                        }
//
//                        @Override
//                        public void onForbidden() {
//                            listener.onError("Terjadi Kesalahan, " +
//                                    "Mohon ulangi beberapa saat lagi");
//                        }
//                    }, response.code());
//                }
//            }
//        };
//        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
//                .unsubscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
    }

    @Override
    public void unSubscribeObservable() {
        compositeSubscription.unsubscribe();
    }
}
