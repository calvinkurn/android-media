package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailView;
import com.tokopedia.transaction.purchase.detail.interactor.OrderDetailInteractor;
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
    public void processCancelSearch(Context context, OrderDetailData data) {
        mainView.onCancelSearchReplacement(data);
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
                mainView.showErrorSnackbar(e.getMessage());
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
                mainView.showErrorSnackbar(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.dismissProgressDialog();
                mainView.onOrderCancelled(s);
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
                mainView.showErrorSnackbar(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mainView.dismissProgressDialog();
                mainView.onSearchCancelled(s);
            }
        };
    }
}
