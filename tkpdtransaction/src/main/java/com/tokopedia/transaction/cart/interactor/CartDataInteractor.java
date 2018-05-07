package com.tokopedia.transaction.cart.interactor;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.core.network.apiservices.logistics.LogisticsAuthService;
import com.tokopedia.core.network.apiservices.transaction.TXActService;
import com.tokopedia.core.network.apiservices.transaction.TXCartActService;
import com.tokopedia.core.network.apiservices.transaction.TXService;
import com.tokopedia.core.network.apiservices.transaction.TXVoucherService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.addtocart.model.kero.Data;
import com.tokopedia.transaction.addtocart.model.kero.LogisticsData;
import com.tokopedia.transaction.addtocart.utils.KeroppiParam;
import com.tokopedia.transaction.cart.interactor.data.ShipmentCartDataRepository;
import com.tokopedia.transaction.cart.interactor.domain.IShipmentCartRepository;
import com.tokopedia.transaction.cart.model.ResponseTransform;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.cartdata.CartData;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.CartRatesData;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;
import com.tokopedia.transaction.cart.model.shipmentcart.EditShipmentCart;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.model.voucher.VoucherData;
import com.tokopedia.transaction.exception.HttpErrorException;
import com.tokopedia.transaction.exception.ResponseErrorException;
import com.tokopedia.transaction.network.VoucherCartService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * collabs with alvarisi
 */
public class CartDataInteractor implements ICartDataInteractor {
    private static final String KEY_FLAG_IS_SUCCESS = "is_success";
    private static final String COUPON = "coupon";
    private static final String DATA = "data";
    private static final String SUCCESS = "success";

    private final TXService txService;
    private final TXActService txActService;
    private final TXCartActService txCartActService;
    private final TXVoucherService txVoucherService;
    private final CompositeSubscription compositeSubscription;
    private final KeroAuthService keroAuthService;
    private final VoucherCartService voucherCartService;

    private IShipmentCartRepository shipmentCartRepository;
    private LogisticsAuthService logisticsAuthService;

    public CartDataInteractor() {
        this.compositeSubscription = new CompositeSubscription();
        this.txService = new TXService();
        this.txCartActService = new TXCartActService();
        this.txActService = new TXActService();
        this.txVoucherService = new TXVoucherService();
        this.shipmentCartRepository = new ShipmentCartDataRepository();
        this.keroAuthService = new KeroAuthService(3);
        this.logisticsAuthService = new LogisticsAuthService();
        this.voucherCartService = new VoucherCartService();
    }

    @Override
    public void calculateCart(TKPDMapParam<String, String> param,
                              Subscriber<Object> subscriber) {

    }

