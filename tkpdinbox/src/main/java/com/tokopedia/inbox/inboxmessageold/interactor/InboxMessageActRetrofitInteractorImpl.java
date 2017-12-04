package com.tokopedia.inbox.inboxmessageold.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetail;
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
 * Created by Nisie on 5/16/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class InboxMessageActRetrofitInteractorImpl implements InboxMessageActRetrofitInteractor {

    private static final String TAG = InboxMessageRetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";
    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";

    private final CompositeSubscription compositeSubscription;
    private KunyitService kunyitService;
    private boolean isRequesting;

    public InboxMessageActRetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.kunyitService = new KunyitService();
        this.isRequesting = false;
    }


    @Override
    public void archiveMessage(@NonNull final Context context, @NonNull final Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .archiveMessage(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void undoArchiveMessage(@NonNull final Context context, @NonNull final Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .undoArchiveMessage(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void deleteMessage(@NonNull final Context context, @NonNull Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .deleteMessage(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void undoDeleteMessage(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .undoDeleteMessage(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void moveToInbox(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .moveToInbox(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void undoMoveToInbox(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .undoMoveToInbox(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void deleteForever(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .deleteForever(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void flagDetailSpam(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .flagSpam(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void undoFlagDetailSpam(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .undoFlagSpam(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void replyMessage(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final SendReplyInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .replyMessage(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(InboxMessageDetail.class));
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void markAsRead(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .markAsRead(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void markAsUnread(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final ActInboxMessageListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .markAsUnread(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoInternetConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(DEFAULT_MSG_ERROR);
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
    public void unSubscribeObservable() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public boolean isRequesting() {
        return isRequesting;
    }

    @Override
    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }
}
