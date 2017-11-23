package com.tokopedia.transaction.purchase.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.apiservices.payment.PaymentTransactionService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.exception.HttpErrorException;
import com.tokopedia.transaction.exception.ResponseErrorException;
import com.tokopedia.transaction.exception.ResponseRuntimeException;
import com.tokopedia.transaction.purchase.domain.TxVerificationRepository;
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
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Angga.Prasetiyo on 11/04/2016.
 */
public class TxOrderNetInteractorImpl implements TxOrderNetInteractor {

    private final CompositeSubscription compositeSubscription;
    private final TXOrderService txOrderService;
    private final TXOrderActService txOrderActService;
    private final PaymentTransactionService paymentTransactionService;
    private final TxVerificationRepository verificationRepository;

    public TxOrderNetInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        txOrderService = new TXOrderService();
        txOrderActService = new TXOrderActService();
        paymentTransactionService = new PaymentTransactionService();
        verificationRepository = new TxVerificationRepository(paymentTransactionService);
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
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onFailed(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onFailed(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onFailed(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
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
                        listener.onFailed(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                } else {
                    listener.onFailed(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponse) {
                if (tkpdResponse.isSuccessful()) {
                    try {
                        if (tkpdResponse.body().getJsonData().getInt("is_success") == 0) {
                            listener.onError(tkpdResponse.body().getErrorMessages().get(0));
                        } else {
                            listener.onSuccess(
                                    tkpdResponse.body().convertDataObj(ConfirmationData.class)
                            );
                        }
                    } catch (Exception e) {
                        onError(e);
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.immediate())
                .unsubscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getConfirmPaymentForm(@NonNull final Context context,
                                      @NonNull Map<String, String> params,
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
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().isNullData()) {
                    listener.onSuccess(response.body().convertDataObj(FormConfPaymentData.class));
                } else {
                    listener.onError(
                            response.body().isError()
                                    && !response.body().getErrorMessages().isEmpty()
                                    ? response.body().getErrorMessages().get(0)
                                    : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getEditPaymentForm(@NonNull final Context context,
                                   @NonNull Map<String, String> params,
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
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().isNullData()) {
                    listener.onSuccess(response.body().convertDataObj(FormEditPaymentData.class));
                } else {
                    listener.onError(response.body().isError()
                            && !response.body().getErrorMessages().isEmpty()
                            ? response.body().getErrorMessages().get(0)
                            : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void requestCancelOrder(@NonNull TKPDMapParam<String, String> params,
                                   final RequestCancelOrderListener listener) {
        Observable<Response<TkpdResponse>> observable = txOrderActService.getApi()
                .requestCancelOrder(params);

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponse) {
                if (tkpdResponse.isSuccessful()) {
                    try {
                        if (tkpdResponse.body().getJsonData().getInt("is_success") == 1) {
                            listener.onSuccess(tkpdResponse.body().getStatusMessages().get(0));
                        } else {
                            listener.onError(tkpdResponse.body().getErrorMessages().get(0));
                        }
                    } catch (Exception e) {
                        onError(e);
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }
        };

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void showCancelTransactionDialog(@NonNull TKPDMapParam<String, String> params,
                                            final CancelTransactionDialogListener listener) {
        Observable<String> observable = verificationRepository
                .processCancelDialogResponse(params);

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if(e instanceof ResponseRuntimeException)
                                    listener.onError(e.getMessage());
                                else listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                            }

                            @Override
                            public void onNext(String message) {
                                listener.onSuccess(message);
                            }
                        })
        );
    }

    @Override
    public void cancelTransaction(@NonNull TKPDMapParam<String, String> params, final CancelTransactionListener listener) {
        Observable<String> observable = verificationRepository.processCancelTransaction(params);

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if(e instanceof ResponseRuntimeException)
                                    listener.onError(e.getMessage());
                                else listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                            }

                            @Override
                            public void onNext(String message) {
                                listener.onSuccess(message);
                            }
                        })
        );
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
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().isNullData()) {
                    listener.onSuccess(response.body().convertDataObj(CancelFormData.class));
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                } else if (response.body().isError()
                        && !response.body().getErrorMessages().isEmpty()) {
                    listener.onError(response.body().getErrorMessages().get(0));
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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

        final Subscriber<Response<TkpdResponse>> subscriber
                = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                        listener.onSuccess(
                                response.body().convertDataObj(TxConfListData.class)
                        );
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                if (e instanceof UnknownHostException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
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
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getInvoiceData(TKPDMapParam<String, String> paramNetwork,
                               Subscriber<TxVerInvoiceData> subscriberGetTXInvoiceData) {
        compositeSubscription.add(Observable.just(paramNetwork)
                .flatMap(new Func1<TKPDMapParam<String, String>,
                        Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(TKPDMapParam<String,
                            String> param) {
                        return txOrderService.getApi()
                                .getTXOrderPaymentConfirmedDetail(param);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, TxVerInvoiceData>() {
                    @Override
                    public TxVerInvoiceData call(Response<TkpdResponse> response) {
                        return transformToTxVerInvoiceData(response);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberGetTXInvoiceData)
        );
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
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onError((response.body().getErrorMessages() != null
                            && !response.body().getErrorMessages().isEmpty())
                            ? response.body().getErrorMessages().get(0)
                            : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onNoConnection(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onError((response.body().getErrorMessages() != null
                            && !response.body().getErrorMessages().isEmpty())
                            ? response.body().getErrorMessages().get(0)
                            : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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

    private TxVerInvoiceData transformToTxVerInvoiceData(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            TkpdResponse tkpdResponse = response.body();
            if (tkpdResponse.isError()) {
                throw new RuntimeException(
                        new ResponseErrorException(
                                tkpdResponse.getErrorMessageJoined()
                        )
                );
            } else {
                return tkpdResponse.convertDataObj(TxVerInvoiceData.class);
            }
        } else {
            throw new RuntimeException(new HttpErrorException(response.code()));
        }
    }

}
