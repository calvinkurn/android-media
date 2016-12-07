package com.tokopedia.transaction.cart.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.apiservices.transaction.TXActService;
import com.tokopedia.core.network.apiservices.transaction.TXCartActService;
import com.tokopedia.core.network.apiservices.transaction.TXCartService;
import com.tokopedia.core.network.apiservices.transaction.TXService;
import com.tokopedia.core.network.apiservices.transaction.TXVoucherService;
import com.tokopedia.core.network.apiservices.user.PeopleActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.model.ResponseTransform;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentData;
import com.tokopedia.transaction.cart.model.cartdata.CartModel;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.model.voucher.VoucherData;
import com.tokopedia.transaction.exception.HttpErrorException;
import com.tokopedia.transaction.exception.ResponseErrorException;

import org.json.JSONException;

import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 11/2/16.
 *         collabs with alvarisi
 */
public class CartDataInteractor implements ICartDataInteractor {
    private static final String KEY_FLAG_IS_SUCCESS = "is_success";

    private final TXService txService;
    private final TXActService txActService;
    private final TXCartActService txCartActService;
    private final TXVoucherService txVoucherService;
    private final CompositeSubscription compositeSubscription;

    public CartDataInteractor() {
        this.compositeSubscription = new CompositeSubscription();
        this.txService = new TXService();
        this.txCartActService = new TXCartActService();
        this.txActService = new TXActService();
        this.txVoucherService = new TXVoucherService();
    }

    @Override
    public void calculateCart(TKPDMapParam<String, String> param,
                              Subscriber<Object> subscriber) {

    }

