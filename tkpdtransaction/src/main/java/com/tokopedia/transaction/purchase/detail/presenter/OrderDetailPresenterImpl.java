package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.common.data.order.OrderDetailItemData;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailView;
import com.tokopedia.transaction.purchase.detail.interactor.OrderDetailInteractor;
import com.tokopedia.transaction.common.data.order.OrderDetailShipmentModel;
import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.buyagain.ResponseBuyAgain;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by kris on 11/10/17. Tokopedia
 */

public class OrderDetailPresenterImpl implements OrderDetailPresenter {

    public static final String PRODUCT_ID = "product_id";
    public static final String QUANTITY = "quantity";
    public static final String NOTES = "notes";
    public static final String SHOP_ID = "shop_id";
    public static final String PARAM = "param";

    private OrderDetailView mainView;

    private OrderDetailInteractor orderDetailInteractor;
    private GraphqlUseCase buyAgainUseCase;

    public OrderDetailPresenterImpl(OrderDetailInteractor orderDetailInteractor, GraphqlUseCase buyAgainUseCase) {
        this.orderDetailInteractor = orderDetailInteractor;
        this.buyAgainUseCase = buyAgainUseCase;
    }

    @Override
    public void setMainViewListener(OrderDetailView view) {
        mainView = view;
    }

    @Override
    public void fetchData(Context context, String orderId, int userMode) {
        mainView.showMainViewLoadingPage();
        TKPDMapParam<String, String> temporaryHash = new TKPDMapParam<>();
        temporaryHash.put(ORDER_ID_KEY, orderId);
        temporaryHash.put(USER_ID_KEY, SessionHandler.getLoginID(context));
        temporaryHash.put(LANGUAGE_KEY, INDONESIAN_LANGUAGE_CONSTANT);
        temporaryHash = AuthUtil.generateParamsNetwork(context, temporaryHash);
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.putAll(temporaryHash);
        params.put(OS_TYPE_KEY, 1);
        params.put(REQUEST_BY_KEY, userMode);
        orderDetailInteractor.requestDetailData(orderDetailDataSubscriber(), params);
    }

    @Override
    public void processInvoice(Context context, OrderDetailData data, boolean seller) {
        AppUtils.InvoiceDialog(
                context, data.getInvoiceUrl(),
                data.getInvoiceNumber(), seller
        );
    }

    @Override
    public void processToShop(Context context, OrderDetailData data) {

    }

    @Override
    public void processShowComplain(Context context, OrderDetailData data) {
        mainView.onViewComplaint(data);
    }

    @Override
    public void processComplaint(Context context, OrderDetailData data) {
        mainView.showComplaintDialog(data.getShopName(), data.getOrderId());
    }

    @Override
    public void processConfirmDeliver(Context context, OrderDetailData data) {
        mainView.showConfirmDialog(data.getOrderId(), data.getOrderCode());
    }

    @Override
    public void processTrackOrder(Context context, OrderDetailData data) {
        mainView.trackShipment(data.getOrderId(), data.getLiveTrackingUrl());
    }

    @Override
    public void processRequestCancelOrder(Context context, OrderDetailData data) {
        mainView.onRequestCancelOrder(data);
    }

    @Override
    public void processSeeAllHistories(Context context, OrderDetailData data) {

    }

    @Override
    public void processAskSeller(Context context, OrderDetailData data) {
        mainView.onAskSeller(data);
    }

    @Override
    public void processChangeAwb(Context context, OrderDetailData data) {
        mainView.onChangeAwb(data);
    }

    @Override
    public void processSellerConfirmShipping(Context context, OrderDetailData data) {
        mainView.onSellerConfirmShipping(data);
    }

    @Override
    public void processAskBuyer(Context context, OrderDetailData data) {
        mainView.onAskBuyer(data);
    }

    @Override
    public void processAcceptOrder(Context context, OrderDetailData data) {
        mainView.onAcceptOrder(data);
    }

    @Override
    public void processAcceptPartialOrder(Context context, OrderDetailData data) {
        mainView.onAcceptOrderPartially(data);
    }

    @Override
    public void processRequestPickup(Context context, OrderDetailData data) {
        mainView.onRequestPickup(data);
    }