    @Override
    public void getCartData(TKPDMapParam<String, String> param,
                            Subscriber<ResponseTransform<CartData>> subscriber) {
        compositeSubscription.add(Observable.just(new ResponseTransform<CartData>())
                .flatMap(funcTransformFromGetCartInfo(param))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void cancelCart(final TKPDMapParam<String, String> paramCancelCart,
                           final TKPDMapParam<String, String> paramCartInfo,
                           Subscriber<ResponseTransform<CartData>> subscriber) {
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
                                  Subscriber<List<Shipment>> subscriber) {
        shipmentCartRepository.shipments(param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void updateCart(TKPDMapParam<String, String> paramUpdate,
                           final TKPDMapParam<String, String> paramCart,
                           Subscriber<ResponseTransform<CartData>> subscriber) {
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
                                    Subscriber<ResponseTransform<CartData>> subscriber) {
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
    public void getThanksTopPay(TKPDMapParam<String, String> params,
                                Scheduler schedulers, Subscriber<ThanksTopPayData> subscriber) {
        Observable<Response<TkpdResponse>> observable
                = txActService.getApi().getThanksDynamicPayment(params);
        compositeSubscription.add(observable
                .flatMap(new Func1<Response<TkpdResponse>, Observable<ThanksTopPayData>>() {
                    @Override
                    public Observable<ThanksTopPayData> call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                return Observable.just(response.body().convertDataObj(
                                        ThanksTopPayData.class)
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
                .subscribeOn(schedulers)
                .observeOn(schedulers)
                .unsubscribeOn(schedulers)
                .subscribe(subscriber)
        );
    }

    @Override
    public void editShipmentCart(TKPDMapParam<String, String> param,
                                 Subscriber<EditShipmentCart> subscriber) {
        shipmentCartRepository.editShipment(param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void editLocationShipment(TKPDMapParam<String, String> param,
                                     Subscriber<SaveLocationData> subscriber) {
        shipmentCartRepository.editLocation(param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void checkVoucherCode(TKPDMapParam<String, String> param,
                                 Subscriber<ResponseTransform<VoucherData>> subscriber) {
        compositeSubscription.add(Observable.just(param)
                .flatMap(new Func1<TKPDMapParam<String, String>,
                        Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>>
                    call(TKPDMapParam<String, String> param) {
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
                        throw new RuntimeException(
                                new HttpErrorException(response.code())
                        );
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void cancelVoucherCache(Context context, Subscriber<Boolean> subscriber) {
        compositeSubscription.add(voucherCartService.getApi()
                .checkVoucherCode(AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE,
                        AuthUtil.generateParamsNetwork(context, new TKPDMapParam<String, String>()))
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            return jsonResponse.getJSONObject(DATA).getBoolean(SUCCESS);
                        } catch (JSONException | NullPointerException e) {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber)
        );
    }

    @Override
    public void unSubscribeObservable() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }

    @NonNull
    private Func1<ResponseTransform<CartData>, Observable<ResponseTransform<CartData>>>
    funcTransformFromGetCartInfo(final TKPDMapParam<String, String> paramCartInfo) {
        return new Func1<ResponseTransform<CartData>, Observable<ResponseTransform<CartData>>>() {
            @Override
            public Observable<ResponseTransform<CartData>> call(
                    ResponseTransform<CartData> cartModelResponseTransform
            ) {
                return Observable.zip(Observable.just(cartModelResponseTransform),
                        txService.getApi().doPayment(paramCartInfo),
                        funcZipResponseTransformFromResponseCartInfo());
            }
        };
    }

    @NonNull
    private Func2<ResponseTransform<CartData>, Response<TkpdResponse>,
            ResponseTransform<CartData>> funcZipResponseTransformFromResponseCartInfo() {
        return new Func2<ResponseTransform<CartData>, Response<TkpdResponse>,
                ResponseTransform<CartData>>() {
            @Override
            public ResponseTransform<CartData> call(
                    ResponseTransform<CartData> cartModelResponseTransform,
                    Response<TkpdResponse> response
            ) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        cartModelResponseTransform.setData(
                                response.body().convertDataObj(CartData.class)
                        );
                        return cartModelResponseTransform;
                    } else {
                        throw new RuntimeException(
                                new ResponseErrorException(response.body().getErrorMessageJoined())
                        );
                    }
                } else {
                    throw new RuntimeException(
                            new HttpErrorException(response.code())
                    );
                }
            }
        };
    }

    @NonNull
    private Func1<Response<TkpdResponse>, ResponseTransform<CartData>>
    funcTransformFromUpdateDeleteActionCart() {
        return new Func1<Response<TkpdResponse>, ResponseTransform<CartData>>() {
            @Override
            public ResponseTransform<CartData> call(Response<TkpdResponse> response) {

                if (response.isSuccessful()) {
                    TkpdResponse tkpdResponse = response.body();
                    if (tkpdResponse.isError()) throw new RuntimeException(
                            new ResponseErrorException(tkpdResponse.getErrorMessageJoined())
                    );
                    if (!tkpdResponse.getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                        try {
                            int status = tkpdResponse.getJsonData()
                                    .getInt(KEY_FLAG_IS_SUCCESS);
                            String message = status == 1 ? tkpdResponse.getErrorMessageJoined()
                                    : tkpdResponse.getErrorMessageJoined();
                            switch (status) {
                                case 1:
                                    ResponseTransform<CartData> responseTransform
                                            = new ResponseTransform<>();
                                    responseTransform.setMessageSuccess(
                                            tkpdResponse.getStatusMessageJoined()
                                    );
                                    return responseTransform;
                                default:
                                    throw new RuntimeException(new ResponseErrorException(message));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new RuntimeException(
                                    new ResponseErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
                            );
                        }
                    } else {
                        throw new RuntimeException(
                                new ResponseErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
                        );
                    }
                } else {
                    throw new RuntimeException(
                            new HttpErrorException(response.code())
                    );
                }
            }
        };
    }

    @Override
    public void calculateKeroRates(String token,
                                   String ut,
                                   final List<CartItem> cartItemList,
                                   KeroRatesListener listener, Context context) {

        List<Observable<CartRatesData>> cartItemObservableList = new ArrayList<>();
        for (int i = 0; i < cartItemList.size(); i++) {
            if (isValidated(cartItemList, i)) {
                cartItemObservableList.add(cartItemObservable(token, ut, cartItemList.get(i), context)
                        .flatMap(engineeredResposne(i, cartItemList)
                        ));
            }
        }
        if (cartItemObservableList.size() > 0) {

            Observable<CartRatesData> multipleRequest = Observable.merge(cartItemObservableList);
            compositeSubscription.add(multipleRequest.subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(responseList(listener)));
        } else {
            listener.onRatesFailed(ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT);
        }
    }

    private boolean isValidated(List<CartItem> cartItemList, int i) {
        return cartItemList.get(i).getCartTotalError() < 1
                && (cartItemList.get(i).getCartErrorMessage1().isEmpty()
                || cartItemList.get(i).getCartErrorMessage1().equals("0"))
                && (cartItemList.get(i).getCartErrorMessage2().isEmpty()
                || cartItemList.get(i).getCartErrorMessage2().equals("0"));
    }

    private Func1<Response<String>, Observable<CartRatesData>> engineeredResposne(
            final int cartItemIndex,
            final List<CartItem> cartItemList) {
        return new Func1<Response<String>, Observable<CartRatesData>>() {
            @Override
            public Observable<CartRatesData> call(Response<String> stringResponse) {

                CartRatesData cartRatesData = new CartRatesData();
                cartRatesData.setRatesIndex(cartItemIndex);
                setPreRatesData(cartRatesData, cartItemList.get(cartItemIndex));
                LogisticsData logisticsData = new LogisticsData();
                if (stringResponse.isSuccessful()) {
                    logisticsData = new Gson().fromJson(stringResponse.body(), LogisticsData.class);

                    if (logisticsData.getOngkirData() != null)
                        cartRatesData.setRatesResponse(CacheUtil.convertModelToString(logisticsData.getOngkirData().getOngkir().getData(),
                                new TypeToken<Data>() {
                                }.getType()));
                    cartRatesData.setErrorResponse("");
                } else {

                    try {
                        logisticsData = new Gson()
                                .fromJson(stringResponse.errorBody().string(), LogisticsData.class);
                        cartRatesData.setErrorResponse(logisticsData.getLogisticsError().get(0).getMessage());
                        cartRatesData.setRatesResponse("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return Observable.just(cartRatesData);
            }
        };
    }

    private void setPreRatesData(CartRatesData cartRatesData, CartItem cartItem) {
        cartRatesData.setCourierServiceId(Integer.parseInt(cartItem
                .getCartShipments()
                .getShipmentPackageId()));
        cartRatesData.setCartTotalProductPrice(Integer.parseInt(cartItem
                .getCartTotalProductPrice()));
        cartRatesData.setCartAdditionalLogisticFee(Integer
                .parseInt(cartItem.getCartLogisticFee()));
        cartRatesData.setKeroRatesKey(cartItem.getCartRatesString());
        cartRatesData.setInsuranced(isProductUseInsurance(cartItem));
    }

    private Subscriber<CartRatesData> responseList(final KeroRatesListener keroRatesListener) {
        return new Subscriber<CartRatesData>() {
            @Override
            public void onCompleted() {
                keroRatesListener.onAllDataCompleted();
            }

            @Override
            public void onError(Throwable e) {
                keroRatesListener.onRatesFailed(e.getMessage());
            }

            @Override
            public void onNext(CartRatesData cartRatesData) {
                keroRatesListener.onSuccess(cartRatesData);
            }
        };
    }

    private Observable<Response<String>> cartItemObservable(String token, String ut,
                                                            CartItem cartItem, Context context) {

        TKPDMapParam<String, String> params = KeroppiParam.paramsKeroCart(token, ut, cartItem);


        return logisticsAuthService.getApi().getLogisticsData(getRequestPayload(context,
                AuthUtil.generateParamsNetwork(context, params)));
    }

    private String getRequestPayload(Context context, TKPDMapParam<String, String> params) {

        return String.format(
                loadRawString(context.getResources(), R.raw.logistics_get_courier_query),
                params.get(KeroppiParam.CAT_ID),
                params.get(KeroppiParam.DESTINATION),
                params.get(KeroppiParam.FROM),
                params.get(KeroppiParam.INSURANCE),
                params.get(KeroppiParam.NAMES),
                params.get(KeroppiParam.ORDER_VALUE),
                params.get(KeroppiParam.ORIGIN),
                params.get(KeroppiParam.PRODUCT_INSURANCE),
                params.get(KeroppiParam.TOKEN),
                params.get(KeroppiParam.UT),
                params.get(KeroppiParam.WEIGHT),
                params.get(KeroppiParam.PARAM_OS_TYPE));
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }

    private boolean isProductUseInsurance(CartItem cartItem) {
        for (CartProduct cartProduct : cartItem.getCartProducts()) {
            if (cartProduct.getProductMustInsurance().equals("1") ||
                    cartProduct.getProductUseInsurance() == 1) {
                return true;
            }
        }
        return false;
    }

}