    @Override
    public void getCartData(TKPDMapParam<String, String> param,
                            Subscriber<CartModel> subscriber) {
        Observable<Response<TkpdResponse>> observable = txService.getApi().doPayment(param);
        compositeSubscription.add(observable
                .map(new Func1<Response<TkpdResponse>, CartModel>() {
                    @Override
                    public CartModel call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                return response.body().convertDataObj(CartModel.class);
                            } else {
                                throw new RuntimeException(
                                        new ResponseErrorException(
                                                response.body().getErrorMessageJoined()
                                        )
                                );
                            }
                        } else {
                            throw new RuntimeException(
                                    new HttpErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
                            );
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public void cancelCart(final TKPDMapParam<String, String> paramCancelCart,
                           final TKPDMapParam<String, String> paramCartInfo,
                           Subscriber<ResponseTransform<CartModel>> subscriber) {
        final Observable<Response<TkpdResponse>> observable
                = txCartActService.getApi().cancelCart(paramCancelCart);
        compositeSubscription.add(observable
                .map(funcTransformFromUpdateDeleteActionCart())
                .flatMap(funcTransformFromGetCartInfo(paramCartInfo))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void calculateShipment(TKPDMapParam<String, String> param,
                                  Subscriber<CalculateShipmentData> subscriber) {
        Observable.just(param)
                .flatMap(new Func1<TKPDMapParam<String, String>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(TKPDMapParam<String, String> stringStringTKPDMapParam) {
                        TXCartService service = new TXCartService();
                        return service
                                .getApi()
                                .calculateCart(stringStringTKPDMapParam);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, CalculateShipmentData>() {
                    @Override
                    public CalculateShipmentData call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                return response.body().convertDataObj(CalculateShipmentData.class);
                            } else {
                                throw new RuntimeException(response.body().getErrorMessages().get(0));
                            }
                        } else {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onTimeout() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                                }

                                @Override
                                public void onServerError() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onBadRequest() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onForbidden() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }
                            }, response.code());
                        }
                        throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void updateCart(TKPDMapParam<String, String> paramUpdate,
                           final TKPDMapParam<String, String> paramCart,
                           Subscriber<ResponseTransform<CartModel>> subscriber) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .editCart(paramUpdate);
        compositeSubscription.add(observable
                .map(funcTransformFromUpdateDeleteActionCart())
                .flatMap(funcTransformFromGetCartInfo(paramCart))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void updateInsuranceCart(TKPDMapParam<String, String> paramUpdate,
                                    final TKPDMapParam<String, String> paramCart,
                                    Subscriber<ResponseTransform<CartModel>> subscriber) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .editInsurance(paramUpdate);
        compositeSubscription.add(observable
                .map(funcTransformFromUpdateDeleteActionCart())
                .flatMap(funcTransformFromGetCartInfo(paramCart))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void getParameterTopPay(TKPDMapParam<String, String> params, Scheduler scheduler,
                                   Subscriber<TopPayParameterData> subscriber) {
        Observable<Response<TkpdResponse>> observable
                = txActService.getApi().getParameterDynamicPayment(params);
        compositeSubscription.add(observable
                .flatMap(new Func1<Response<TkpdResponse>, Observable<TopPayParameterData>>() {
                    @Override
                    public Observable<TopPayParameterData> call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                return Observable.just(response.body().convertDataObj(
                                        TopPayParameterData.class)
                                );
                            } else {
                                throw new RuntimeException(
                                        new ResponseErrorException(
                                                response.body().getErrorMessages().get(0)
                                        )
                                );
                            }
                        } else {
                            throw new RuntimeException(new HttpErrorException(response.code()));
                        }
                    }
                })
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .unsubscribeOn(scheduler)
                .subscribe(subscriber)
        );
    }

    @Override
    public void editShipmentCart(TKPDMapParam<String, String> param,
                                 Subscriber<ShipmentCartData> subscriber) {
        Observable.just(param)
                .flatMap(new Func1<TKPDMapParam<String, String>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(TKPDMapParam<String, String> params) {
                        TXCartActService service = new TXCartActService();
                        return service
                                .getApi()
                                .editAddress(params);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, ShipmentCartData>() {
                    @Override
                    public ShipmentCartData call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                if (!response.body().isNullData() &&
                                        !response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                                    int status = 0;
                                    try {
                                        status = response.body().getJsonData()
                                                .getInt(KEY_FLAG_IS_SUCCESS);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String message;
                                    if (status == 1) {
                                        message = response.body()
                                                .getStatusMessages().get(0);
                                    } else {
                                        message = response.body().getErrorMessages().get(0);
                                    }
                                    ShipmentCartData shipmentCartData = new ShipmentCartData();
                                    shipmentCartData.setStatus(String.valueOf(status));
                                    shipmentCartData.setMessage(message);
                                    return shipmentCartData;
                                } else {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                                }
                            } else {
                                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                            }
                        } else {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onTimeout() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                                }

                                @Override
                                public void onServerError() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onBadRequest() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onForbidden() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }
                            }, response.code());
                        }
                        throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void editLocationShipment(TKPDMapParam<String, String> param,
                                     Subscriber<SaveLocationData> subscriber) {
        Observable.just(param)
                .flatMap(new Func1<TKPDMapParam<String, String>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(TKPDMapParam<String, String> params) {
                        PeopleActService service = new PeopleActService();
                        return service
                                .getApi()
                                .editAddress(params);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, SaveLocationData>() {
                    @Override
                    public SaveLocationData call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                if (!response.body().isNullData() &&
                                        !response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                                    return response.body().convertDataObj(SaveLocationData.class);
                                } else {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                                }
                            } else {
                                throw new RuntimeException(response.body().getErrorMessages().get(0));
                            }
                        } else {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onTimeout() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                                }

                                @Override
                                public void onServerError() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onBadRequest() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onForbidden() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }
                            }, response.code());
                        }
                        throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void checkVoucherCode(TKPDMapParam<String, String> param,
                                 Subscriber<ResponseTransform<VoucherData>> subscriber) {
        Observable.just(param)
                .flatMap(new Func1<TKPDMapParam<String, String>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(TKPDMapParam<String, String> param) {
                        return txVoucherService.getApi().checkVoucherCode(param);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, ResponseTransform<VoucherData>>() {
                    @Override
                    public ResponseTransform<VoucherData> call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                ResponseTransform<VoucherData> voucherDataResponseTransform
                                        = new ResponseTransform<>();
                                voucherDataResponseTransform.setData(
                                        response.body().convertDataObj(VoucherData.class)
                                );
                                voucherDataResponseTransform.setMessageSuccess(
                                        response.body().getStatusMessageJoined()
                                );
                                return voucherDataResponseTransform;
                            } else {
                                throw new RuntimeException(new ResponseErrorException(
                                        response.body().getErrorMessageJoined()
                                ));
                            }
                        }
                        throw new RuntimeException(new HttpErrorException(response.code()));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void unSubscribeObservable() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }

    @NonNull
    private Func1<ResponseTransform<CartModel>, Observable<ResponseTransform<CartModel>>>
    funcTransformFromGetCartInfo(final TKPDMapParam<String, String> paramCartInfo) {
        return new Func1<ResponseTransform<CartModel>, Observable<ResponseTransform<CartModel>>>() {
            @Override
            public Observable<ResponseTransform<CartModel>> call(
                    ResponseTransform<CartModel> cartModelResponseTransform
            ) {
                return Observable.zip(Observable.just(cartModelResponseTransform),
                        txService.getApi().doPayment(paramCartInfo),
                        funcZipResponseTransformFromResponseCartInfo());
            }
        };
    }

    @NonNull
    private Func2<ResponseTransform<CartModel>, Response<TkpdResponse>,
            ResponseTransform<CartModel>> funcZipResponseTransformFromResponseCartInfo() {
        return new Func2<ResponseTransform<CartModel>, Response<TkpdResponse>,
                ResponseTransform<CartModel>>() {
            @Override
            public ResponseTransform<CartModel> call(
                    ResponseTransform<CartModel> cartModelResponseTransform,
                    Response<TkpdResponse> response
            ) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        cartModelResponseTransform.setData(
                                response.body().convertDataObj(CartModel.class)
                        );
                        return cartModelResponseTransform;
                    } else {
                        throw new RuntimeException(response.body().getErrorMessages().get(0));
                    }
                } else {
                    handleErrorHttpError(response);
                }
                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        };
    }

    @NonNull
    private Func1<Response<TkpdResponse>,
            ResponseTransform<CartModel>> funcTransformFromUpdateDeleteActionCart() {
        return new Func1<Response<TkpdResponse>, ResponseTransform<CartModel>>() {
            @Override
            public ResponseTransform<CartModel> call(Response<TkpdResponse> response) {
                if (response.isSuccessful() && !response.body().isError()
                        && !response.body().isNullData()) {
                    if (!response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                        try {
                            int status = response.body().getJsonData()
                                    .getInt(KEY_FLAG_IS_SUCCESS);
                            String message = status == 1 ? response.body()
                                    .getStatusMessages().get(0)
                                    : response.body().getErrorMessages().get(0);
                            switch (status) {
                                case 1:
                                    ResponseTransform<CartModel> responseTransform
                                            = new ResponseTransform<>();
                                    responseTransform.setMessageSuccess(
                                            response.body().getStatusMessageJoined()
                                    );
                                    return responseTransform;
                                default:
                                    throw new RuntimeException(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                    throw new RuntimeException("");
                }
                throw new RuntimeException("");
            }
        };
    }

    private void handleErrorHttpError(Response<TkpdResponse> response) {
        new ErrorHandler(new ErrorListener() {
            @Override
            public void onUnknown() {
                throw new RuntimeException(
                        new HttpErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
                );
            }

            @Override
            public void onTimeout() {
                throw new RuntimeException(
                        ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                );
            }

            @Override
            public void onServerError() {
                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onBadRequest() {
                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onForbidden() {
                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        }, response.code());
    }


}
