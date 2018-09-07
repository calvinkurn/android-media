package com.tokopedia.seller.selling;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.seller.selling.model.ResponseConfirmShipping;
import com.tokopedia.seller.selling.constant.shopshippingdetail.ShopShippingDetailView;
import com.tokopedia.core.R;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.seller.selling.constant.SellingServiceConstant;
import com.tokopedia.seller.selling.model.ModelParamSelling;
import com.tokopedia.seller.selling.model.modelConfirmShipping.Data;
import com.tokopedia.seller.selling.model.orderShipping.OrderProduct;
import com.tokopedia.seller.selling.orderReject.fragment.ConfirmRejectOrderFragment;
import com.tokopedia.seller.selling.orderReject.model.DataResponseReject;
import com.tokopedia.seller.selling.orderReject.model.ModelEditDescription;
import com.tokopedia.seller.selling.orderReject.model.ModelEditPrice;
import com.tokopedia.seller.selling.orderReject.model.ModelRejectOrder;
import com.tokopedia.seller.selling.orderReject.model.ResponseEditDescription;
import com.tokopedia.seller.selling.orderReject.model.ResponseEditPrice;
import com.tokopedia.seller.selling.orderReject.model.ResponseGetProductForm;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.selling.network.apiservices.MyShopOrderActService;
import com.tokopedia.seller.selling.network.apiservices.ProductActService;
import com.tokopedia.seller.selling.network.apiservices.ProductService;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 * <p>
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class SellingService extends IntentService implements SellingServiceConstant {
    // TODO: Rename actions, choose action names that describe tasks that this

    public static final String TAG = "MNORMANSYAH";
    public static final String messageTAG = SellingService.class.getSimpleName() + " ";

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.tokopedia.tkpd.selling.action.FOO";
    public static final String ACTION_BAZ = "com.tokopedia.tkpd.selling.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.tokopedia.tkpd.selling.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.tokopedia.tkpd.selling.extra.PARAM2";

    private AuthService service;
    private Gson gson;
    private ResultReceiver receiver;
    private LocalCacheHandler cache;
    SessionHandler sessionHandler;

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public SellingService() {
        super("SellingService");
    }

    public static void startSellingService(Context context, DownloadResultReceiver receiver, Bundle bundle, int type) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, SellingService.class);
        intent.putExtra(SellingService.RECEIVER, receiver);

        // set mandatory param
        intent.putExtra(TYPE, type);
        /* Send optional extras to Download IntentService */
        switch (type) {
            case GET_PRODUCT_FORM_EDIT_CLOSED:
            case GET_PRODUCT_FORM_EDIT:
                List<OrderProduct> orderProducts = Parcels.unwrap(bundle.getParcelable(ConfirmRejectOrderFragment.PRODUCT_DETAIL_KEY));
                intent.putExtra(ConfirmRejectOrderFragment.PRODUCT_DETAIL_KEY, Parcels.wrap(orderProducts));
                break;
            case REJECT_ORDER_WITH_REASON:
            case REJECT_ORDER_CLOSE_SHOP:
            case REJECT_ORDER:
                ModelRejectOrder modelRejectOrder = Parcels.unwrap(bundle.getParcelable(ModelRejectOrder.MODEL_REJECT_ORDER_KEY));
                intent.putExtra(ModelRejectOrder.MODEL_REJECT_ORDER_KEY, Parcels.wrap(modelRejectOrder));
                break;
            case REJECT_ORDER_WITH_DESCRIPTION:
                List<ModelEditDescription> modelEditDescriptions = Parcels.unwrap(bundle.getParcelable(ModelEditDescription.MODEL_EDIT_DESCRIPTION_KEY));
                modelRejectOrder = Parcels.unwrap(bundle.getParcelable(ModelRejectOrder.MODEL_REJECT_ORDER_KEY));
                intent.putExtra(ModelRejectOrder.MODEL_REJECT_ORDER_KEY, Parcels.wrap(modelRejectOrder));
                intent.putExtra(ModelEditDescription.MODEL_EDIT_DESCRIPTION_KEY, Parcels.wrap(modelEditDescriptions));
                break;
            case REJECT_ORDER_WITH_PRICE:
                List<ModelEditPrice> modelEditPrices = Parcels.unwrap(bundle.getParcelable(ModelEditPrice.MODEL_EDIT_PRICE_KEY));
                modelRejectOrder = Parcels.unwrap(bundle.getParcelable(ModelRejectOrder.MODEL_REJECT_ORDER_KEY));
                intent.putExtra(ModelRejectOrder.MODEL_REJECT_ORDER_KEY, Parcels.wrap(modelRejectOrder));
                intent.putExtra(ModelEditPrice.MODEL_EDIT_PRICE_KEY, Parcels.wrap(modelEditPrices));
                break;
            case CONFIRM_MULTI_SHIPPING:
                List<ModelParamSelling> modelParamSellings = Parcels.unwrap(bundle.getParcelable(MODEL_PARAM_SELLING_KEY));
                intent.putExtra(MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSellings));
                break;
            case CONFIRM_SHIPPING:
            case CANCEL_SHIPPING:
            case CONFIRM_NEW_ORDER:
            case PARTIAL_NEW_ORDER:
                ModelParamSelling modelParamSelling = Parcels.unwrap(bundle.getParcelable(MODEL_PARAM_SELLING_KEY));
                intent.putExtra(MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSelling));
                break;
            default:
                throw new RuntimeException("unknown type for starting download !!!");
        }

        context.startService(intent);
    }

    /**
     * start selling service without download receiver either because
     * use broadcast receiver from local broadcast manager
     *
     * @param context
     * @param bundle
     * @param type
     */
    public static void startSellingService(Context context, Bundle bundle, int type) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, SellingService.class);

        // set mandatory param
        intent.putExtra(TYPE, type);
        /* Send optional extras to Download IntentService */
        switch (type) {
            case REJECT_ORDER_WITH_REASON:
                ModelRejectOrder modelRejectOrder = Parcels.unwrap(bundle.getParcelable(ModelRejectOrder.MODEL_REJECT_ORDER_KEY));
                intent.putExtra(ModelRejectOrder.MODEL_REJECT_ORDER_KEY, Parcels.wrap(modelRejectOrder));
                break;
            case CONFIRM_MULTI_SHIPPING:
                List<ModelParamSelling> modelParamSellings = Parcels.unwrap(bundle.getParcelable(MODEL_PARAM_SELLING_KEY));
                intent.putExtra(MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSellings));
                break;
            case CONFIRM_SHIPPING:
            case CANCEL_SHIPPING:
            case CONFIRM_NEW_ORDER:
            case REJECT_NEW_ORDER:
            case PARTIAL_NEW_ORDER:
                ModelParamSelling modelParamSelling = Parcels.unwrap(bundle.getParcelable(MODEL_PARAM_SELLING_KEY));
                intent.putExtra(MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSelling));
                break;
            default:
                throw new RuntimeException("unknown type for starting download !!!");
        }

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }

        receiver = intent.getParcelableExtra(RECEIVER);
        int type = intent.getIntExtra(TYPE, INVALID_TYPE);
        gson = new GsonBuilder().create();
        sessionHandler = new SessionHandler(getApplicationContext());

        switch (type) {
            case REJECT_ORDER_WITH_REASON:
                Bundle running = new Bundle();
                running.putInt(TYPE, type);
                ModelRejectOrder modelRejectOrder = Parcels.unwrap(intent.getParcelableExtra(ModelRejectOrder.MODEL_REJECT_ORDER_KEY));
                running.putInt(ShopShippingDetailView.POSITION, modelRejectOrder.getPosition());
                receiver.send(STATUS_RUNNING, running);
//                sendBroadcast(STATUS_RUNNING, running);
                Map<String, String> params = new HashMap<String, String>();
                params.put(ModelRejectOrder.ACTION_TYPE, modelRejectOrder.getAction_type());
                params.put(ModelRejectOrder.EST_SHIPPING, modelRejectOrder.getEst_shipping());
                params.put(ModelRejectOrder.LIST_PRODUCT_ID, modelRejectOrder.getList_product_id());
                params.put(ModelRejectOrder.ORDER_ID, modelRejectOrder.getOrder_id());
                params.put(ModelRejectOrder.QTY_ACCEPT, modelRejectOrder.getQty_accept());
                params.put(ModelRejectOrder.REASON, modelRejectOrder.getReason());
                params.put(ModelRejectOrder.CLOSED_NOTE, modelRejectOrder.getClosed_note());
                params.put(ModelRejectOrder.CLOSE_END, modelRejectOrder.getClose_end());
                params.put(ModelRejectOrder.REASON_CODE, modelRejectOrder.getReason_code());

                service = new MyShopOrderActService();
                ((MyShopOrderActService) service).getApi().proceedOrder(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type, modelRejectOrder.getPosition()));
                break;
            case REJECT_ORDER_CLOSE_SHOP:
            case REJECT_ORDER:
                running = new Bundle();
                running.putInt(TYPE, type);
                modelRejectOrder = Parcels.unwrap(intent.getParcelableExtra(ModelRejectOrder.MODEL_REJECT_ORDER_KEY));
                receiver.send(STATUS_RUNNING, running);
                params = new HashMap<String, String>();
                params.put(ModelRejectOrder.ACTION_TYPE, modelRejectOrder.getAction_type());
                params.put(ModelRejectOrder.EST_SHIPPING, modelRejectOrder.getEst_shipping());
                params.put(ModelRejectOrder.LIST_PRODUCT_ID, modelRejectOrder.getList_product_id());
                params.put(ModelRejectOrder.ORDER_ID, modelRejectOrder.getOrder_id());
                params.put(ModelRejectOrder.QTY_ACCEPT, modelRejectOrder.getQty_accept());
                params.put(ModelRejectOrder.REASON, modelRejectOrder.getReason());
                params.put(ModelRejectOrder.CLOSED_NOTE, modelRejectOrder.getClosed_note());
                params.put(ModelRejectOrder.CLOSE_END, modelRejectOrder.getClose_end());
                params.put(ModelRejectOrder.REASON_CODE, modelRejectOrder.getReason_code());

                service = new MyShopOrderActService();
                ((MyShopOrderActService) service).getApi().proceedOrder(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;
            case REJECT_ORDER_WITH_DESCRIPTION:
                running = new Bundle();
                running.putInt(TYPE, type);
                modelRejectOrder = Parcels.unwrap(intent.getParcelableExtra(ModelRejectOrder.MODEL_REJECT_ORDER_KEY));
                List<ModelEditDescription> modelEditDescriptions = Parcels.unwrap(intent.getParcelableExtra(ModelEditDescription.MODEL_EDIT_DESCRIPTION_KEY));
                receiver.send(STATUS_RUNNING, running);
                rejectOrderWithDescription(modelEditDescriptions, modelRejectOrder, type);
                break;
            case REJECT_ORDER_WITH_PRICE:
                running = new Bundle();
                running.putInt(TYPE, type);
                modelRejectOrder = Parcels.unwrap(intent.getParcelableExtra(ModelRejectOrder.MODEL_REJECT_ORDER_KEY));
                List<ModelEditPrice> modelEditPrices = Parcels.unwrap(intent.getParcelableExtra(ModelEditPrice.MODEL_EDIT_PRICE_KEY));
                receiver.send(STATUS_RUNNING, running);
                rejectOrderWithPrice(modelEditPrices, modelRejectOrder, type);
                break;
            case CONFIRM_SHIPPING:
                /* Update UI: Download CourierService is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                ModelParamSelling modelParamSelling = Parcels.unwrap(intent.getParcelableExtra(MODEL_PARAM_SELLING_KEY));
                running.putInt(ShopShippingDetailView.POSITION, modelParamSelling.getPosition());
                receiver.send(STATUS_RUNNING, running);
                params = new HashMap<String, String>();
                params.put(ModelParamSelling.ACTION_TYPE, modelParamSelling.getActionType());
                params.put(ModelParamSelling.ORDER_ID, modelParamSelling.getOrderId());
                params.put(ModelParamSelling.SHIPPING_REF, modelParamSelling.getRefNum());
                if (modelParamSelling.getShipmentId() != null && !modelParamSelling.getShipmentId().equals("")) {
                    params.put(ModelParamSelling.SHIPMENT_ID, modelParamSelling.getShipmentId());
                    params.put(ModelParamSelling.SHIPMENT_NAME, modelParamSelling.getShipmentName());
                    params.put(ModelParamSelling.SP_ID, modelParamSelling.getSpId());
                }

                service = new MyShopOrderActService();
                ((MyShopOrderActService) service).getApi().proceedShipping(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type, modelParamSelling.getPosition()));
                break;
            case CANCEL_SHIPPING:
                /* Update UI: Download CourierService is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                modelParamSelling = Parcels.unwrap(intent.getParcelableExtra(MODEL_PARAM_SELLING_KEY));
                running.putInt(ShopShippingDetailView.POSITION, modelParamSelling.getPosition());
                receiver.send(STATUS_RUNNING, running);
                params = new HashMap<String, String>();
                params.put(ModelParamSelling.ACTION_TYPE, modelParamSelling.getActionType());
                params.put(ModelParamSelling.ORDER_ID, modelParamSelling.getOrderId());
                params.put(ModelParamSelling.REASON, modelParamSelling.getReason());

                service = new MyShopOrderActService();
                ((MyShopOrderActService) service).getApi().proceedShipping(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type, modelParamSelling.getPosition()));
                break;
            case CONFIRM_MULTI_SHIPPING:
                running = new Bundle();
                running.putInt(TYPE, type);
                List<ModelParamSelling> modelParamSellings = Parcels.unwrap(intent.getParcelableExtra(MODEL_PARAM_SELLING_KEY));
                receiver.send(STATUS_RUNNING, running);

                multiConfirmShipping(modelParamSellings, type);
                break;
            case CONFIRM_NEW_ORDER:
                /* Update UI: Download CourierService is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                modelParamSelling = Parcels.unwrap(intent.getParcelableExtra(MODEL_PARAM_SELLING_KEY));
                running.putInt(ShopShippingDetailView.POSITION, modelParamSelling.getPosition());
                receiver.send(STATUS_RUNNING, running);
//                sendBroadcast(STATUS_RUNNING, running);
                params = new HashMap<String, String>();
                params.put(ModelParamSelling.ACTION_TYPE, modelParamSelling.getActionType());
                params.put(ModelParamSelling.ORDER_ID, modelParamSelling.getOrderId());

                service = new MyShopOrderActService();
                ((MyShopOrderActService) service).getApi().proceedOrder(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type, modelParamSelling.getPosition()));
                break;
            case PARTIAL_NEW_ORDER:
                /* Update UI: Download CourierService is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                modelParamSelling = Parcels.unwrap(intent.getParcelableExtra(MODEL_PARAM_SELLING_KEY));
                running.putInt(ShopShippingDetailView.POSITION, modelParamSelling.getPosition());
                receiver.send(STATUS_RUNNING, running);
//                sendBroadcast(STATUS_RUNNING, running);
                params = new HashMap<String, String>();
                params.put(ModelParamSelling.ACTION_TYPE, modelParamSelling.getActionType());
                params.put(ModelParamSelling.ORDER_ID, modelParamSelling.getOrderId());
                params.put(ModelParamSelling.REASON, modelParamSelling.getReason());
                params.put(ModelParamSelling.QTY_ACCEPT, modelParamSelling.getQtyAccept());

                service = new MyShopOrderActService();
                ((MyShopOrderActService) service).getApi().proceedOrder(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type, modelParamSelling.getPosition()));
                break;
            case GET_PRODUCT_FORM_EDIT_CLOSED:
            case GET_PRODUCT_FORM_EDIT:
                running = new Bundle();
                running.putInt(TYPE, type);
                List<OrderProduct> orderProducts = Parcels.unwrap(intent.getParcelableExtra(ConfirmRejectOrderFragment.PRODUCT_DETAIL_KEY));
                receiver.send(STATUS_RUNNING, running);
                getProductFormEdit(orderProducts, type);
                break;
            default:
                if (intent != null) {
                    final String action = intent.getAction();
                    if (ACTION_FOO.equals(action)) {
                        final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                        final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                        handleActionFoo(param1, param2);
                    } else if (ACTION_BAZ.equals(action)) {
                        final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                        final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                        handleActionBaz(param1, param2);
                    }
                }
                break;
        }
    }

    private void getProductFormEdit(List<OrderProduct> orderProducts, final int type) {
        Observable.from(orderProducts)
                .flatMap(new Func1<OrderProduct, Observable<OrderProduct>>() {
                    @Override
                    public Observable<OrderProduct> call(OrderProduct orderProduct) {

                        final Map<String, String> paramsGetProduct = new HashMap<String, String>();
                        SessionHandler sessionHandler = new SessionHandler(getApplicationContext());
                        paramsGetProduct.put("shop_id", sessionHandler.getShopID());
                        paramsGetProduct.put(ModelEditDescription.PRODUCT_ID, orderProduct.getProductId().toString());

                        ProductService productService = new ProductService();
                        Observable<ResponseGetProductForm> getProductForm = productService.getApi().getEditFormSelling(AuthUtil.generateParams(getApplicationContext(), paramsGetProduct));
                        return Observable.zip(Observable.just(orderProduct), getProductForm, new Func2<OrderProduct, ResponseGetProductForm, OrderProduct>() {
                            @Override
                            public OrderProduct call(OrderProduct orderProduct, ResponseGetProductForm responseGetProductForm) {
                                if (responseGetProductForm != null) {
                                    if (responseGetProductForm.getMessage_error() == null) {
                                        orderProduct.setShopIsGold(responseGetProductForm.getData().getShop_is_gold());
                                        orderProduct.setProductDescription(responseGetProductForm.getData().getProduct().getProduct_short_desc());
                                        orderProduct.setProductNormalPrice(responseGetProductForm.getData().getProduct().getProduct_price());
                                        orderProduct.setProductPriceCurrency(responseGetProductForm.getData().getProduct().getProduct_currency_id());
                                        orderProduct.setProductWeight(responseGetProductForm.getData().getProduct().getProduct_weight());
                                        orderProduct.setProductWeightUnit(responseGetProductForm.getData().getProduct().getProduct_weight_unit());
                                    }
                                    return orderProduct;
                                } else {
                                    throw new RuntimeException("ERROR GENERATE MODEL");
                                }
                            }
                        });
                    }
                })
                .toList()
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<List<OrderProduct>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Bundle resultData = new Bundle();
                        resultData.putInt(TYPE, type);
                        resultData.putInt(NETWORK_ERROR_FLAG, INVALID_NETWORK_ERROR_FLAG);
                        resultData.putString(MESSAGE_ERROR_FLAG, getString(R.string.error_connection_problem));
                        receiver.send(STATUS_ERROR, resultData);
                    }

                    @Override
                    public void onNext(List<OrderProduct> orderProducts) {
                        Bundle result = new Bundle();
                        result.putInt(TYPE, type);
                        result.putParcelable(ConfirmRejectOrderFragment.PRODUCT_DETAIL_KEY, Parcels.wrap(orderProducts));
                        receiver.send(STATUS_FINISHED, result);
                    }
                });
    }

    private void rejectOrderWithDescription(List<ModelEditDescription> modelEditDescriptions, final ModelRejectOrder modelRejectOrder, int type) {
        Observable.from(modelEditDescriptions)
                .flatMap(new Func1<ModelEditDescription, Observable<ModelEditDescription>>() {
                    @Override
                    public Observable<ModelEditDescription> call(ModelEditDescription modelEditDescription) {

                        final Map<String, String> paramsEditDescription = new HashMap<String, String>();
                        paramsEditDescription.put(ModelEditDescription.PRODUCT_DESCRIPTION, modelEditDescription.getProduct_description());
                        paramsEditDescription.put(ModelEditDescription.PRODUCT_ID, modelEditDescription.getProduct_id());

                        ProductActService productActService = new ProductActService();
                        Observable<ResponseEditDescription> editDescription = productActService.getApi().editDescription(AuthUtil.generateParams(getApplicationContext(), paramsEditDescription));
                        return Observable.zip(Observable.just(modelEditDescription), editDescription, new Func2<ModelEditDescription, ResponseEditDescription, ModelEditDescription>() {
                            @Override
                            public ModelEditDescription call(ModelEditDescription modelEditDescription1, ResponseEditDescription responseEditDescription) {
                                if (responseEditDescription != null) {
                                    return modelEditDescription1;
                                } else {
                                    throw new RuntimeException("ERROR GENERATE MODEL");
                                }
                            }
                        });
                    }
                })
                .toList()
                .flatMap(new Func1<List<ModelEditDescription>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(List<ModelEditDescription> modelEditDescriptions1) {
                        final Map<String, String> params = new HashMap<String, String>();
                        params.put(ModelRejectOrder.ACTION_TYPE, modelRejectOrder.getAction_type());
                        params.put(ModelRejectOrder.EST_SHIPPING, modelRejectOrder.getEst_shipping());
                        params.put(ModelRejectOrder.LIST_PRODUCT_ID, modelRejectOrder.getList_product_id());
                        params.put(ModelRejectOrder.ORDER_ID, modelRejectOrder.getOrder_id());
                        params.put(ModelRejectOrder.QTY_ACCEPT, modelRejectOrder.getQty_accept());
                        params.put(ModelRejectOrder.REASON, modelRejectOrder.getReason());
                        params.put(ModelRejectOrder.CLOSED_NOTE, modelRejectOrder.getClosed_note());
                        params.put(ModelRejectOrder.CLOSE_END, modelRejectOrder.getClose_end());
                        params.put(ModelRejectOrder.REASON_CODE, modelRejectOrder.getReason_code());

                        return new MyShopOrderActService().getApi().proceedOrder(AuthUtil.generateParams(getApplicationContext(), params));
                    }
                })
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber(type));
    }

    private void rejectOrderWithPrice(List<ModelEditPrice> modelEditPrices, final ModelRejectOrder modelRejectOrder, int type) {
        Observable.from(modelEditPrices)
                .flatMap(new Func1<ModelEditPrice, Observable<ModelEditPrice>>() {
                    @Override
                    public Observable<ModelEditPrice> call(ModelEditPrice modelEditPrice) {

                        final Map<String, String> paramsEditPrice = new HashMap<String, String>();
                        paramsEditPrice.put(ModelEditPrice.PRODUCT_ID, modelEditPrice.getProduct_id());
                        paramsEditPrice.put(ModelEditPrice.PRODUCT_PRICE, modelEditPrice.getProduct_price());
                        paramsEditPrice.put(ModelEditPrice.PRODUCT_PRICE_CURRENCY, modelEditPrice.getProduct_price_currency());
                        paramsEditPrice.put(ModelEditPrice.PRODUCT_WEIGHT_UNIT, modelEditPrice.getProduct_weight());
                        paramsEditPrice.put(ModelEditPrice.PRODUCT_WEIGHT_VALUE, modelEditPrice.getProduct_weight_value());

                        ProductActService productActService = new ProductActService();
                        Observable<ResponseEditPrice> editPrice = productActService.getApi().editWeightPrice(AuthUtil.generateParams(getApplicationContext(), paramsEditPrice));
                        return Observable.zip(Observable.just(modelEditPrice), editPrice, new Func2<ModelEditPrice, ResponseEditPrice, ModelEditPrice>() {
                            @Override
                            public ModelEditPrice call(ModelEditPrice modelEditPrice1, ResponseEditPrice responseEditPrice) {
                                if (responseEditPrice != null) {
                                    return modelEditPrice1;
                                } else {
                                    throw new RuntimeException("ERROR GENERATE MODEL");
                                }
                            }
                        });
                    }
                })
                .toList()
                .flatMap(new Func1<List<ModelEditPrice>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(List<ModelEditPrice> modelEditPrices) {
                        final Map<String, String> params = new HashMap<String, String>();
                        params.put(ModelRejectOrder.ACTION_TYPE, modelRejectOrder.getAction_type());
                        params.put(ModelRejectOrder.EST_SHIPPING, modelRejectOrder.getEst_shipping());
                        params.put(ModelRejectOrder.LIST_PRODUCT_ID, modelRejectOrder.getList_product_id());
                        params.put(ModelRejectOrder.ORDER_ID, modelRejectOrder.getOrder_id());
                        params.put(ModelRejectOrder.QTY_ACCEPT, modelRejectOrder.getQty_accept());
                        params.put(ModelRejectOrder.REASON, modelRejectOrder.getReason());
                        params.put(ModelRejectOrder.CLOSED_NOTE, modelRejectOrder.getClosed_note());
                        params.put(ModelRejectOrder.CLOSE_END, modelRejectOrder.getClose_end());
                        params.put(ModelRejectOrder.REASON_CODE, modelRejectOrder.getReason_code());

                        return new MyShopOrderActService().getApi().proceedOrder(AuthUtil.generateParams(getApplicationContext(), params));
                    }
                })
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber(type));
    }



    private void multiConfirmShipping(List<ModelParamSelling> orderShipping, final int type) {
        Observable.from(orderShipping)
                .flatMap(new Func1<ModelParamSelling, Observable<ModelParamSelling>>() {
                    @Override
                    public Observable<ModelParamSelling> call(ModelParamSelling orderShipping) {

                        final Map<String, String> paramsConfirmShipping = new HashMap<String, String>();
                        paramsConfirmShipping.put(ModelParamSelling.ACTION_TYPE, orderShipping.getActionType());
                        paramsConfirmShipping.put(ModelParamSelling.ORDER_ID, orderShipping.getOrderId());
                        paramsConfirmShipping.put(ModelParamSelling.SHIPPING_REF, orderShipping.getRefNum());

                        service = new MyShopOrderActService();
                        ((MyShopOrderActService) service).getApi().proceedShipping(AuthUtil.generateParams(getApplicationContext(), paramsConfirmShipping))
                                .subscribeOn(Schedulers.immediate())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber(type));

                        Observable<ResponseConfirmShipping> getProductForm = ((MyShopOrderActService) service).getApi().proceedShippingMulti(AuthUtil.generateParams(getApplicationContext(), paramsConfirmShipping));
                        return Observable.zip(Observable.just(orderShipping), getProductForm, new Func2<ModelParamSelling, ResponseConfirmShipping, ModelParamSelling>() {
                            @Override
                            public ModelParamSelling call(ModelParamSelling modelParamSelling, ResponseConfirmShipping responseConfirmShipping) {
                                if (responseConfirmShipping != null) {
//                                    if (responseConfirmShipping.getMessage_error() == null) {
//                                        orderProduct.setProductDescription(responseConfirmShipping.getData().getProduct().getProduct_short_desc());
//                                        orderProduct.setProductNormalPrice(responseConfirmShipping.getData().getProduct().getProduct_price());
//                                        orderProduct.setProductPriceCurrency(responseConfirmShipping.getData().getProduct().getProduct_currency_id());
//                                        orderProduct.setProductWeight(responseConfirmShipping.getData().getProduct().getProduct_weight());
//                                        orderProduct.setProductWeightUnit(responseConfirmShipping.getData().getProduct().getProduct_weight_unit());
//                                    }
                                    return modelParamSelling;
                                } else {
                                    throw new RuntimeException("ERROR GENERATE MODEL");
                                }
                            }
                        });
                    }
                })
                .toList()
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<List<ModelParamSelling>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Bundle resultData = new Bundle();
                        resultData.putInt(TYPE, type);
                        resultData.putInt(NETWORK_ERROR_FLAG, INVALID_NETWORK_ERROR_FLAG);
                        resultData.putString(MESSAGE_ERROR_FLAG, getString(R.string.error_connection_problem));
                        receiver.send(STATUS_ERROR, resultData);
                    }

                    @Override
                    public void onNext(List<ModelParamSelling> modelParamSellings) {
                        Bundle result = new Bundle();
                        result.putInt(TYPE, type);
                        result.putParcelable(MODEL_CONFIRM_SHIPPING_KEY, Parcels.wrap(modelParamSellings));
                        receiver.send(STATUS_FINISHED, result);
                    }
                });
    }

    private class Subscriber extends rx.Subscriber<Response<TkpdResponse>> {
        int type;
        int position;
        ErrorListener listener;

        public Subscriber(int type, int position) {
            this.position = position;
            this.type = type;
            listener = new ErrorListener(type);
        }

        public Subscriber(int type) {
            this.type = type;
            listener = new ErrorListener(type);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            listener.noConnection();
        }

        @Override
        public void onNext(Response<TkpdResponse> responseData) {
            if (responseData.isSuccessful()) {
                TkpdResponse response = responseData.body();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.getStringData());
                } catch (JSONException je) {
                    Log.e(TAG, messageTAG + je.getLocalizedMessage());
                }
                if (!response.isError()) {
                    switch (type) {
                        case REJECT_ORDER_WITH_REASON:
                            DataResponseReject dataResponseReject = gson.fromJson(jsonObject.toString(), DataResponseReject.class);
                            Bundle result = new Bundle();
                            result.putInt(TYPE, type);
                            result.putInt(ShopShippingDetailView.POSITION, position);
                            result.putParcelable(DataResponseReject.MODEL_DATA_REJECT_RESPONSE_KEY, Parcels.wrap(dataResponseReject));
                            receiver.send(STATUS_FINISHED, result);
//                            sendBroadcast(STATUS_FINISHED, result);
                            break;
                        case REJECT_ORDER_CLOSE_SHOP:
                        case REJECT_ORDER_WITH_PRICE:
                        case REJECT_ORDER_WITH_DESCRIPTION:
                        case REJECT_ORDER:
                            dataResponseReject = gson.fromJson(jsonObject.toString(), DataResponseReject.class);
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            result.putParcelable(DataResponseReject.MODEL_DATA_REJECT_RESPONSE_KEY, Parcels.wrap(dataResponseReject));
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case CONFIRM_NEW_ORDER:
                        case PARTIAL_NEW_ORDER:
                        case REJECT_NEW_ORDER:
                            Data data = gson.fromJson(jsonObject.toString(), Data.class);
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            result.putInt(ShopShippingDetailView.POSITION, position);
                            result.putParcelable(MODEL_CONFIRM_SHIPPING_KEY, Parcels.wrap(data));
                            receiver.send(STATUS_FINISHED, result);
//                            sendBroadcast(STATUS_FINISHED, result);
                            break;
                        case CONFIRM_MULTI_SHIPPING:
                        case CONFIRM_SHIPPING:
                        case CANCEL_SHIPPING:
                            data = gson.fromJson(jsonObject.toString(), Data.class);
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            result.putInt(ShopShippingDetailView.POSITION, position);
                            result.putParcelable(MODEL_CONFIRM_SHIPPING_KEY, Parcels.wrap(data));

                            receiver.send(STATUS_FINISHED, result);
                            break;
                    }// end of switch-case
                } else {
                    onMessageError(response.getErrorMessages());
                }
            } else {
                new ErrorHandler(listener, responseData.code());
            }
        }

        /**
         * No connection still not known
         */
        public class ErrorListener implements com.tokopedia.core.network.retrofit.response.ErrorListener {
            int errorCode;
            String error;

            public ErrorListener(int errorCode) {
                this.errorCode = errorCode;
                error = getString(R.string.error_connection_problem);
            }

            public void onResponse() {
                Bundle resultData = new Bundle();
                switch (type) {
                    case CONFIRM_NEW_ORDER:
                    case PARTIAL_NEW_ORDER:
                    case REJECT_NEW_ORDER:
                        resultData.putInt(TYPE, type);
                        resultData.putInt(ShopShippingDetailView.POSITION, position);
                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
                        resultData.putString(MESSAGE_ERROR_FLAG, error.toString());
                        receiver.send(STATUS_ERROR, resultData);
//                        sendBroadcast(STATUS_ERROR, resultData);
                        break;
                    case REJECT_ORDER_WITH_REASON:
                        resultData.putInt(ShopShippingDetailView.POSITION, position);
                        resultData.putInt(TYPE, type);
                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
                        resultData.putString(MESSAGE_ERROR_FLAG, error.toString());
                        receiver.send(STATUS_ERROR, resultData);
//                        sendBroadcast(STATUS_ERROR, resultData);
                        break;
                    case REJECT_ORDER_CLOSE_SHOP:
                    case REJECT_ORDER_WITH_DESCRIPTION:
                    case REJECT_ORDER:
                    case REJECT_ORDER_WITH_PRICE:
                        resultData.putInt(TYPE, type);
                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
                        resultData.putString(MESSAGE_ERROR_FLAG, error.toString());
                        receiver.send(STATUS_ERROR, resultData);
                        break;
                    case CONFIRM_MULTI_SHIPPING:
                    case CONFIRM_SHIPPING:
                    case CANCEL_SHIPPING:
                        resultData.putInt(TYPE, type);
                        resultData.putInt(ShopShippingDetailView.POSITION, position);
                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
                        resultData.putString(MESSAGE_ERROR_FLAG, error.toString());
                        receiver.send(STATUS_ERROR, resultData);
                        break;
                }
            }

            public void noConnection() {
                error = getString(R.string.msg_network_error_1);
                onResponse();
            }

            @Override
            public void onUnknown() {
                errorCode = ResponseStatus.SC_REQUEST_TIMEOUT;
                onResponse();
            }

            @Override
            public void onTimeout() {
                errorCode = ResponseStatus.SC_REQUEST_TIMEOUT;
                onResponse();
            }

            @Override
            public void onServerError() {
                errorCode = ResponseStatus.SC_INTERNAL_SERVER_ERROR;
                onResponse();
            }

            @Override
            public void onBadRequest() {
                errorCode = ResponseStatus.SC_BAD_REQUEST;
                onResponse();
            }

            @Override
            public void onForbidden() {
                errorCode = ResponseStatus.SC_FORBIDDEN;
                onResponse();
            }
        }

        public void onMessageError(List<String> MessageError) {
            if (MessageError == null || !(MessageError.size() > 0))
                return;

            Bundle resultData = new Bundle();
            switch (type) {
                case CONFIRM_NEW_ORDER:
                case PARTIAL_NEW_ORDER:
                case REJECT_NEW_ORDER:
                    resultData.putInt(TYPE, type);
                    resultData.putInt(ShopShippingDetailView.POSITION, position);
                    resultData.putInt(NETWORK_ERROR_FLAG, MESSAGE_ERROR_FLAG_RESPONSE);
                    resultData.putString(MESSAGE_ERROR_FLAG, MessageError.toString().replace("[", "").replace("]", ""));
                    receiver.send(STATUS_ERROR, resultData);
//                    sendBroadcast(STATUS_ERROR, resultData);
                    break;
                case REJECT_ORDER_WITH_REASON:
                    resultData.putInt(ShopShippingDetailView.POSITION, position);
                    resultData.putInt(TYPE, type);
                    resultData.putInt(NETWORK_ERROR_FLAG, MESSAGE_ERROR_FLAG_RESPONSE);
                    resultData.putString(MESSAGE_ERROR_FLAG, MessageError.toString().replace("[", "").replace("]", ""));
                    receiver.send(STATUS_ERROR, resultData);
//                    sendBroadcast(STATUS_ERROR, resultData);
                    break;
                case REJECT_ORDER_CLOSE_SHOP:
                case REJECT_ORDER_WITH_PRICE:
                case REJECT_ORDER_WITH_DESCRIPTION:
                case REJECT_ORDER:
                    resultData.putInt(TYPE, type);
                    resultData.putInt(NETWORK_ERROR_FLAG, MESSAGE_ERROR_FLAG_RESPONSE);
                    resultData.putString(MESSAGE_ERROR_FLAG, MessageError.toString().replace("[", "").replace("]", ""));
                    receiver.send(STATUS_ERROR, resultData);
                    break;
                case CONFIRM_MULTI_SHIPPING:
                case CONFIRM_SHIPPING:
                case CANCEL_SHIPPING:
                    resultData.putInt(TYPE, type);
                    resultData.putInt(ShopShippingDetailView.POSITION, position);
                    resultData.putInt(NETWORK_ERROR_FLAG, MESSAGE_ERROR_FLAG_RESPONSE);
                    resultData.putString(MESSAGE_ERROR_FLAG, MessageError.toString().replace("[", "").replace("]", ""));
                    receiver.send(STATUS_ERROR, resultData);
                    break;
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
