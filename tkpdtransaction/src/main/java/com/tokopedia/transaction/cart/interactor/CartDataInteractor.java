package com.tokopedia.transaction.cart.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.apiservices.transaction.TXCartActService;
import com.tokopedia.core.network.apiservices.transaction.TXCartService;
import com.tokopedia.core.network.apiservices.transaction.TXService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentData;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentWrapper;
import com.tokopedia.transaction.cart.model.cartdata.CartModel;

import org.json.JSONException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 11/2/16.
 */

public class CartDataInteractor implements ICartDataInteractor {
    private static final String KEY_FLAG_IS_SUCCESS = "is_success";

    private final TXService txService;
    private final TXCartActService txCartActService;
    private final CompositeSubscription compositeSubscription;

    public CartDataInteractor() {
        this.compositeSubscription = new CompositeSubscription();
        this.txService = new TXService();
        this.txCartActService = new TXCartActService();
    }

    @Override
    public void calculateCart(TKPDMapParam<String, String> param, Subscriber<Object> subscriber) {

    }

    @Override
    public void getCartData(TKPDMapParam<String, String> param, Subscriber<CartModel> subscriber) {
        Observable<Response<TkpdResponse>> observable = txService.getApi().doPayment(param);
        compositeSubscription.add(observable
                .map(new Func1<Response<TkpdResponse>, CartModel>() {
                    @Override
                    public CartModel call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                return response.body().convertDataObj(CartModel.class);
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
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public void cancelCart(TKPDMapParam<String, String> paramCancelCart,
                           final TKPDMapParam<String, String> paramCartInfo,
                           Subscriber<CartModel> subscriber) {
        Observable<Response<TkpdResponse>> observable = txCartActService.getApi().cancelCart(paramCancelCart);

        compositeSubscription.add(observable
                .flatMap(new Func1<Response<TkpdResponse>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(Response<TkpdResponse> response) {
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
                                            return txService.getApi().doPayment(paramCartInfo);
                                        default:
                                            throw new RuntimeException("Gagal");
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
                })
                .map(new Func1<Response<TkpdResponse>, CartModel>() {
                    @Override
                    public CartModel call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                return response.body().convertDataObj(CartModel.class);
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
                .subscribe(subscriber)
        );
    }

    @Override
    public void calculateShipment(CalculateShipmentWrapper wrapper, Subscriber<CalculateShipmentData> subscriber) {
        Observable.just(wrapper)
                .flatMap(new Func1<CalculateShipmentWrapper, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(CalculateShipmentWrapper wrapper) {
                        TXCartService service = new TXCartService();
                        Observable<Response<TkpdResponse>> observableNetwork = service
                                .getApi()
                                .calculateCart(wrapper.getParams());

                        return observableNetwork;
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
                           TKPDMapParam<String, String> paramCart, Subscriber<CartModel> subscriber) {

    }

    @NonNull
    private Func2<CalculateShipmentWrapper, Response<TkpdResponse>, CalculateShipmentWrapper> transformShipmentData() {
        return new Func2<CalculateShipmentWrapper, Response<TkpdResponse>, CalculateShipmentWrapper>() {
            @Override
            public CalculateShipmentWrapper call(CalculateShipmentWrapper wrapper, Response<TkpdResponse> response) {
                wrapper.setData(response.body().convertDataObj(CalculateShipmentData.class));
                return wrapper;
            }
        };
    }
}
