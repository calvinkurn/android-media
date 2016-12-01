package com.tokopedia.transaction.cart.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.apiservices.transaction.TXActService;
import com.tokopedia.core.network.apiservices.transaction.TXCartActService;
import com.tokopedia.core.network.apiservices.transaction.TXService;
import com.tokopedia.core.network.apiservices.user.PeopleActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.interactor.entity.EditShipmentEntity;
import com.tokopedia.transaction.cart.interactor.entity.ShipmentEntity;
import com.tokopedia.transaction.cart.interactor.entity.mapper.ShipmentEntityDataMapper;
import com.tokopedia.transaction.cart.interactor.source.CloudShipmentCartSource;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.cartdata.CartModel;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;
import com.tokopedia.transaction.cart.model.shipmentcart.EditShipmentCart;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.exception.HttpErrorException;
import com.tokopedia.transaction.exception.ResponseErrorException;

import org.json.JSONException;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
    private final CompositeSubscription compositeSubscription;

    private CloudShipmentCartSource cloudSource;
    private ShipmentEntityDataMapper mapper;

    public CartDataInteractor() {
        this.compositeSubscription = new CompositeSubscription();
        this.txService = new TXService();
        this.txCartActService = new TXCartActService();
        this.txActService = new TXActService();
        this.cloudSource = new CloudShipmentCartSource();
        this.mapper = new ShipmentEntityDataMapper();
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
    public void cancelCart(final TKPDMapParam<String, String> paramCancelCart,
                           final TKPDMapParam<String, String> paramCartInfo,
                           Subscriber<CartModel> subscriber) {
        Observable<Response<TkpdResponse>> observable
                = txCartActService.getApi().cancelCart(paramCancelCart);

        compositeSubscription.add(observable
                .flatMap(new Func1<Response<TkpdResponse>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(Response<TkpdResponse> response) {
                        return getResponseObservableUpdateDelete(response, paramCartInfo);
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
    public void updateCart(TKPDMapParam<String, String> paramUpdate,
                           final TKPDMapParam<String, String> paramCart,
                           Subscriber<CartModel> subscriber) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .editCart(paramUpdate);
        compositeSubscription.add(observable
                .flatMap(new Func1<Response<TkpdResponse>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(Response<TkpdResponse> response) {
                        return getResponseObservableUpdateDelete(response, paramCart);
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
                .subscribe(subscriber));
    }

    @Override
    public void updateInsuranceCart(TKPDMapParam<String, String> paramUpdate,
                                    final TKPDMapParam<String, String> paramCart,
                                    Subscriber<CartModel> subscriber) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .editInsurance(paramUpdate);
        compositeSubscription.add(observable
                .flatMap(new Func1<Response<TkpdResponse>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(Response<TkpdResponse> response) {
                        return getResponseObservableUpdateDelete(response, paramCart);
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

    @NonNull
    private Observable<Response<TkpdResponse>> getResponseObservableUpdateDelete(
            Response<TkpdResponse> response, TKPDMapParam<String, String> paramCart) {
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
                            return txService.getApi().doPayment(paramCart);
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

    @Override
    public void calculateShipment(TKPDMapParam<String, String> param, Subscriber<List<Shipment>> subscriber) {
        cloudSource.shipments(param)
                .map(new Func1<List<ShipmentEntity>, List<Shipment>>() {
                    @Override
                    public List<Shipment> call(List<ShipmentEntity> shipmentEntities) {
                        return mapper.transform(shipmentEntities);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void editShipmentCart(TKPDMapParam<String, String> param, Subscriber<EditShipmentCart> subscriber) {
        cloudSource.editShipment(param)
                .map(new Func1<EditShipmentEntity, EditShipmentCart>() {
                    @Override
                    public EditShipmentCart call(EditShipmentEntity editShipmentEntity) {
                        return mapper.transform(editShipmentEntity);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void editLocationShipment(TKPDMapParam<String, String> param, Subscriber<SaveLocationData> subscriber) {
        cloudSource.editLocation(param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void unSubscribeObservable() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }
}