    @Override
    public void processChangeCourier(Context context, OrderDetailData data) {
        mainView.onChangeCourier(data);
    }

    @Override
    public void processRejectOrder(Context context, OrderDetailData data) {
        mainView.onRejectOrder(data);
    }

    @Override
    public void processRejectShipment(Context context, OrderDetailData data) {
        mainView.onRejectShipment(data);
    }

    @Override
    public void processCancelSearch(Context context, OrderDetailData data) {
        mainView.onCancelSearchReplacement(data);
    }

    @Override
    public void acceptOrder(Context context, String orderId) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> acceptOrderParam = new TKPDMapParam<>();
        acceptOrderParam.put(ACTION_TYPE_KEY, ACCEPT_ORDER_CONSTANT);
        acceptOrderParam.put(ORDER_ID_KEY, orderId);
        orderDetailInteractor.processOrder(refreshActivitySubscriber(),
                AuthUtil.generateParamsNetwork(context, acceptOrderParam));

    }

    @Override
    public void confirmChangeAwb(Context context, String orderId, String refNumber) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> changeAwbParam = new TKPDMapParam<>();
        changeAwbParam.put(ORDER_ID_KEY, orderId);
        changeAwbParam.put(SHIPPING_REF_KEY, refNumber);
        orderDetailInteractor.confirmAwb(sellerActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, changeAwbParam));
    }

    @Override
    public void partialOrder(Context context,
                             String orderId,
                             String reason,
                             String quantityAccept) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> partialParam = new TKPDMapParam<>();
        partialParam.put(ACTION_TYPE_KEY, PARTIAL_ORDER_CONSTANT);
        partialParam.put(ORDER_ID_KEY, orderId);
        partialParam.put(REASON_KEY, reason);
        partialParam.put(QUANTITY_ACCEPT_KEY, quantityAccept);
        orderDetailInteractor.processOrder(refreshActivitySubscriber(),
                AuthUtil.generateParamsNetwork(context, partialParam));
    }

    @Override
    public void rejectOrder(Context context, String orderId, String reason) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> rejectOrderParam = new TKPDMapParam<>();
        rejectOrderParam.put(ACTION_TYPE_KEY, REJECT_ORDER_CONSTANT);
        rejectOrderParam.put(ORDER_ID_KEY, orderId);
        rejectOrderParam.put(REASON_KEY, reason);
        orderDetailInteractor.processOrder(sellerFragmentActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, rejectOrderParam));
    }

    @Override
    public void rejectOrderGenericReason(Context context, TKPDMapParam<String, String> reasonParam) {
        mainView.showProgressDialog();
        reasonParam.put(ACTION_TYPE_KEY, REJECT_ORDER_CONSTANT);
        orderDetailInteractor.processOrder(rejectOrderActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, reasonParam));
    }

    @Override
    public void rejectOrderChangeVarian(Context context,
                                        List<EmptyVarianProductEditable> emptyVarianProductEditables) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> rejectVarianParam = new TKPDMapParam<>();
        rejectVarianParam.put(ACTION_TYPE_KEY, REJECT_ORDER_CONSTANT);
        rejectVarianParam.put(REASON_CODE_KEY, CHANGE_VARIAN_CODE);
        rejectVarianParam.put(ORDER_ID_KEY, emptyVarianProductEditables.get(0).getOrderId());
        orderDetailInteractor.rejectEmptyOrderVarian(
                rejectOrderActionSubscriber(),
                emptyVarianProductEditables,
                AuthUtil.generateParamsNetwork(context, new TKPDMapParam<String, String>()),
                AuthUtil.generateParamsNetwork(context, rejectVarianParam));

    }

    @Override
    public void rejectOrderChangeWeightPrice(
            Context context,
            List<WrongProductPriceWeightEditable> editables
    ) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> changeWeightPriceParam = new TKPDMapParam<>();
        changeWeightPriceParam.put(ACTION_TYPE_KEY, REJECT_ORDER_CONSTANT);
        changeWeightPriceParam.put(REASON_CODE_KEY, CHANGE_PRODUCT_CODE);
        changeWeightPriceParam.put(ORDER_ID_KEY, editables.get(0).getOrderId());
        orderDetailInteractor.rejectChangeWeightPrice(
                rejectOrderActionSubscriber(),
                editables,
                AuthUtil.generateParamsNetwork(context, new TKPDMapParam<String, String>()),
                AuthUtil.generateParamsNetwork(context, changeWeightPriceParam));
    }

    @Override
    public void processInstantCourierShipping(Context context, String orderId) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> processShippingParam = new TKPDMapParam<>();
        processShippingParam.put(ACTION_TYPE_KEY, CONFIRM_SHIPPING_CONSTANT);
        processShippingParam.put(ORDER_ID_KEY, orderId);
        orderDetailInteractor.confirmShipping(sellerActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, processShippingParam));
    }

    @Override
    public void processShipping(Context context,
                                OrderDetailShipmentModel shipmentModel) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> processShippingParam = new TKPDMapParam<>();
        processShippingParam.put(ACTION_TYPE_KEY, CONFIRM_SHIPPING_CONSTANT);
        processShippingParam.put(ORDER_ID_KEY, shipmentModel.getOrderId());
        processShippingParam.put(SHIPPING_REF_KEY, shipmentModel.getShippingRef());
        processShippingParam.put(SHIPMENT_ID_KEY, shipmentModel.getShipmentId());
        processShippingParam.put(SHIPMENT_NAME_KEY, shipmentModel.getShipmentName());
        processShippingParam.put(SERVICE_ID_KEY, shipmentModel.getPackageId());
        orderDetailInteractor.processOrder(sellerActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, processShippingParam));

    }

    @Override
    public void cancelShipping(Context context, String orderId, String reason) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> processShippingParam = new TKPDMapParam<>();
        processShippingParam.put(ACTION_TYPE_KEY, REJECT_ORDER_CONSTANT);
        processShippingParam.put(ORDER_ID_KEY, orderId);
        processShippingParam.put(REASON_KEY, reason);
        orderDetailInteractor.cancelShipping(sellerActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, processShippingParam));
    }

    @Override
    public void retryOrder(Context context, OrderDetailData data) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> retryPickUpParams = new TKPDMapParam<>();
        retryPickUpParams.put(ORDER_ID_KEY, data.getOrderId());
        orderDetailInteractor.retryPickup(
                sellerActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, retryPickUpParams));
    }

    @Override
    public void cancelOrder(Context context, String orderId, String notes) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> cancelOrderParam = new TKPDMapParam<>();
        cancelOrderParam.put(ORDER_ID_KEY, orderId);
        cancelOrderParam.put(REASON_CANCEL_KEY, notes);
        orderDetailInteractor.cancelOrder(cancelOrderSubscriber(),
                AuthUtil.generateParamsNetwork(context, cancelOrderParam));
    }

    @Override
    public void cancelReplacement(Context context, String orderId,
                                  int reasonCode,
                                  String reasonText) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> temporaryParam = new TKPDMapParam<>();
        temporaryParam.put(ORDER_ID_KEY, orderId);
        temporaryParam.put(REASON_KEY, reasonText);
        temporaryParam.put(USER_ID_KEY, SessionHandler.getLoginID(context));
        temporaryParam = AuthUtil.generateParamsNetwork(context, temporaryParam);
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.putAll(temporaryParam);
        params.put(REPLACEMENT_REASON_CODE, reasonCode);
        orderDetailInteractor.cancelReplacement(cancelReplacementSubscriber(), params);
    }

    @Override
    public void processBuyAgain(Resources resources, OrderDetailData data) {
        mainView.showProgressDialog();
        Map<String, Object> variables = new HashMap<>();
        variables.put(PARAM, generateInputQueryBuyAgain(data));

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.buy_again), ResponseBuyAgain.class, variables);

        buyAgainUseCase.clearRequest();
        buyAgainUseCase.addRequest(graphqlRequest);

        buyAgainUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(mainView != null) {
                    mainView.dismissProgressDialog();
                    mainView.onErrorBuyAgain(e);
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                mainView.dismissProgressDialog();
                ResponseBuyAgain responseBuyAgain = objects.getData(ResponseBuyAgain.class);
                if(responseBuyAgain.getAddToCartMulti().getData().getSuccess() == 1){
                    mainView.onSuccessBuyAgain(StringUtils.convertListToStringDelimiter(responseBuyAgain.getAddToCartMulti().getData().getMessage(), ","), data);
                }else{
                    mainView.onErrorBuyAgain( new MessageErrorException(StringUtils.convertListToStringDelimiter(responseBuyAgain.getAddToCartMulti().getData().getMessage(),",")));
                }
            }
        });

    }

    private JsonArray generateInputQueryBuyAgain(OrderDetailData data) {
        List<OrderDetailItemData> orderDetailItemData = data.getItemList();
        JsonArray jsonArray = new JsonArray();
        for (OrderDetailItemData dataOrder : orderDetailItemData) {
            JsonObject passenger = new JsonObject();

            int productId = 0;
            int quantity = 0;
            int shopId = 0;
            try {
                productId = Integer.parseInt(dataOrder.getProductId());
                quantity = Integer.parseInt(dataOrder.getItemQuantity());
                shopId = Integer.parseInt(data.getShopId());
            }catch (Exception e){
                Log.e("error parse", e.getMessage());
            }
            passenger.addProperty(PRODUCT_ID, productId);
            passenger.addProperty(QUANTITY, quantity);
            passenger.addProperty(NOTES, dataOrder.getNotes());
            passenger.addProperty(SHOP_ID, shopId);
            jsonArray.add(passenger);
        }
        return jsonArray;
    }

    @Override
    public void processFinish(Context context, String orderId, String orderStatus) {
        TKPDMapParam<String, String> orderDetailParams = new TKPDMapParam<>();
        orderDetailParams.put(ORDER_ID_KEY, orderId);
        if (orderStatus.equals(context.getString(com.tokopedia.core2.R.string.ORDER_DELIVERED))
                || orderStatus.equals(context.getString(com.tokopedia.core2.R.string.ORDER_DELIVERY_FAILURE))) {
            orderDetailInteractor.confirmDeliveryConfirm(confirmShipmentSubscriber(),
                    AuthUtil.generateParamsNetwork(context, orderDetailParams));
        } else {
            orderDetailInteractor.confirmFinishConfirm(confirmShipmentSubscriber(),
                    AuthUtil.generateParamsNetwork(context, orderDetailParams));
        }
    }

    @Override
    public boolean isToggleBuyAgainOn() {
        return mainView.isToggleBuyAgainOn();
    }

    @Override
    public void onDestroyed() {
        orderDetailInteractor.onActivityClosed();
        buyAgainUseCase.unsubscribe();
    }

    private Subscriber<OrderDetailData> orderDetailDataSubscriber() {
        return new Subscriber<OrderDetailData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.hideMainViewLoadingPage();
                mainView.onError(e.getMessage());
            }

            @Override
            public void onNext(OrderDetailData data) {
                mainView.hideMainViewLoadingPage();
                mainView.onReceiveDetailData(data);
            }
        };
    }

    private Subscriber<String> confirmShipmentSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.showSnackbar(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.onOrderFinished(s);
            }
        };
    }

    private Subscriber<String> cancelOrderSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.dismissProgressDialog();
                mainView.onOrderCancelled(s);
            }
        };
    }

    private Subscriber<String> closeActivitySubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(s);
                mainView.dismissActivity();
                //TODO put action to finish activity and refresh
            }
        };
    }

    private Subscriber<String> refreshActivitySubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.dismissProgressDialog();
                mainView.showSnackbarWithCloseButton(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(s);
                mainView.dismissActivity();
                //TODO put action to finish activity and refresh
            }
        };
    }

    private Subscriber<String> sellerActionSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(s);
                mainView.dismissSellerActionFragment();
                //TODO put action to finish activity and refresh
            }
        };
    }

    private Subscriber<String> sellerFragmentActionSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(s);
                mainView.dismissSellerActionFragment();
                //TODO put action to finish activity and refresh
            }
        };
    }

    private Subscriber<String> rejectOrderActionSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.dismissProgressDialog();
                mainView.dismissRejectOrderActionFragment();
                //TODO put action to finish activity and refresh
            }
        };
    }

    private Subscriber<String> cancelReplacementSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.dismissProgressDialog();
                mainView.showSnackbar(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.dismissProgressDialog();
                mainView.onSearchCancelled(s);
            }
        };
    }
}
