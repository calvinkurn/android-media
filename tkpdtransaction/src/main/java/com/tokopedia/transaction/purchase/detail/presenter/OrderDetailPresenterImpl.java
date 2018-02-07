package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailView;
import com.tokopedia.transaction.purchase.detail.interactor.OrderDetailInteractor;
import com.tokopedia.transaction.purchase.detail.model.detail.editmodel.OrderDetailShipmentModel;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

import rx.Subscriber;

/**
 * Created by kris on 11/10/17. Tokopedia
 */

public class OrderDetailPresenterImpl implements OrderDetailPresenter {

    private OrderDetailView mainView;

    private OrderDetailInteractor orderDetailInteractor;

    public OrderDetailPresenterImpl(OrderDetailInteractor orderDetailInteractor) {
        this.orderDetailInteractor = orderDetailInteractor;
    }

    @Override
    public void setMainViewListener(OrderDetailView view) {
        mainView = view;
    }

    @Override
    public void fetchData(Context context, String orderId, int userMode) {
        mainView.showMainViewLoadingPage();
        TKPDMapParam<String, String> temporaryHash = new TKPDMapParam<>();
        temporaryHash.put("order_id", orderId);
        temporaryHash.put("user_id", SessionHandler.getLoginID(context));
        temporaryHash.put("lang", "id");
        temporaryHash = AuthUtil.generateParamsNetwork(context, temporaryHash);
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.putAll(temporaryHash);
        params.put("os_type", 1);
        params.put("request_by", userMode);
        orderDetailInteractor.requestDetailData(orderDetailDataSubscriber(), params);
    }

    @Override
    public void processInvoice(Context context, OrderDetailData data) {
        AppUtils.InvoiceDialog(
                context, data.getInvoiceUrl(),
                data.getInvoiceNumber()
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
        mainView.trackShipment(data.getOrderId());
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
        acceptOrderParam.put("action_type", "accept");
        acceptOrderParam.put("order_id", orderId);
        orderDetailInteractor.processOrder(sellerActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, acceptOrderParam));

    }

    @Override
    public void confirmChangeAwb(Context context, String orderId, String refNumber) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> changeAwbParam = new TKPDMapParam<>();
        changeAwbParam.put("order_id", orderId);
        changeAwbParam.put("shipping_ref", refNumber);
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
        partialParam.put("action_type", "partial");
        partialParam.put("order_id", orderId);
        partialParam.put("reason", reason);
        partialParam.put("qty_accept", quantityAccept);
        orderDetailInteractor.processOrder(sellerActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, partialParam));
    }

    @Override
    public void rejectOrder(Context context, String orderId, String reason) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> rejectOrderParam = new TKPDMapParam<>();
        rejectOrderParam.put("action_type", "reject");
        rejectOrderParam.put("reason", orderId);
        orderDetailInteractor.processOrder(sellerFragmentActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, rejectOrderParam));
    }

    @Override
    public void processShipping(Context context,
                                OrderDetailShipmentModel shipmentModel) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> processShippingParam = new TKPDMapParam<>();
        processShippingParam.put("action_type", "confirm");
        processShippingParam.put("order_id", shipmentModel.getOrderId());
        processShippingParam.put("shipping_ref", shipmentModel.getShippingRef());
        processShippingParam.put("shipment_id", shipmentModel.getShipmentId());
        processShippingParam.put("shipment_name", shipmentModel.getShipmentName());
        processShippingParam.put("sp_id", shipmentModel.getPackageId());
        orderDetailInteractor.processOrder(sellerActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, processShippingParam));

    }

    @Override
    public void retryOrder(Context context, OrderDetailData data) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> retryPickUpParams = new TKPDMapParam<>();
        retryPickUpParams.put("order_id", data.getOrderId());
        orderDetailInteractor.retryPickup(
                sellerActionSubscriber(),
                AuthUtil.generateParamsNetwork(context, retryPickUpParams));
    }

    @Override
    public void cancelOrder(Context context, String orderId, String notes) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> cancelOrderParam = new TKPDMapParam<>();
        cancelOrderParam.put("order_id", orderId);
        cancelOrderParam.put("reason_cancel", notes);
        orderDetailInteractor.cancelOrder(cancelOrderSubscriber(),
                AuthUtil.generateParamsNetwork(context, cancelOrderParam));
    }

    @Override
    public void cancelReplacement(Context context, String orderId,
                                  int reasonCode,
                                  String reasonText) {
        mainView.showProgressDialog();
        TKPDMapParam<String, String> temporaryParam = new TKPDMapParam<>();
        temporaryParam.put("order_id", orderId);
        temporaryParam.put("reason", reasonText);
        temporaryParam.put("user_id", SessionHandler.getLoginID(context));
        temporaryParam = AuthUtil.generateParamsNetwork(context, temporaryParam);
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.putAll(temporaryParam);
        params.put("r_code", reasonCode);
        orderDetailInteractor.cancelReplacement(cancelReplacementSubscriber(), params);
    }

    @Override
    public void processFinish(Context context, String orderId, String orderStatus) {
        TKPDMapParam<String, String> orderDetailParams = new TKPDMapParam<>();
        orderDetailParams.put("order_id", orderId);
        if (orderStatus.equals(context.getString(com.tokopedia.core.R.string.ORDER_DELIVERED))
                || orderStatus.equals(context.getString(com.tokopedia.core.R.string.ORDER_DELIVERY_FAILURE))) {
            orderDetailInteractor.confirmDeliveryConfirm(confirmShipmentSubscriber(),
                    AuthUtil.generateParamsNetwork(context, orderDetailParams));
        } else {
            orderDetailInteractor.confirmFinishConfirm(confirmShipmentSubscriber(),
                    AuthUtil.generateParamsNetwork(context, orderDetailParams));
        }
    }

    @Override
    public void onDestroyed() {
        orderDetailInteractor.onActivityClosed();
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
