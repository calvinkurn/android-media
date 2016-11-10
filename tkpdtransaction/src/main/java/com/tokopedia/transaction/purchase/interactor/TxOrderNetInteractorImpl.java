package com.tokopedia.transaction.purchase.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.transaction.purchase.model.ConfirmationData;
import com.tokopedia.transaction.purchase.model.response.cancelform.CancelFormData;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.FormConfPaymentData;
import com.tokopedia.transaction.purchase.model.response.formconfirmpayment.FormEditPaymentData;
import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfListData;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderListData;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerListData;
import com.tokopedia.transaction.purchase.model.response.txverinvoice.TxVerInvoiceData;

import org.json.JSONException;

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
 * TxOrderNetInteractorImpl
 * Created by Angga.Prasetiyo on 11/04/2016.
 */
public class TxOrderNetInteractorImpl implements TxOrderNetInteractor {

    private final CompositeSubscription compositeSubscription;
    private final TXOrderService txOrderService;
    private final TXOrderActService txOrderActService;

    public TxOrderNetInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        txOrderService = new TXOrderService();
        txOrderActService = new TXOrderActService();
    }

    @Override
    public void uploadValidProofByPayment(final Context context, Map<String, String> params,
                                          final OnUploadProof listener) {
        Observable<Response<TkpdResponse>> observable = txOrderActService.getApi()
                .uploadValidProofByPayment(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onFailed(context.getString(R.string.default_request_error_timeout));
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().isNullData()) {
                    int isSuccess;
                    try {
                        isSuccess = response.body().getJsonData().getInt("is_success");
                        if (isSuccess == 1)
                            listener.onSuccess(response.body().getStatusMessages().get(0));
                        else listener.onFailed(response.body().getErrorMessages().get(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailed(context.getString(R.string.default_request_error_timeout));
                    }
                } else {
                    listener.onFailed(context.getString(R.string.default_request_error_timeout));
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void confirmPayment(@NonNull final Context context, @NonNull Map<String, String> params,
                               final PaymentActionListener listener) {
        Observable<Response<TkpdResponse>> observable = txOrderActService.getApi()
                .confirmPayment(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection("");
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(context.getString(R.string.default_request_error_timeout));
                } else {
                    listener.onError(context.getString(R.string.default_request_error_unknown));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponse) {
                if (tkpdResponse.isSuccessful()) {
                    try {
                        if (tkpdResponse.body().getJsonData().getInt("is_success") == 0) {
                            listener.onError(tkpdResponse.body().getErrorMessages().get(0));
                        } else {
                            listener.onSuccess(tkpdResponse.body().convertDataObj(ConfirmationData.class));
                        }
                    } catch (Exception e) {
                        onError(e);
                    }
                } else {
                    listener.onError(context.getString
                            (R.string.default_request_error_unknown));
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.immediate())
                .unsubscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void editPayment(@NonNull final Context context, @NonNull Map<String, String> params,
                            final PaymentActionListener listener) {
        Observable<Response<TkpdResponse>> observable = txOrderActService.getApi()
                .editPaymnet(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection("");
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(context.getString(R.string.default_request_error_timeout));
                } else {
                    listener.onError(context.getString(R.string.default_request_error_unknown));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponse) {
                if (tkpdResponse.isSuccessful()) {
                    try {
                        if (tkpdResponse.body().getJsonData().getInt("is_success") == 0) {
                            listener.onError(tkpdResponse.body().getErrorMessages().get(0));
                        } else {
                            listener.onSuccess(tkpdResponse.body()
                                    .convertDataObj(ConfirmationData.class));
                        }
                    } catch (Exception e) {
                        onError(e);
                    }
                } else {
                    listener.onError(context.getString(R.string.default_request_error_unknown));
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.immediate())
                .unsubscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getConfirmPaymentForm(@NonNull final Context context, @NonNull Map<String, String> params,
                                      final ConfirmPaymentFormListener listener) {
        Observable<Response<TkpdResponse>> observable = txOrderService.getApi()
                .getConfirmPaymentForm(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection("");
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(context.getString(R.string.default_request_error_timeout));
                } else {
                    listener.onError(context.getString(R.string.default_request_error_unknown));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().isNullData()) {
                    listener.onSuccess(response.body().convertDataObj(FormConfPaymentData.class));
                } else {
                    listener.onError(response.body().isError() && !response.body().getErrorMessages().isEmpty()
                            ? response.body().getErrorMessages().get(0)
                            : context.getString(R.string.default_request_error_unknown));
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getEditPaymentForm(@NonNull final Context context, @NonNull Map<String, String> params,
                                   final EditPaymentFormListener listener) {
        Observable<Response<TkpdResponse>> observable = txOrderService.getApi()
                .getEditPaymentForm(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection("");
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(context.getString(R.string.default_request_error_timeout));
                } else {
                    listener.onError(context.getString(R.string.default_request_error_unknown));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().isNullData()) {
                    listener.onSuccess(response.body().convertDataObj(FormEditPaymentData.class));
                } else {
                    listener.onError(response.body().isError() && !response.body().getErrorMessages().isEmpty()
                            ? response.body().getErrorMessages().get(0)
                            : context.getString(R.string.default_request_error_unknown));
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getCancelPaymentForm(final Context context, final Map<String, String> params,
                                     final OnCancelPaymentForm listener) {
        Observable<Response<TkpdResponse>> observable = txOrderService.getApi()
                .getCancelPaymentForm(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
//                    DialogNoConnection.createShow(context,
//                            new DialogNoConnection.ActionListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    getCancelPaymentForm(context, params, listener);
//                                }
//                            });
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else {
                    listener.onError("Terjadi kesalahan, mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().isNullData()) {
                    listener.onSuccess(response.body().convertDataObj(CancelFormData.class));
                } else {
                    listener.onError("Terjadi kesalahan, mohon ulangi beberapa saat lagi");
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void cancelPayment(final Context context, final Map<String, String> params,
                              final OnCancelPayment listener) {
        Observable<Response<TkpdResponse>> observable = txOrderActService.getApi()
                .cancelPayment(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
//                    DialogNoConnection.createShow(context,
//                            new DialogNoConnection.ActionListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    cancelPayment(context, params, listener);
//                                }
//                            });
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else {
                    listener.onError("Terjadi kesalahan, mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().getJsonData().isNull("is_success")) {
                    try {
                        if (response.body().getJsonData().getInt("is_success") == 1) {
                            listener.onSuccess();
                        } else {
                            listener.onError(response.body().getErrorMessages().get(0));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else if (response.body().isError() && !response.body().getErrorMessages().isEmpty()) {
                    listener.onError(response.body().getErrorMessages().get(0));

                } else {
                    listener.onError(context.getString(R.string.default_request_error_unknown));
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getOrderStatusList(@NonNull final Context context,
                                   @NonNull final Map<String, String> params,
                                   @NonNull final OnGetOrderStatusList listener) {

        final Observable<Response<TkpdResponse>> observable = txOrderService.getApi()
                .getTXOrderStatus(AuthUtil.generateParams(context, params));

        final Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
//                    DialogNoConnection.createShow(context,
//                            new DialogNoConnection.ActionListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    getOrderStatusList(context, params, listener);
//                                }
//                            });
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
                    OrderListData data = response.body().convertDataObj(OrderListData.class);
                    if (response.body().isNullData() || data == null
                            || data.getOrderDataList() == null
                            || data.getOrderDataList().isEmpty()) {
                        listener.onEmptyData();
                    } else {
                        listener.onSuccess(response.body().getJsonData(),
                                response.body().convertDataObj(OrderListData.class));
                    }
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }
        };


        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getPaymentConfirmationList(@NonNull final Context context,
                                           final @NonNull Map<String, String> params,
                                           @NonNull final OnGetPaymentConfirmationList listener) {
        Observable<Response<TkpdResponse>> observable = txOrderService.getApi()
                .getTXOrderPaymentConfirmation(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
//                    DialogNoConnection.createShow(context,
//                            new DialogNoConnection.ActionListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    getPaymentConfirmationList(context, params, listener);
//                                }
//                            });
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
                    TxConfListData data = response.body().convertDataObj(TxConfListData.class);
                    if (response.body().isNullData() || data == null
                            || data.getTxConfDataList() == null
                            || data.getTxConfDataList().isEmpty()) {
                        listener.onEmptyData();
                    } else {
                        listener.onSuccess(response.body().getJsonData(),
                                response.body().convertDataObj(TxConfListData.class));
                    }
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public void getPaymentVerificationList(@NonNull final Context context,
                                           @NonNull final Map<String, String> params,
                                           @NonNull final OnGetPaymentVerificationList listener) {
        Observable<Response<TkpdResponse>> observable = txOrderService.getApi()
                .getTXOrderPaymentConfirmed(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
//                    DialogNoConnection.createShow(context,
//                            new DialogNoConnection.ActionListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    getPaymentVerificationList(context, params, listener);
//                                }
//                            });
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
                    TxVerListData data = response.body().convertDataObj(TxVerListData.class);
                    if (response.body().isNullData() || data == null
                            || data.getTxVerDataList() == null
                            || data.getTxVerDataList().isEmpty()) {
                        listener.onEmptyData();
                    } else {
                        listener.onSuccess(data);
                    }
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void deliverReject(@NonNull final Context context, @NonNull Map<String, String> params,
                              @NonNull final OnDeliverReject listener) {
        Observable<Response<TkpdResponse>> observable = txOrderActService.getApi()
                .deliveryReject(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(context.getString(R.string.msg_connection_timeout_toast));
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
                    try {
                        int status = response.body().getJsonData().getInt("is_success");
                        switch (status) {
                            case 1:
                                listener.onSuccess("");
                                break;
                            default:
                                listener.onFailed("");
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                    }
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getInvoiceData(@NonNull final Context context, @NonNull Map<String, String> params,
                               @NonNull final OnGetInvoiceData listener) {
        Observable<Response<TkpdResponse>> observable = txOrderService.getApi()
                .getTXOrderPaymentConfirmedDetail(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(context.getString(R.string.msg_connection_timeout_toast));
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
                    TxVerInvoiceData data = response.body().convertDataObj(TxVerInvoiceData.class);
                    listener.onSuccess(data);
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getOrderReceiveList(@NonNull final Context context,
                                    @NonNull final Map<String, String> params,
                                    @NonNull final OnGetOrderReceiveList listener) {
        Observable<Response<TkpdResponse>> observable = txOrderService.getApi()
                .getTXOrderDeliver(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
//                    DialogNoConnection.createShow(context,
//                            new DialogNoConnection.ActionListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    getOrderReceiveList(context, params, listener);
//                                }
//                            });
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
                    OrderListData data = response.body().convertDataObj(OrderListData.class);
                    if (response.body().isNullData() || data == null
                            || data.getOrderDataList() == null
                            || data.getOrderDataList().isEmpty()) {
                        listener.onEmptyData();
                    } else {
                        listener.onSuccess(response.body().getJsonData(),
                                response.body().convertDataObj(OrderListData.class));
                    }
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getOrderTxList(@NonNull final Context context,
                               @NonNull final Map<String, String> params,
                               @NonNull final OnGetOrderTxList listener) {
        Observable<Response<TkpdResponse>> observable = txOrderService.getApi()
                .getTXOrderList(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
//                    DialogNoConnection.createShow(context,
//                            new DialogNoConnection.ActionListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    getOrderTxList(context, params, listener);
//                                }
//                            });
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()) {
                    OrderListData data = response.body().convertDataObj(OrderListData.class);
                    if (response.body().isNullData() || data == null
                            || data.getOrderDataList() == null
                            || data.getOrderDataList().isEmpty()) {
                        listener.onEmptyData();
                    } else {
                        listener.onSuccess(response.body().getJsonData(),
                                response.body().convertDataObj(OrderListData.class));
                    }
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void confirmFinishDeliver(@NonNull final Context context,
                                     @NonNull final Map<String, String> params,
                                     @NonNull final OnConfirmFinishDeliver listener) {
        Observable<Response<TkpdResponse>> observable = txOrderActService.getApi()
                .deliveryFinishOrder(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
//                    DialogNoConnection.createShow(context,
//                            new DialogNoConnection.ActionListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    confirmFinishDeliver(context, params, listener);
//                                }
//                            });
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                try {
                    if (!tkpdResponseResponse.body().isError() && tkpdResponseResponse.isSuccessful()
                            && tkpdResponseResponse.body().getJsonData()
                            .getString("is_success").equals("1")) {
                        listener.onSuccess(tkpdResponseResponse.body().getStatusMessages().get(0),
                                tkpdResponseResponse.body().getJsonData().getJSONObject("ld"));
                    } else {
                        if (!tkpdResponseResponse.body().getErrorMessages().get(0).isEmpty()) {
                            listener.onError(tkpdResponseResponse.body().getErrorMessages().get(0));
                        } else {
                            listener.onError(context.getString(R.string.msg_connection_timeout));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError(context.getString(R.string.msg_connection_timeout));
                }

            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void confirmDeliver(@NonNull final Context context,
                               @NonNull final Map<String, String> params,
                               @NonNull final OnConfirmFinishDeliver listener) {
        Observable<Response<TkpdResponse>> observable = txOrderActService.getApi()
                .deliveryConfirm(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
//                    DialogNoConnection.createShow(context,
//                            new DialogNoConnection.ActionListener() {
//                                @Override
//                                public void onRetryClicked() {
//                                    confirmDeliver(context, params, listener);
//                                }
//                            });
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                } else {
                    listener.onError(context.getString(R.string.msg_connection_timeout_toast));
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                try {
                    if (!tkpdResponseResponse.body().isError() && tkpdResponseResponse.isSuccessful()
                            && tkpdResponseResponse.body().getJsonData()
                            .getString("is_success").equals("1")) {
                        listener.onSuccess(tkpdResponseResponse.body().getStatusMessages().get(0),
                                tkpdResponseResponse.body().getJsonData().getJSONObject("ld"));
                    } else {
                        if (!tkpdResponseResponse.body().getErrorMessages().get(0).isEmpty()) {
                            listener.onError(tkpdResponseResponse.body().getErrorMessages().get(0));
                        } else {
                            listener.onError(context.getString(R.string.msg_connection_timeout));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError(context.getString(R.string.msg_connection_timeout));
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
}
